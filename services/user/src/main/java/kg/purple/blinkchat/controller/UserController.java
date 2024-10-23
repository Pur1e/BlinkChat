package kg.purple.blinkchat.controller;

import kg.purple.blinkchat.dto.UserDto;
import kg.purple.blinkchat.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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
	
	@PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
	@PatchMapping("/{username}")
	public ResponseEntity<UserDto> updateUser(@PathVariable String username, @RequestBody UserDto updatedUser) {
		if (hasAccess(username)) {
			UserDto user = userService.updateUserByUsername(username, updatedUser);
			return ResponseEntity.ok(user);
		}
		return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
	}
	
	@PreAuthorize("hasAnyAuthority('ADMIN','USER')")
	@DeleteMapping("/{username}")
	public ResponseEntity<?> deleteUser(@PathVariable String username) {
		if (hasAccess(username)) {
			userService.deleteUserByUsername(username);
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
	}
	
	@GetMapping()
	public ResponseEntity<List<UserDto>> getUsersByUsername(@RequestParam String username) {
		List<UserDto> users = userService.getUsersByUsername(username);
		return ResponseEntity.ok(users);
	}
	
	private boolean hasAccess(String username) {
		String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
		boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
				.stream().anyMatch(a -> a.getAuthority().equals("ADMIN"));
		return isAdmin || currentUsername.equals(username);
	}
}
