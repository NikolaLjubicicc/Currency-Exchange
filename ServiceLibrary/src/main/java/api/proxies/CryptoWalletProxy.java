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

import api.dtos.CryptoWalletDto;

@FeignClient("crypto-wallet")
public interface CryptoWalletProxy {

	@GetMapping("/crypto-wallet")
	ResponseEntity<CryptoWalletDto> getCryptoWalletByEmail(@RequestParam String email, @RequestHeader("X-User-Role") String callerRole, @RequestHeader("X-User-Email") String callerEmail);

	@PostMapping("/crypto-wallet")
	ResponseEntity<CryptoWalletDto> createCryptoWallet(@RequestBody CryptoWalletDto dto, @RequestHeader("X-User-Role") String callerRole);

	@PutMapping("/crypto-wallet")
	ResponseEntity<CryptoWalletDto> updateCryptoWallet(@RequestBody CryptoWalletDto dto, @RequestHeader("X-User-Role") String callerRole, @RequestHeader("X-User-Email") String callerEmail);

	@DeleteMapping("/crypto-wallet")
	ResponseEntity<?> deleteCryptoWallet(@RequestParam String email, @RequestHeader("X-User-Role") String callerRole);
}