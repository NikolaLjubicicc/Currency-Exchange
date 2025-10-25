package api.dtos;

import java.math.BigDecimal;

public class CryptoWalletDto {

	private Long id;
	private String userEmail;
	private BigDecimal btcBalance;
	private BigDecimal ethBalance;
	private BigDecimal usdtBalance;
	private BigDecimal bnbBalance;
	private BigDecimal adaBalance;

	public CryptoWalletDto() {
	}

	public CryptoWalletDto(Long id, String userEmail, BigDecimal btcBalance, BigDecimal ethBalance,
			BigDecimal usdtBalance, BigDecimal bnbBalance, BigDecimal adaBalance) {
		super();
		this.id = id;
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
}