package currencyConversion;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import api.dtos.BankAccountDto;
import api.dtos.CurrencyConversionDto;
import api.dtos.CurrencyExchangeDto;
import api.proxies.BankAccountProxy;
import api.proxies.CurrencyExchangeProxy;
import api.services.CurrencyConversionService;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import util.exceptions.InsufficientFundsException;
import util.exceptions.InvalidQuantityException;

@RestController
public class CurrencyConversionServiceImpl implements CurrencyConversionService {

	@Autowired
	private CurrencyExchangeProxy proxy;

	@Autowired
	private BankAccountProxy bankAccountProxy;

	Retry retry;
	CurrencyExchangeDto response;

	public CurrencyConversionServiceImpl(RetryRegistry registry) {
		retry = registry.retry("default");
	}

	
	@Override
	@CircuitBreaker(name = "cb", fallbackMethod = "fallback")
	public ResponseEntity<?> getConversionFeign(String from, String to, BigDecimal quantity, String userEmail, String userRole) {
		if(quantity.compareTo(BigDecimal.valueOf(300)) == 1) {
			throw new InvalidQuantityException(String.format("Quantity of %s is too large", quantity));
		}

		BankAccountDto bankAccount = bankAccountProxy.getBankAccountByEmail(userEmail, userRole, userEmail).getBody();

		if(bankAccount == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Bank account not found for user: " + userEmail);
		}

		BigDecimal currentBalance = bankAccount.getBalanceByCurrency(from);
		if(currentBalance.compareTo(quantity) < 0) {
			throw new InsufficientFundsException(
					String.format("Insufficient %s balance. Available: %s, Required: %s", from, currentBalance, quantity));
		}

		retry.executeSupplier(() -> response = proxy.getExchangeFeign(from, to).getBody());

		CurrencyConversionDto finalResponse = new CurrencyConversionDto(response, quantity);
		finalResponse.setFeign(true);

		BigDecimal convertedAmount = finalResponse.getConversionResult().getConvertedAmount();

		BigDecimal newFromBalance = currentBalance.subtract(quantity);
		BigDecimal newToBalance = bankAccount.getBalanceByCurrency(to).add(convertedAmount);

		switch (from.toUpperCase()) {
		case "EUR" -> bankAccount.setEurBalance(newFromBalance);
		case "USD" -> bankAccount.setUsdBalance(newFromBalance);
		case "GBP" -> bankAccount.setGbpBalance(newFromBalance);
		case "CHF" -> bankAccount.setChfBalance(newFromBalance);
		case "RSD" -> bankAccount.setRsdBalance(newFromBalance);
		}

		switch (to.toUpperCase()) {
		case "EUR" -> bankAccount.setEurBalance(newToBalance);
		case "USD" -> bankAccount.setUsdBalance(newToBalance);
		case "GBP" -> bankAccount.setGbpBalance(newToBalance);
		case "CHF" -> bankAccount.setChfBalance(newToBalance);
		case "RSD" -> bankAccount.setRsdBalance(newToBalance);
		}

		BankAccountDto updatedAccount = bankAccountProxy.updateBankAccount(bankAccount, userRole, userEmail).getBody();

		String description = String.format("Uspešno izvršena razmena %s: %s za %s: %s",
				from, quantity, to, convertedAmount);
		finalResponse.setTransactionDescription(description);
		finalResponse.setBankAccountAfterTransaction(updatedAccount);

		return ResponseEntity.ok(finalResponse);
	}

	public ResponseEntity<?> fallback(CallNotPermittedException ex){
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body("Currecny conversion service is currently unavailable, Circuit breaker is in OPEN state!");
	}

}
