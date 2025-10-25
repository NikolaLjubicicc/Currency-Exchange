package tradeService;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "trade_exchange")
public class TradeExchangeModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "fiat_currency")
	private String fiatCurrency;

	@Column(name = "crypto_currency")
	private String cryptoCurrency;

	@Column(name = "exchange_rate")
	private BigDecimal exchangeRate;

	public TradeExchangeModel() {
	}

	public TradeExchangeModel(Long id, String fiatCurrency, String cryptoCurrency, BigDecimal exchangeRate) {
		super();
		this.id = id;
		this.fiatCurrency = fiatCurrency;
		this.cryptoCurrency = cryptoCurrency;
		this.exchangeRate = exchangeRate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public BigDecimal getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(BigDecimal exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
}