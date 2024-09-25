package kg.purple.blinkchat.service.impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import kg.purple.blinkchat.config.JwtService;
import kg.purple.blinkchat.dto.AuthRequest;
import kg.purple.blinkchat.dto.AuthResponse;
import kg.purple.blinkchat.dto.RegisterRequest;
import kg.purple.blinkchat.model.User;
import kg.purple.blinkchat.repository.UserRepository;
import kg.purple.blinkchat.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;
	
	@Transactional
	@Override
	public AuthResponse register(RegisterRequest request) {
		if (userRepository.findByUsername(request.getUsername()).isPresent())
			throw new EntityExistsException("User with username " + request.getUsername() + " already exists");
		
		User user = User.builder()
				.username(request.getUsername())
				.password(passwordEncoder.encode(request.getPassword()))
				.name(request.getName())
				.surname(request.getSurname())
				.enabled(true)
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
}
