package api.dtos;

import java.math.BigDecimal;

public class BankAccountDto {

	private Long id;
	private String userEmail;
	private BigDecimal eurBalance;
	private BigDecimal usdBalance;
	private BigDecimal gbpBalance;
	private BigDecimal chfBalance;
	private BigDecimal rsdBalance;

	public BankAccountDto() {
	}

	public BankAccountDto(Long id, String userEmail, BigDecimal eurBalance, BigDecimal usdBalance,
			BigDecimal gbpBalance, BigDecimal chfBalance, BigDecimal rsdBalance) {
		super();
		this.id = id;
		this.userEmail = userEmail;
		this.eurBalance = eurBalance;
		this.usdBalance = usdBalance;
		this.gbpBalance = gbpBalance;
		this.chfBalance = chfBalance;
		this.rsdBalance = rsdBalance;
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

	public BigDecimal getEurBalance() {
		return eurBalance;
	}

	public void setEurBalance(BigDecimal eurBalance) {
		this.eurBalance = eurBalance;
	}

	public BigDecimal getUsdBalance() {
		return usdBalance;
	}

	public void setUsdBalance(BigDecimal usdBalance) {
		this.usdBalance = usdBalance;
	}

	public BigDecimal getGbpBalance() {
		return gbpBalance;
	}

	public void setGbpBalance(BigDecimal gbpBalance) {
		this.gbpBalance = gbpBalance;
	}

	public BigDecimal getChfBalance() {
		return chfBalance;
	}

	public void setChfBalance(BigDecimal chfBalance) {
		this.chfBalance = chfBalance;
	}

	public BigDecimal getRsdBalance() {
		return rsdBalance;
	}

	public void setRsdBalance(BigDecimal rsdBalance) {
		this.rsdBalance = rsdBalance;
	}

	public BigDecimal getBalanceByCurrency(String currency) {
		return switch (currency.toUpperCase()) {
		case "EUR" -> eurBalance;
		case "USD" -> usdBalance;
		case "GBP" -> gbpBalance;
		case "CHF" -> chfBalance;
		case "RSD" -> rsdBalance;
		default -> BigDecimal.ZERO;
		};
	}
}