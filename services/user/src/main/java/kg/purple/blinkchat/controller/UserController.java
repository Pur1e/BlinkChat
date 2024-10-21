package kg.purple.blinkchat.controller;

import kg.purple.blinkchat.dto.UserDto;
import kg.purple.blinkchat.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor

public class UserController {
	
	private final UserService userService;
	
	@GetMapping("/{username}")
	public ResponseEntity<UserDto> getUser(@PathVariable String username) {
		UserDto user = userService.getUserByUsername(username);
		return ResponseEntity.ok(user);
	}
	
	@PatchMapping("/{username}")
	public ResponseEntity<UserDto> updateUser(@PathVariable String username, @RequestBody UserDto updatedUser) {
		UserDto user = userService.updateUserByUsername(username, updatedUser);
		return ResponseEntity.ok(user);
	}
	
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	@DeleteMapping("/{username}")
	public ResponseEntity<Void> deleteUser(@PathVariable String username) {
		userService.deleteUserByUsername(username);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping()
	public ResponseEntity<List<UserDto>> getUsersByUsername(@RequestParam String username) {
		List<UserDto> users = userService.getUsersByUsername(username);
		return ResponseEntity.ok(users);
	}
}
