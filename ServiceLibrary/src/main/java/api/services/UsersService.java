package api.services;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import api.dtos.UserDto;

public interface UsersService {

	@GetMapping("/users")
	List<UserDto> getUsers();

	@GetMapping("/users/email")
	UserDto getUserByEmail(@RequestParam String email);

	@PostMapping("/users/newAdmin")
	ResponseEntity<?> createAdmin(@RequestBody UserDto dto, @RequestHeader("X-User-Role") String callerRole);

	@PostMapping("/users/newUser")
	ResponseEntity<?> createUser(@RequestBody UserDto dto, @RequestHeader("X-User-Role") String callerRole);

	@PostMapping("/users/newOwner")
	ResponseEntity<?> createOwner(@RequestBody UserDto dto, @RequestHeader("X-User-Role") String callerRole);

	@PutMapping("/users")
	ResponseEntity<?> updateUser(@RequestBody UserDto dto, @RequestHeader("X-User-Role") String callerRole, @RequestHeader("X-User-Email") String callerEmail);

	@DeleteMapping("/users")
	ResponseEntity<?> deleteUser(@RequestParam String email, @RequestHeader("X-User-Role") String callerRole);

}
