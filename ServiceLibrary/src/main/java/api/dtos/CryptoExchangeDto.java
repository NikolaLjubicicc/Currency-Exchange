package api.dtos;

import java.math.BigDecimal;

public class CryptoExchangeDto {

	private Long id;
	private String cryptoFrom;
	private String cryptoTo;
	private BigDecimal exchangeRate;

	public CryptoExchangeDto() {
	}

	public CryptoExchangeDto(Long id, String cryptoFrom, String cryptoTo, BigDecimal exchangeRate) {
		super();
		this.id = id;
		this.cryptoFrom = cryptoFrom;
		this.cryptoTo = cryptoTo;
		this.exchangeRate = exchangeRate;
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

	public BigDecimal getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(BigDecimal exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
}