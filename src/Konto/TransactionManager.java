package Konto;
import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

public class TransactionManager {
	private List<Transaction> transactions;
	
	public TransactionManager() {
		this.transactions = new ArrayList<>();
	}
	
	
	public void addTransaction(Transaction t){
		if (t == null) {
	        throw new IllegalArgumentException("Transaction darf nicht null sein.");
	    }
		transactions.add(t);
	}
	public void removeLastTransaction() {
		if(!transactions.isEmpty()) {
		transactions.remove(transactions.size()-1);
		}
	}
	
	public List<Transaction> getAllTransactions() {
		return new ArrayList<>(transactions);
	}
	public BigDecimal getCurrentAccountBalance(){
		BigDecimal sum = BigDecimal.ZERO;
		for(Transaction t : transactions) {
			sum = sum.add(t.getAccountEffekt());
			}
		return sum;
	}
	public BigDecimal getCurrentTobiasBalance() {
		BigDecimal sum = BigDecimal.ZERO;
		for(Transaction t : transactions) {
			sum = sum.add(t.getTobiasSplit());
		}
		return sum;
	}
	public BigDecimal getCurrentBerndBalance() {
		BigDecimal sum = BigDecimal.ZERO;
		for(Transaction t : transactions) {
			sum = sum.add(t.getBerndSplit());
		}
		
		return sum;
	}
	public List<Transaction> getTransactionsBetweenInclusive(LocalDate start, LocalDate end){
		List<Transaction> timedTransactions = new ArrayList<>();
		for(Transaction t : transactions) {
			LocalDate d = t.getDate();
			if(!d.isBefore(start) && !d.isAfter(end)) {
				timedTransactions.add(t);
			}
		}	
		return timedTransactions;
	}
	public BigDecimal getAccountBalanceAt(LocalDate date) {
		BigDecimal sum = BigDecimal.ZERO;
		for(Transaction t : transactions) {
			if(!t.getDate().isAfter(date)) {
			sum = sum.add(t.getAccountEffekt());
			}
		}
		return sum;
	}
	
	public BigDecimal getTobiasBalanceAt(LocalDate date) {
		BigDecimal sum = BigDecimal.ZERO;
		for(Transaction t : transactions) {
			if(!t.getDate().isAfter(date)) {
				sum = sum.add(t.getTobiasSplit());
			}
		}
		return sum;
	}
		
	public BigDecimal getBerndBalanceAt(LocalDate date) {
		BigDecimal sum = BigDecimal.ZERO;
		for(Transaction t : transactions) {
			if(!t.getDate().isAfter(date)) {
				sum = sum.add(t.getBerndSplit());
			}
		}
		return sum;
	}
	public void removeTransaction(Transaction t) {
		if(!transactions.isEmpty()) {
			for(int i = 0; i < transactions.size();i++) {
				if(transactions.get(i).equals(t)) {
					transactions.remove(i);
					break;
				}
			}
		}
	}
	public void removeTransactionAt(int index) {
	    if (index < 0 || index >= transactions.size()) {
	        throw new IndexOutOfBoundsException("Ungültiger Index: " + index);
	    }
	    transactions.remove(index);
	}
	public void replaceTransactionAt(int index, Transaction transaction) {
	    if (transaction == null) {
	        throw new IllegalArgumentException("Transaction darf nicht null sein.");
	    }
	    if (index < 0 || index >= transactions.size()) {
	        throw new IndexOutOfBoundsException("Ungültiger Index: " + index);
	    }
	    transactions.set(index, transaction);
	}
}

