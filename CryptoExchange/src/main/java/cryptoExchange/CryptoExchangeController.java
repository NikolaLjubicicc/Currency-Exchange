package cryptoExchange;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import api.services.CryptoExchangeService;

@RestController
public class CryptoExchangeController {

	@Autowired
	private CryptoExchangeService service;

	@GetMapping("/crypto-exchange")
	public ResponseEntity<?> getCryptoExchangeRate(@RequestParam String from, @RequestParam String to) {
		return service.getCryptoExchangeRate(from, to);
	}
}