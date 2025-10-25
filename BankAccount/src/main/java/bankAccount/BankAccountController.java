package bankAccount;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import api.dtos.BankAccountDto;
import api.services.BankAccountService;

@RestController
public class BankAccountController {

	@Autowired
	private BankAccountService service;

	@GetMapping("/bank-account")
	public ResponseEntity<?> getBankAccountByEmail(@RequestParam String email,
			@RequestHeader("X-User-Role") String callerRole, @RequestHeader("X-User-Email") String callerEmail) {
		return service.getBankAccountByEmail(email, callerRole, callerEmail);
	}

	@GetMapping("/bank-account/all")
	public ResponseEntity<?> getAllBankAccounts(@RequestHeader("X-User-Role") String callerRole) {
		return service.getAllBankAccounts(callerRole);
	}

	@PostMapping("/bank-account")
	public ResponseEntity<?> createBankAccount(@RequestBody BankAccountDto dto,
			@RequestHeader("X-User-Role") String callerRole) {
		return service.createBankAccount(dto, callerRole);
	}

	@PutMapping("/bank-account")
	public ResponseEntity<?> updateBankAccount(@RequestBody BankAccountDto dto,
			@RequestHeader("X-User-Role") String callerRole, @RequestHeader("X-User-Email") String callerEmail) {
		return service.updateBankAccount(dto, callerRole, callerEmail);
	}

	@DeleteMapping("/bank-account")
	public ResponseEntity<?> deleteBankAccount(@RequestParam String email,
			@RequestHeader("X-User-Role") String callerRole) {
		return service.deleteBankAccount(email, callerRole);
	}
}