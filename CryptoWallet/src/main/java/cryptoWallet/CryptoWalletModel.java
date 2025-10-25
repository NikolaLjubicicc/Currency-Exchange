package cryptoWallet;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "crypto_wallet")
public class CryptoWalletModel implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String userEmail;

	@Column(nullable = false)
	private BigDecimal btcBalance;

	@Column(nullable = false)
	private BigDecimal ethBalance;

	@Column(nullable = false)
	private BigDecimal usdtBalance;

	@Column(nullable = false)
	private BigDecimal bnbBalance;

	@Column(nullable = false)
	private BigDecimal adaBalance;

	public CryptoWalletModel() {
	}

	public CryptoWalletModel(String userEmail, BigDecimal btcBalance, BigDecimal ethBalance, BigDecimal usdtBalance,
			BigDecimal bnbBalance, BigDecimal adaBalance) {
		super();
		this.userEmail = userEmail;
		this.btcBalance = btcBalance;
		this.ethBalance = ethBalance;
		this.usdtBalance = usdtBalance;
		this.bnbBalance = bnbBalance;
		this.adaBalance = adaBalance;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public BigDecimal getBtcBalance() {
		return btcBalance;
	}

	public void setBtcBalance(BigDecimal btcBalance) {
		this.btcBalance = btcBalance;
	}

	public BigDecimal getEthBalance() {
		return ethBalance;
	}

	public void setEthBalance(BigDecimal ethBalance) {
		this.ethBalance = ethBalance;
	}

	public BigDecimal getUsdtBalance() {
		return usdtBalance;
	}

	public void setUsdtBalance(BigDecimal usdtBalance) {
		this.usdtBalance = usdtBalance;
	}

	public BigDecimal getBnbBalance() {
		return bnbBalance;
	}

	public void setBnbBalance(BigDecimal bnbBalance) {
		this.bnbBalance = bnbBalance;
	}

	public BigDecimal getAdaBalance() {
		return adaBalance;
	}

	public void setAdaBalance(BigDecimal adaBalance) {
		this.adaBalance = adaBalance;
	}

	public BigDecimal getBalanceByCrypto(String crypto) {
		return switch (crypto.toUpperCase()) {
		case "BTC" -> btcBalance;
		case "ETH" -> ethBalance;
		case "USDT" -> usdtBalance;
		case "BNB" -> bnbBalance;
		case "ADA" -> adaBalance;
		default -> BigDecimal.ZERO;
		};
	}

	public void setBalanceByCrypto(String crypto, BigDecimal amount) {
		switch (crypto.toUpperCase()) {
		case "BTC":
			this.btcBalance = amount;
			break;
		case "ETH":
			this.ethBalance = amount;
			break;
		case "USDT":
			this.usdtBalance = amount;
			break;
		case "BNB":
			this.bnbBalance = amount;
			break;
		case "ADA":
			this.adaBalance = amount;
			break;
		}
	}
}