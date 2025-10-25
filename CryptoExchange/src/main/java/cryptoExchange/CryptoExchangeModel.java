package cryptoExchange;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "crypto_exchange")
public class CryptoExchangeModel implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "crypto_from", nullable = false)
	private String cryptoFrom;

	@Column(name = "crypto_to", nullable = false)
	private String cryptoTo;

	@Column(name = "exchange_rate", nullable = false)
	private BigDecimal exchangeRate;

	public CryptoExchangeModel() {
	}

	public CryptoExchangeModel(String cryptoFrom, String cryptoTo, BigDecimal exchangeRate) {
		super();
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