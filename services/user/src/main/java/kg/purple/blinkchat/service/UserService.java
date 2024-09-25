package kg.purple.blinkchat.service;

import kg.purple.blinkchat.dto.AuthRequest;
import kg.purple.blinkchat.dto.AuthResponse;
import kg.purple.blinkchat.dto.RegisterRequest;
import kg.purple.blinkchat.dto.UserDto;

import java.util.List;

public interface UserService {
	AuthResponse register(RegisterRequest request);
	
	AuthResponse authenticate(AuthRequest request);
	
	UserDto getUserByUsername(String username);
	
	UserDto updateUserByUsername(String username, UserDto updatedUser);
	
	void deleteUserByUsername(String username);
	
	List<UserDto> getUsersByUsername(String username);
}
