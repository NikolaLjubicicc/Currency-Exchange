package api.services;

import java.math.BigDecimal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

public interface TradeService {

	ResponseEntity<?> buyFiatToCrypto(@RequestParam String fiatCurrency, @RequestParam String cryptoCurrency,
			@RequestParam BigDecimal fiatAmount, @RequestHeader("X-User-Email") String userEmail, @RequestHeader("X-User-Role") String userRole);

	ResponseEntity<?> sellCryptoToFiat(@RequestParam String cryptoCurrency, @RequestParam String fiatCurrency,
			@RequestParam BigDecimal cryptoAmount, @RequestHeader("X-User-Email") String userEmail, @RequestHeader("X-User-Role") String userRole);

	ResponseEntity<?> trade(@RequestParam String from, @RequestParam String to, @RequestParam BigDecimal quantity,
			@RequestHeader("X-User-Email") String userEmail, @RequestHeader("X-User-Role") String userRole);
}