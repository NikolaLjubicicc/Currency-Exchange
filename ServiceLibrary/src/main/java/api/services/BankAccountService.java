package api.services;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import api.dtos.BankAccountDto;

public interface BankAccountService {

	ResponseEntity<?> getBankAccountByEmail(@RequestParam String email, @RequestHeader("X-User-Role") String callerRole,
			@RequestHeader("X-User-Email") String callerEmail);

	ResponseEntity<?> getAllBankAccounts(@RequestHeader("X-User-Role") String callerRole);

	ResponseEntity<?> createBankAccount(@RequestBody BankAccountDto dto,
			@RequestHeader("X-User-Role") String callerRole);

	ResponseEntity<?> updateBankAccount(@RequestBody BankAccountDto dto,
			@RequestHeader("X-User-Role") String callerRole, @RequestHeader("X-User-Email") String callerEmail);

	ResponseEntity<?> deleteBankAccount(@RequestParam String email, @RequestHeader("X-User-Role") String callerRole);
}