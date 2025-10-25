package bankAccount;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "bank_account")
public class BankAccountModel implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String userEmail;

	@Column(nullable = false)
	private BigDecimal eurBalance;

	@Column(nullable = false)
	private BigDecimal usdBalance;

	@Column(nullable = false)
	private BigDecimal gbpBalance;

	@Column(nullable = false)
	private BigDecimal chfBalance;

	@Column(nullable = false)
	private BigDecimal rsdBalance;

	public BankAccountModel() {
	}

	public BankAccountModel(String userEmail, BigDecimal eurBalance, BigDecimal usdBalance, BigDecimal gbpBalance,
			BigDecimal chfBalance, BigDecimal rsdBalance) {
		super();
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

	public void setBalanceByCurrency(String currency, BigDecimal amount) {
		switch (currency.toUpperCase()) {
		case "EUR":
			this.eurBalance = amount;
			break;
		case "USD":
			this.usdBalance = amount;
			break;
		case "GBP":
			this.gbpBalance = amount;
			break;
		case "CHF":
			this.chfBalance = amount;
			break;
		case "RSD":
			this.rsdBalance = amount;
			break;
		}
	}
}