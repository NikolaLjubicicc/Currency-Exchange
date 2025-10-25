package cryptoConversion;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import api.dtos.CryptoConversionDto;
import api.dtos.CryptoExchangeDto;
import api.dtos.CryptoWalletDto;
import api.proxies.CryptoExchangeProxy;
import api.proxies.CryptoWalletProxy;
import api.services.CryptoConversionService;
import util.exceptions.InsufficientFundsException;

@Service
public class CryptoConversionServiceImpl implements CryptoConversionService {

	@Autowired
	private CryptoExchangeProxy cryptoExchangeProxy;

	@Autowired
	private CryptoWalletProxy cryptoWalletProxy;

	@Override
	public ResponseEntity<?> convertCrypto(@RequestParam String from, @RequestParam String to,
			@RequestParam BigDecimal quantity, @RequestHeader("X-User-Email") String userEmail,
			@RequestHeader("X-User-Role") String userRole) {

		CryptoExchangeDto exchangeDto = cryptoExchangeProxy.getCryptoExchangeRate(from, to).getBody();

		if (exchangeDto == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Exchange rate not found for: " + from + " to " + to);
		}

		BigDecimal exchangeRate = exchangeDto.getExchangeRate();
		BigDecimal convertedAmount = quantity.multiply(exchangeRate);

		CryptoWalletDto cryptoWallet = cryptoWalletProxy.getCryptoWalletByEmail(userEmail, userRole, userEmail).getBody();

		if (cryptoWallet == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Crypto wallet not found for user: " + userEmail);
		}

		BigDecimal currentBalance = cryptoWallet.getBalanceByCrypto(from);
		if (currentBalance.compareTo(quantity) < 0) {
			throw new InsufficientFundsException(
					String.format("Insufficient %s balance. Available: %s, Required: %s", from, currentBalance,
							quantity),
					"You don't have enough crypto to complete this transaction");
		}

		BigDecimal newFromBalance = currentBalance.subtract(quantity);
		BigDecimal newToBalance = cryptoWallet.getBalanceByCrypto(to).add(convertedAmount);

		switch (from.toUpperCase()) {
		case "BTC":
			cryptoWallet.setBtcBalance(newFromBalance);
			break;
		case "ETH":
			cryptoWallet.setEthBalance(newFromBalance);
			break;
		case "USDT":
			cryptoWallet.setUsdtBalance(newFromBalance);
			break;
		case "BNB":
			cryptoWallet.setBnbBalance(newFromBalance);
			break;
		case "ADA":
			cryptoWallet.setAdaBalance(newFromBalance);
			break;
		}

		switch (to.toUpperCase()) {
		case "BTC":
			cryptoWallet.setBtcBalance(newToBalance);
			break;
		case "ETH":
			cryptoWallet.setEthBalance(newToBalance);
			break;
		case "USDT":
			cryptoWallet.setUsdtBalance(newToBalance);
			break;
		case "BNB":
			cryptoWallet.setBnbBalance(newToBalance);
			break;
		case "ADA":
			cryptoWallet.setAdaBalance(newToBalance);
			break;
		}

		CryptoWalletDto updatedWallet = cryptoWalletProxy.updateCryptoWallet(cryptoWallet, userRole, userEmail).getBody();

		String description = String.format("Uspešno je izvršena razmena %s: %s za %s: %s", from, quantity, to,
				convertedAmount);

		CryptoConversionDto response = new CryptoConversionDto(null, from, to, quantity, exchangeRate,
				convertedAmount, description, updatedWallet);

		return ResponseEntity.ok(response);
	}
}