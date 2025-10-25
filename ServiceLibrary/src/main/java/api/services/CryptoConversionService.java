package api.services;

import java.math.BigDecimal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

public interface CryptoConversionService {

	ResponseEntity<?> convertCrypto(@RequestParam String from, @RequestParam String to,
			@RequestParam BigDecimal quantity, @RequestHeader("X-User-Email") String userEmail,
			@RequestHeader("X-User-Role") String userRole);
}