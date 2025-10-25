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

import api.dtos.CryptoWalletDto;

public interface CryptoWalletService {

	@GetMapping("/crypto-wallet")
	ResponseEntity<?> getCryptoWalletByEmail(@RequestParam String email, @RequestHeader("X-User-Role") String callerRole, @RequestHeader("X-User-Email") String callerEmail);

	@GetMapping("/crypto-wallet/all")
	ResponseEntity<?> getAllCryptoWallets(@RequestHeader("X-User-Role") String callerRole);

	@PostMapping("/crypto-wallet")
	ResponseEntity<?> createCryptoWallet(@RequestBody CryptoWalletDto dto, @RequestHeader("X-User-Role") String callerRole);

	@PutMapping("/crypto-wallet")
	ResponseEntity<?> updateCryptoWallet(@RequestBody CryptoWalletDto dto, @RequestHeader("X-User-Role") String callerRole, @RequestHeader("X-User-Email") String callerEmail);

	@DeleteMapping("/crypto-wallet")
	ResponseEntity<?> deleteCryptoWallet(@RequestParam String email, @RequestHeader("X-User-Role") String callerRole);
}