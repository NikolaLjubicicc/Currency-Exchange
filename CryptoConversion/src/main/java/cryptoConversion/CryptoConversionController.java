package cryptoConversion;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import api.services.CryptoConversionService;

@RestController
public class CryptoConversionController {

	@Autowired
	private CryptoConversionService service;

	@GetMapping("/crypto-conversion-feign")
	public ResponseEntity<?> convertCrypto(@RequestParam String from, @RequestParam String to,
			@RequestParam BigDecimal quantity, @RequestHeader("X-User-Email") String userEmail,
			@RequestHeader("X-User-Role") String userRole) {
		return service.convertCrypto(from, to, quantity, userEmail, userRole);
	}
}