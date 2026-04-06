package Konto;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class Transaction {
	private final LocalDate date;
	private final TransactionType type;
	private final String description;
	private final BigDecimal accountEffect;
	private final BigDecimal grossInterestRate;
	private final BigDecimal taxBernd;
	private final Split split;
	private final String enteredBy;
	
	//normal Transaction
	
	public Transaction(LocalDate date, TransactionType type, String description, BigDecimal accountEffect, Split split, String enteredBy) {
		this (date, type, description, accountEffect, BigDecimal.ZERO, BigDecimal.ZERO, split, enteredBy);
	}
	//Interest Transaction
	public Transaction(LocalDate date, String description,BigDecimal accountEffect, BigDecimal grossInterestRate, BigDecimal TaxBernd,  Split split, String enteredBy) {
		this(date, TransactionType.Interest, description, grossInterestRate.subtract(TaxBernd), grossInterestRate, TaxBernd, split, enteredBy);
	}
	//main Transaction
	public Transaction(LocalDate date, TransactionType type, String description, BigDecimal accountEffect, BigDecimal grossInterestRate, BigDecimal TaxBernd, Split split, String enteredBy) {
		this.date = Objects.requireNonNull(date);
		this.type = Objects.requireNonNull(type);
		this.description = Objects.requireNonNull(description);
		this.accountEffect = requireNonNullMoney(accountEffect);
		this.grossInterestRate = requireNonNullMoney(grossInterestRate);
		this.taxBernd = requireNonNullMoney(TaxBernd);
		this.split = Objects.requireNonNull(split);
		this.enteredBy = enteredBy;
		validate();
	}
	private void validate() {
		
		
		if(split.sum().compareTo(accountEffect) != 0) {
			throw new IllegalArgumentException("split sum needs to be equal to accountEffect!");
		}
		if(type != TransactionType.Interest) {
			if(grossInterestRate.compareTo(BigDecimal.ZERO) != 0 || taxBernd.compareTo(BigDecimal.ZERO) != 0) {
				throw new IllegalArgumentException("If its an interest Transaction, TaxBernd and grossinterestRate should be Zero!");	
			}
		}
		if(type == TransactionType.Interest) {
			if(grossInterestRate.compareTo(BigDecimal.ZERO) < 0 || taxBernd.compareTo(BigDecimal.ZERO)<0) {
				throw new IllegalArgumentException("Interests can't be negativ!");
			}
			if(accountEffect.compareTo(grossInterestRate.subtract(taxBernd)) != 0) {
				throw new IllegalArgumentException("AccountEffect - grossInterestRate + TaxBernd should be Zero!");
			}
		}
	}
	public LocalDate getDate() {
		return date;
	}
	public TransactionType getType() {
		return type;
	}
	public String getDescription() {
		return description;
	}
	public BigDecimal getAccountEffekt() {
		return accountEffect;
	}
	public BigDecimal getGrossInterestRate() {
		return grossInterestRate;
	}
	public BigDecimal getTaxBernd() {
		return taxBernd;
	}
	public BigDecimal getTobiasSplit() {
		return split.getTobias();
	}
	public BigDecimal getBerndSplit() {
		return split.getBernd();
	}
	public String getEnteredBy() {
	    return enteredBy;
	}
	
	 private BigDecimal requireNonNullMoney(BigDecimal value) {
	        return Objects.requireNonNull(value);
	    }
	 @Override
	 public String toString() {
	     return String.format(
	         "%s | %-10s | Konto: %8s | T: %8s | B: %8s | %s",
	         date,
	         type,
	         accountEffect,
	         split.getTobias(),
	         split.getBernd(),
	         description
	     );
	 }
}



