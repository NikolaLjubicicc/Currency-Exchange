package tradeService;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import api.services.TradeService;

@RestController
public class TradeController {

	@Autowired
	private TradeService service;

	@GetMapping("/trade/buy-crypto")
	public ResponseEntity<?> buyFiatToCrypto(@RequestParam String fiatCurrency, @RequestParam String cryptoCurrency,
			@RequestParam BigDecimal fiatAmount, @RequestHeader("X-User-Email") String userEmail,
			@RequestHeader("X-User-Role") String userRole) {
		return service.buyFiatToCrypto(fiatCurrency, cryptoCurrency, fiatAmount, userEmail, userRole);
	}

	@GetMapping("/trade/sell-crypto")
	public ResponseEntity<?> sellCryptoToFiat(@RequestParam String cryptoCurrency, @RequestParam String fiatCurrency,
			@RequestParam BigDecimal cryptoAmount, @RequestHeader("X-User-Email") String userEmail,
			@RequestHeader("X-User-Role") String userRole) {
		return service.sellCryptoToFiat(cryptoCurrency, fiatCurrency, cryptoAmount, userEmail, userRole);
	}

	@GetMapping("/trade-service")
	public ResponseEntity<?> trade(@RequestParam String from, @RequestParam String to,
			@RequestParam BigDecimal quantity, @RequestHeader("X-User-Email") String userEmail,
			@RequestHeader("X-User-Role") String userRole) {
		return service.trade(from, to, quantity, userEmail, userRole);
	}
}