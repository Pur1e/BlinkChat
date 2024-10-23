package kg.purple.blinkchat;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import kg.purple.blinkchat.config.JwtService;
import kg.purple.blinkchat.dto.AuthRequest;
import kg.purple.blinkchat.dto.AuthResponse;
import kg.purple.blinkchat.dto.RegisterRequest;
import kg.purple.blinkchat.model.User;
import kg.purple.blinkchat.repository.RoleRepository;
import kg.purple.blinkchat.repository.UserRepository;
import kg.purple.blinkchat.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
	
	@Mock
	private UserRepository userRepository;
	
	@Mock
	private PasswordEncoder passwordEncoder;
	
	@Mock
	private JwtService jwtService;
	
	@Mock
	private AuthenticationManager authenticationManager;
	
	@Mock
	private RoleRepository roleRepository;
	
	@InjectMocks
	private UserServiceImpl userService;
	
	@BeforeEach
	void setUp() {
		userService = new UserServiceImpl(userRepository, passwordEncoder, jwtService, authenticationManager, roleRepository);
	}
	
	@Test
	void testRegisterUserSuccess() {
		RegisterRequest request = new RegisterRequest("newUser", "password", "John", "Doe");
		
		when(userRepository.findByUsername("newUser")).thenReturn(Optional.empty());
		when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
		when(jwtService.generateToken(any(User.class))).thenReturn("jwtToken");
		
		AuthResponse response = userService.register(request);
		
		verify(userRepository).saveAndFlush(any(User.class));
		assertEquals("jwtToken", response.getToken());
	}
	
	@Test
	void testRegisterUserFailure() {
		RegisterRequest request = new RegisterRequest("existingUser", "password", "John", "Doe");
		
		when(userRepository.findByUsername("existingUser")).thenReturn(Optional.of(new User()));
		
		assertThrows(EntityExistsException.class, () -> userService.register(request));
	}
	
	@Test
	void testLoginSuccess() {
		AuthRequest request = new AuthRequest("username", "password");
		
		User user = User.builder()
				.username("username")
				.password("encodedPassword")
				.build();
		
		when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));
		when(jwtService.generateToken(user)).thenReturn("jwtToken");
		
		AuthResponse response = userService.authenticate(request);
		
		assertEquals("jwtToken", response.getToken());
	}
	
	@Test
	void testLoginFailureUserNotFound() {
		AuthRequest request = new AuthRequest("username", "password");
		
		when(userRepository.findByUsername("username")).thenReturn(Optional.empty());
		
		assertThrows(EntityNotFoundException.class, () -> userService.authenticate(request));
	}
	
	@Test
	void testLoginFailureInvalidPassword() {
		AuthRequest request = new AuthRequest("username", "wrongPassword");
		
		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
				.thenThrow(new BadCredentialsException("Invalid credentials"));
		
		assertThrows(BadCredentialsException.class, () -> userService.authenticate(request));
	}
}
