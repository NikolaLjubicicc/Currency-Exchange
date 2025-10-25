package api.proxies;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import api.dtos.BankAccountDto;

@FeignClient("bank-account")
public interface BankAccountProxy {

	@GetMapping("/bank-account")
	ResponseEntity<BankAccountDto> getBankAccountByEmail(@RequestParam String email, @RequestHeader("X-User-Role") String callerRole, @RequestHeader("X-User-Email") String callerEmail);

	@PostMapping("/bank-account")
	ResponseEntity<BankAccountDto> createBankAccount(@RequestBody BankAccountDto dto, @RequestHeader("X-User-Role") String callerRole);

	@PutMapping("/bank-account")
	ResponseEntity<BankAccountDto> updateBankAccount(@RequestBody BankAccountDto dto, @RequestHeader("X-User-Role") String callerRole, @RequestHeader("X-User-Email") String callerEmail);

	@DeleteMapping("/bank-account")
	ResponseEntity<?> deleteBankAccount(@RequestParam String email, @RequestHeader("X-User-Role") String callerRole);
}