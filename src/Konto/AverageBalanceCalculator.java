package Konto;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

public class AverageBalanceCalculator {
	
	private TransactionManager manager;
	public AverageBalanceCalculator(TransactionManager manager) {
		this.manager = manager;
	}
	
	public BigDecimal getAverageTobiasBalance(LocalDate start, LocalDate end) {
		return getAverageBalance(start, end, BalanceType.TOBIAS);
	}
	public BigDecimal getAverageBerndBalance(LocalDate start, LocalDate end) {
		return getAverageBalance(start, end, BalanceType.BERND);
	}
	public BigDecimal getAverageTotalBalance(LocalDate start, LocalDate end) {
		return getAverageBalance(start, end, BalanceType.TOTAL);
	}
	
	private BigDecimal getAverageBalance(LocalDate start, LocalDate end, BalanceType type) {
		
		if(start.isAfter(end)) {
			throw new IllegalArgumentException("start can't be after end!");
		}
		
		
		BigDecimal currentBalance = getBalanceAt(start.minusDays(1), type);
		List<Transaction> alle = manager.getAllTransactions();
		List<Transaction> relevant = new ArrayList<>();
		
		
		
		for(Transaction t : alle) {
			if(!t.getDate().isAfter(end) && !t.getDate().isBefore(start)) {
				relevant.add(t);
			}
		}
		relevant.sort(Comparator.comparing(Transaction::getDate));
		BigDecimal sumWeighted = BigDecimal.ZERO;
		LocalDate currentDate = start;
		
		
		for(Transaction t : relevant) {
			LocalDate txDate = t.getDate();
			BigDecimal days = BigDecimal.ZERO;
			LocalDate countDate = currentDate;
			
			while(countDate.isBefore(txDate)) {
				countDate = countDate.plusDays(1);
				days = days.add(BigDecimal.ONE);
			}
			
			 sumWeighted = sumWeighted.add(currentBalance.multiply(days));
			currentBalance = currentBalance.add(getAmountFromTransaction(t, type));
			currentDate = txDate;
		}
		
		
		BigDecimal remainingDays = BigDecimal.ZERO;
		LocalDate countDate = currentDate;
		
		while(!countDate.isAfter(end)) {
			remainingDays = remainingDays.add(BigDecimal.ONE);
			countDate = countDate.plusDays(1);
		}
		
		sumWeighted = sumWeighted.add(currentBalance.multiply(remainingDays));
		BigDecimal totalDays = BigDecimal.ZERO;
		countDate = start;
		
		while(!countDate.isAfter(end)) {
			countDate = countDate.plusDays(1);
			totalDays = totalDays.add(BigDecimal.ONE);
	}
		
		
		BigDecimal average = sumWeighted.divide(totalDays, 2, RoundingMode.HALF_UP);
		return average;
	}
		private BigDecimal getBalanceAt(LocalDate date, BalanceType type) {
		switch(type) {
			case TOBIAS:
				return manager.getTobiasBalanceAt(date);
			case BERND:
				return manager.getBerndBalanceAt(date);
			case TOTAL:
				return manager.getAccountBalanceAt(date);
			default:
				throw new IllegalArgumentException("invalid BalanceType!");
		}
	}
	private BigDecimal getAmountFromTransaction(Transaction t, BalanceType type) {
		switch(type) {
			case TOBIAS:
				return t.getTobiasSplit();
			case BERND:
				return t.getBerndSplit();
			case TOTAL:
				return t.getAccountEffekt();
			default:
				throw new IllegalArgumentException("invalid BalanceType!");
		}
	}
	
}
