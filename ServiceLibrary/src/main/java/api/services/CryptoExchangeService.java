package api.services;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

public interface CryptoExchangeService {

	ResponseEntity<?> getCryptoExchangeRate(@RequestParam String from, @RequestParam String to);
}