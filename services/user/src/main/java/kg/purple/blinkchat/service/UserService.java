package kg.purple.blinkchat.service;

import kg.purple.blinkchat.dto.AuthRequest;
import kg.purple.blinkchat.dto.AuthResponse;
import kg.purple.blinkchat.dto.RegisterRequest;

public interface UserService {
	AuthResponse register(RegisterRequest request);
	
	AuthResponse authenticate(AuthRequest request);
}
