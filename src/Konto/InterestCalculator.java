package Konto;

		import java.math.BigDecimal;
		import java.math.RoundingMode;
		import java.time.LocalDate;

		public class InterestCalculator {
			private AverageBalanceCalculator calculator;
			
			public InterestCalculator(AverageBalanceCalculator calculator) {
				this.calculator = calculator;
			}
			public Transaction createInterestTransaction(LocalDate start, LocalDate end, LocalDate bookingDate, String description, BigDecimal grossInterest, BigDecimal taxBernd) {
				
				if(start.isAfter(end)) {
					throw new IllegalArgumentException("end date can't be After start date!");
				}
				
				if(grossInterest.compareTo(BigDecimal.ZERO) < 0 ) {
					throw new IllegalArgumentException("interest can't be negativ!");
				}
				
				if(taxBernd.compareTo(BigDecimal.ZERO) < 0) {
					throw new IllegalArgumentException("Tax can't be negativ!");
				}
				
					
				BigDecimal averageTobias = calculator.getAverageTobiasBalance(start, end);
				
				BigDecimal averageBernd = calculator.getAverageBerndBalance(start, end);
							
				BigDecimal totalAverage = averageTobias.add(averageBernd);
				
				
				
				
				
				BigDecimal tobiasGrossInterest  = grossInterest.multiply(averageTobias).divide(totalAverage, 2, RoundingMode.HALF_UP);
				
				BigDecimal berndGrossInterest = grossInterest.subtract(tobiasGrossInterest);
				
				BigDecimal tobiasNetInterest = tobiasGrossInterest;
				
				BigDecimal berndNetInterest = berndGrossInterest.subtract(taxBernd);
				
				Split split = new Split(tobiasNetInterest, berndNetInterest);
				
				BigDecimal accountEffect = grossInterest.subtract(taxBernd);
				
				Transaction transaction = new Transaction(bookingDate, description, accountEffect, grossInterest, taxBernd, split);
				
				return transaction;
			}
				
		
	
		
		}


