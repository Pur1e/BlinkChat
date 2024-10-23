package kg.purple.blinkchat.service.impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import kg.purple.blinkchat.config.JwtService;
import kg.purple.blinkchat.dto.AuthRequest;
import kg.purple.blinkchat.dto.AuthResponse;
import kg.purple.blinkchat.dto.RegisterRequest;
import kg.purple.blinkchat.dto.UserDto;
import kg.purple.blinkchat.model.Role;
import kg.purple.blinkchat.model.User;
import kg.purple.blinkchat.repository.RoleRepository;
import kg.purple.blinkchat.repository.UserRepository;
import kg.purple.blinkchat.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;
	private final RoleRepository roleRepository;
	
	@Transactional
	@Override
	public AuthResponse register(RegisterRequest request) {
		if (userRepository.findByUsername(request.getUsername()).isPresent())
			throw new EntityExistsException("User with username " + request.getUsername() + " already exists");
		
		Role userRole = roleRepository.findByName("USER")
				.orElseThrow(() -> new EntityNotFoundException("Default role not found"));
		
		User user = User.builder()
				.username(request.getUsername())
				.password(passwordEncoder.encode(request.getPassword()))
				.name(request.getName())
				.surname(request.getSurname())
				.enabled(true)
				.roles(Set.of(userRole))
				.build();
		
		userRepository.saveAndFlush(user);
		
		String token = jwtService.generateToken(user);
		
		return AuthResponse.builder()
				.token(token)
				.build();
	}
	
	@Override
	public AuthResponse authenticate(AuthRequest request) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				request.getUsername(),
				request.getPassword()
		));
		
		var user = userRepository.findByUsername(request.getUsername())
				.orElseThrow(() -> new EntityNotFoundException("User not found"));
		
		var token = jwtService.generateToken(user);
		
		return AuthResponse.builder()
				.token(token)
				.build();
	}
	
	@Override
	public UserDto getUserByUsername(String username) {
		User user = findUserByUsername(username);
		
		return UserDto.builder()
				.username(user.getUsername())
				.name(user.getName())
				.surname(user.getSurname())
				.build();
	}
	
	@Override
	@Modifying
	public UserDto updateUserByUsername(String username, UserDto request) {
		User user = findUserByUsername(username);
		
		if (request != null) {
			if (request.getName() != null) user.setName(request.getName());
			if (request.getSurname() != null) user.setSurname(request.getSurname());
		} else {
			throw new IllegalArgumentException("Request must not be null");
		}
		
		userRepository.save(user);
		
		return UserDto.builder()
				.username(user.getUsername())
				.name(user.getName())
				.surname(user.getSurname())
				.build();
	}
	
	@Override
	@Modifying
	public void deleteUserByUsername(String username) {
		User user = findUserByUsername(username);
		user.setEnabled(false);
		userRepository.save(user);
	}
	
	@Override
	public List<UserDto> getUsersByUsername(String username) {
		return userRepository.findAllByUsernameContainingIgnoreCase(username)
				.stream()
				.map(u -> UserDto.builder()
						.username(u.getUsername())
						.surname(u.getSurname())
						.name(u.getName())
						.build())
				.toList();
	}
	
	private User findUserByUsername(String username) {
		return userRepository.findByUsername(username)
				.orElseThrow(() -> new EntityNotFoundException("User not found with username " + username));
	}
}
