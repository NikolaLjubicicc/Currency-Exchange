package tradeService;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import api.dtos.BankAccountDto;
import api.dtos.CryptoWalletDto;
import api.dtos.CurrencyExchangeDto;
import api.dtos.TradeDto;
import api.proxies.BankAccountProxy;
import api.proxies.CryptoWalletProxy;
import api.proxies.CurrencyExchangeProxy;
import api.services.TradeService;
import util.exceptions.InsufficientFundsException;

@Service
public class TradeServiceImpl implements TradeService {

	@Autowired
	private TradeExchangeRepository tradeExchangeRepository;

	@Autowired
	private BankAccountProxy bankAccountProxy;

	@Autowired
	private CryptoWalletProxy cryptoWalletProxy;

	@Autowired
	private CurrencyExchangeProxy currencyExchangeProxy;

	@Override
	public ResponseEntity<?> buyFiatToCrypto(@RequestParam String fiatCurrency, @RequestParam String cryptoCurrency,
			@RequestParam BigDecimal fiatAmount, @RequestHeader("X-User-Email") String userEmail, @RequestHeader("X-User-Role") String userRole) {

		if (!"USER".equals(userRole)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body("Only USER can access trade service");
		}

		BankAccountDto bankAccount = bankAccountProxy.getBankAccountByEmail(userEmail, userRole, userEmail).getBody();

		if (bankAccount == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Bank account not found for user: " + userEmail);
		}

		CryptoWalletDto cryptoWallet = cryptoWalletProxy.getCryptoWalletByEmail(userEmail, userRole, userEmail).getBody();

		if (cryptoWallet == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Crypto wallet not found for user: " + userEmail);
		}

		String targetFiatCurrency = fiatCurrency.toUpperCase();
		BigDecimal amountInTargetCurrency = fiatAmount;

		if (!targetFiatCurrency.equals("USD") && !targetFiatCurrency.equals("EUR")) {
			String intermediaryCurrency = "USD";
			CurrencyExchangeDto exchangeDto = currencyExchangeProxy
					.getExchangeFeign(targetFiatCurrency, intermediaryCurrency).getBody();

			if (exchangeDto == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("Exchange rate not found for: " + targetFiatCurrency + " to " + intermediaryCurrency);
			}

			amountInTargetCurrency = fiatAmount.multiply(exchangeDto.getExchangeRate());
			targetFiatCurrency = intermediaryCurrency;
		}

		BigDecimal currentBalance = bankAccount.getBalanceByCurrency(fiatCurrency);
		if (currentBalance.compareTo(fiatAmount) < 0) {
			throw new InsufficientFundsException(
					String.format("Insufficient %s balance. Available: %s, Required: %s", fiatCurrency,
							currentBalance, fiatAmount),
					"You don't have enough funds to complete this transaction");
		}

		Optional<TradeExchangeModel> tradeExchangeOpt = tradeExchangeRepository.findByFiatAndCrypto(targetFiatCurrency,
				cryptoCurrency.toUpperCase());

		if (tradeExchangeOpt.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Exchange rate not found for: " + targetFiatCurrency + " to " + cryptoCurrency);
		}

		TradeExchangeModel tradeExchange = tradeExchangeOpt.get();
		BigDecimal cryptoAmount = amountInTargetCurrency.multiply(tradeExchange.getExchangeRate());

		BigDecimal newFiatBalance = currentBalance.subtract(fiatAmount);
		switch (fiatCurrency.toUpperCase()) {
		case "EUR" -> bankAccount.setEurBalance(newFiatBalance);
		case "USD" -> bankAccount.setUsdBalance(newFiatBalance);
		case "GBP" -> bankAccount.setGbpBalance(newFiatBalance);
		case "CHF" -> bankAccount.setChfBalance(newFiatBalance);
		case "RSD" -> bankAccount.setRsdBalance(newFiatBalance);
		}

		BigDecimal newCryptoBalance = cryptoWallet.getBalanceByCrypto(cryptoCurrency).add(cryptoAmount);
		switch (cryptoCurrency.toUpperCase()) {
		case "BTC" -> cryptoWallet.setBtcBalance(newCryptoBalance);
		case "ETH" -> cryptoWallet.setEthBalance(newCryptoBalance);
		case "USDT" -> cryptoWallet.setUsdtBalance(newCryptoBalance);
		case "BNB" -> cryptoWallet.setBnbBalance(newCryptoBalance);
		case "ADA" -> cryptoWallet.setAdaBalance(newCryptoBalance);
		}

		BankAccountDto updatedBankAccount = bankAccountProxy.updateBankAccount(bankAccount, userRole, userEmail).getBody();
		CryptoWalletDto updatedCryptoWallet = cryptoWalletProxy.updateCryptoWallet(cryptoWallet, userRole, userEmail).getBody();

		String description = String.format("Uspešno kupljeno %s: %s za %s: %s", cryptoCurrency, cryptoAmount,
				fiatCurrency, fiatAmount);

		TradeDto response = new TradeDto(fiatCurrency, cryptoCurrency, fiatAmount, cryptoAmount,
				tradeExchange.getExchangeRate(), description, updatedBankAccount, updatedCryptoWallet);

		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<?> sellCryptoToFiat(@RequestParam String cryptoCurrency, @RequestParam String fiatCurrency,
			@RequestParam BigDecimal cryptoAmount, @RequestHeader("X-User-Email") String userEmail, @RequestHeader("X-User-Role") String userRole) {

		if (!"USER".equals(userRole)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body("Only USER can access trade service");
		}

		BankAccountDto bankAccount = bankAccountProxy.getBankAccountByEmail(userEmail, userRole, userEmail).getBody();

		if (bankAccount == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Bank account not found for user: " + userEmail);
		}

		CryptoWalletDto cryptoWallet = cryptoWalletProxy.getCryptoWalletByEmail(userEmail, userRole, userEmail).getBody();

		if (cryptoWallet == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Crypto wallet not found for user: " + userEmail);
		}

		BigDecimal currentCryptoBalance = cryptoWallet.getBalanceByCrypto(cryptoCurrency);
		if (currentCryptoBalance.compareTo(cryptoAmount) < 0) {
			throw new InsufficientFundsException(
					String.format("Insufficient %s balance. Available: %s, Required: %s", cryptoCurrency,
							currentCryptoBalance, cryptoAmount),
					"You don't have enough crypto to complete this transaction");
		}

		String targetFiatCurrency = fiatCurrency.toUpperCase();
		boolean needsSecondConversion = false;

		if (!targetFiatCurrency.equals("USD") && !targetFiatCurrency.equals("EUR")) {
			targetFiatCurrency = "USD";
			needsSecondConversion = true;
		}

		Optional<TradeExchangeModel> tradeExchangeOpt = tradeExchangeRepository
				.findByFiatAndCrypto(cryptoCurrency.toUpperCase(), targetFiatCurrency);

		if (tradeExchangeOpt.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Exchange rate not found for: " + cryptoCurrency + " to " + targetFiatCurrency);
		}

		TradeExchangeModel tradeExchange = tradeExchangeOpt.get();
		BigDecimal fiatAmountInTargetCurrency = cryptoAmount.multiply(tradeExchange.getExchangeRate());

		BigDecimal finalFiatAmount = fiatAmountInTargetCurrency;

		if (needsSecondConversion) {
			CurrencyExchangeDto exchangeDto = currencyExchangeProxy
					.getExchangeFeign(targetFiatCurrency, fiatCurrency.toUpperCase()).getBody();

			if (exchangeDto == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("Exchange rate not found for: " + targetFiatCurrency + " to " + fiatCurrency);
			}

			finalFiatAmount = fiatAmountInTargetCurrency.multiply(exchangeDto.getExchangeRate());
		}

		BigDecimal newCryptoBalance = currentCryptoBalance.subtract(cryptoAmount);
		switch (cryptoCurrency.toUpperCase()) {
		case "BTC" -> cryptoWallet.setBtcBalance(newCryptoBalance);
		case "ETH" -> cryptoWallet.setEthBalance(newCryptoBalance);
		case "USDT" -> cryptoWallet.setUsdtBalance(newCryptoBalance);
		case "BNB" -> cryptoWallet.setBnbBalance(newCryptoBalance);
		case "ADA" -> cryptoWallet.setAdaBalance(newCryptoBalance);
		}

		BigDecimal currentFiatBalance = bankAccount.getBalanceByCurrency(fiatCurrency);
		BigDecimal newFiatBalance = currentFiatBalance.add(finalFiatAmount);
		switch (fiatCurrency.toUpperCase()) {
		case "EUR" -> bankAccount.setEurBalance(newFiatBalance);
		case "USD" -> bankAccount.setUsdBalance(newFiatBalance);
		case "GBP" -> bankAccount.setGbpBalance(newFiatBalance);
		case "CHF" -> bankAccount.setChfBalance(newFiatBalance);
		case "RSD" -> bankAccount.setRsdBalance(newFiatBalance);
		}

		BankAccountDto updatedBankAccount = bankAccountProxy.updateBankAccount(bankAccount, userRole, userEmail).getBody();
		CryptoWalletDto updatedCryptoWallet = cryptoWalletProxy.updateCryptoWallet(cryptoWallet, userRole, userEmail).getBody();

		String description = String.format("Uspešno prodato %s: %s za %s: %s", cryptoCurrency, cryptoAmount,
				fiatCurrency, finalFiatAmount);

		TradeDto response = new TradeDto(fiatCurrency, cryptoCurrency, finalFiatAmount, cryptoAmount,
				tradeExchange.getExchangeRate(), description, updatedBankAccount, updatedCryptoWallet);

		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<?> trade(@RequestParam String from, @RequestParam String to,
			@RequestParam BigDecimal quantity, @RequestHeader("X-User-Email") String userEmail, @RequestHeader("X-User-Role") String userRole) {

		if (!"USER".equals(userRole)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body("Only USER can access trade service");
		}

		boolean fromIsCrypto = isCryptoCurrency(from);
		boolean toIsCrypto = isCryptoCurrency(to);

		if (fromIsCrypto && toIsCrypto) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Cannot trade between two crypto currencies. Use crypto-conversion service instead.");
		}

		if (!fromIsCrypto && !toIsCrypto) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Cannot trade between two fiat currencies. Use currency-conversion service instead.");
		}

		if (fromIsCrypto && !toIsCrypto) {
			return sellCryptoToFiat(from, to, quantity, userEmail, userRole);
		}

		if (!fromIsCrypto && toIsCrypto) {
			return buyFiatToCrypto(from, to, quantity, userEmail, userRole);
		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid currency combination");
	}

	private boolean isCryptoCurrency(String currency) {
		String upperCurrency = currency.toUpperCase();
		return upperCurrency.equals("BTC") || upperCurrency.equals("ETH") || upperCurrency.equals("USDT")
				|| upperCurrency.equals("BNB") || upperCurrency.equals("ADA");
	}
}