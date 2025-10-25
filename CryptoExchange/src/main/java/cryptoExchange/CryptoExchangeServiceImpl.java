package cryptoExchange;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import api.dtos.CryptoExchangeDto;
import api.services.CryptoExchangeService;

@Service
public class CryptoExchangeServiceImpl implements CryptoExchangeService {

	@Autowired
	private CryptoExchangeRepository repo;

	@Override
	public ResponseEntity<?> getCryptoExchangeRate(@RequestParam String from, @RequestParam String to) {

		CryptoExchangeModel model = repo.findByCryptoFromAndCryptoTo(from.toUpperCase(), to.toUpperCase());

		if (model == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Exchange rate not found for: " + from + " to " + to);
		}

		return ResponseEntity.ok(convertModelToDto(model));
	}

	private CryptoExchangeDto convertModelToDto(CryptoExchangeModel model) {
		return new CryptoExchangeDto(model.getId(), model.getCryptoFrom(), model.getCryptoTo(),
				model.getExchangeRate());
	}
}