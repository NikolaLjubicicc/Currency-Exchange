package api.dtos;

import java.math.BigDecimal;

public class CryptoConversionDto {

	private Long id;
	private String cryptoFrom;
	private String cryptoTo;
	private BigDecimal quantity;
	private BigDecimal exchangeRate;
	private BigDecimal convertedAmount;
	private String transactionDescription;
	private CryptoWalletDto cryptoWalletAfterTransaction;

	public CryptoConversionDto() {
	}

	public CryptoConversionDto(Long id, String cryptoFrom, String cryptoTo, BigDecimal quantity,
			BigDecimal exchangeRate, BigDecimal convertedAmount, String transactionDescription,
			CryptoWalletDto cryptoWalletAfterTransaction) {
		super();
		this.id = id;
		this.cryptoFrom = cryptoFrom;
		this.cryptoTo = cryptoTo;
		this.quantity = quantity;
		this.exchangeRate = exchangeRate;
		this.convertedAmount = convertedAmount;
		this.transactionDescription = transactionDescription;
		this.cryptoWalletAfterTransaction = cryptoWalletAfterTransaction;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCryptoFrom() {
		return cryptoFrom;
	}

	public void setCryptoFrom(String cryptoFrom) {
		this.cryptoFrom = cryptoFrom;
	}

	public String getCryptoTo() {
		return cryptoTo;
	}

	public void setCryptoTo(String cryptoTo) {
		this.cryptoTo = cryptoTo;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(BigDecimal exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public BigDecimal getConvertedAmount() {
		return convertedAmount;
	}

	public void setConvertedAmount(BigDecimal convertedAmount) {
		this.convertedAmount = convertedAmount;
	}

	public String getTransactionDescription() {
		return transactionDescription;
	}

	public void setTransactionDescription(String transactionDescription) {
		this.transactionDescription = transactionDescription;
	}

	public CryptoWalletDto getCryptoWalletAfterTransaction() {
		return cryptoWalletAfterTransaction;
	}

	public void setCryptoWalletAfterTransaction(CryptoWalletDto cryptoWalletAfterTransaction) {
		this.cryptoWalletAfterTransaction = cryptoWalletAfterTransaction;
	}
}