package api.dtos;

import java.math.BigDecimal;

public class TradeDto {

	private String fiatCurrency;
	private String cryptoCurrency;
	private BigDecimal fiatAmount;
	private BigDecimal cryptoAmount;
	private BigDecimal exchangeRate;
	private String transactionDescription;
	private BankAccountDto bankAccountAfterTransaction;
	private CryptoWalletDto cryptoWalletAfterTransaction;

	public TradeDto() {
	}

	public TradeDto(String fiatCurrency, String cryptoCurrency, BigDecimal fiatAmount, BigDecimal cryptoAmount,
			BigDecimal exchangeRate, String transactionDescription, BankAccountDto bankAccountAfterTransaction,
			CryptoWalletDto cryptoWalletAfterTransaction) {
		super();
		this.fiatCurrency = fiatCurrency;
		this.cryptoCurrency = cryptoCurrency;
		this.fiatAmount = fiatAmount;
		this.cryptoAmount = cryptoAmount;
		this.exchangeRate = exchangeRate;
		this.transactionDescription = transactionDescription;
		this.bankAccountAfterTransaction = bankAccountAfterTransaction;
		this.cryptoWalletAfterTransaction = cryptoWalletAfterTransaction;
	}

	public String getFiatCurrency() {
		return fiatCurrency;
	}

	public void setFiatCurrency(String fiatCurrency) {
		this.fiatCurrency = fiatCurrency;
	}

	public String getCryptoCurrency() {
		return cryptoCurrency;
	}

	public void setCryptoCurrency(String cryptoCurrency) {
		this.cryptoCurrency = cryptoCurrency;
	}

	public BigDecimal getFiatAmount() {
		return fiatAmount;
	}

	public void setFiatAmount(BigDecimal fiatAmount) {
		this.fiatAmount = fiatAmount;
	}

	public BigDecimal getCryptoAmount() {
		return cryptoAmount;
	}

	public void setCryptoAmount(BigDecimal cryptoAmount) {
		this.cryptoAmount = cryptoAmount;
	}

	public BigDecimal getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(BigDecimal exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public String getTransactionDescription() {
		return transactionDescription;
	}

	public void setTransactionDescription(String transactionDescription) {
		this.transactionDescription = transactionDescription;
	}

	public BankAccountDto getBankAccountAfterTransaction() {
		return bankAccountAfterTransaction;
	}

	public void setBankAccountAfterTransaction(BankAccountDto bankAccountAfterTransaction) {
		this.bankAccountAfterTransaction = bankAccountAfterTransaction;
	}

	public CryptoWalletDto getCryptoWalletAfterTransaction() {
		return cryptoWalletAfterTransaction;
	}

	public void setCryptoWalletAfterTransaction(CryptoWalletDto cryptoWalletAfterTransaction) {
		this.cryptoWalletAfterTransaction = cryptoWalletAfterTransaction;
	}
}