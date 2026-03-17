package Konto;
import java.math.BigDecimal;
import java.time.LocalDate;

public class Konto_main {


	

	    public static void main(String[] args) {
	    	
	   
	    	/*	Split s = new Split(bd("310.00"), bd("0.00"));
	    	System.out.println("Split direkt:");
	    	System.out.println("Tobias = " + s.getTobias());
	    	System.out.println("Bernd  = " + s.getBernd());

	    	Transaction tx = normalTx(
	    	        LocalDate.of(2026, 1, 10),
	    	        "Test Tobias Einzahlung",
	    	        bd("310.00"),
	    	        bd("310.00"),
	    	        bd("0.00")
	    	);

	    	System.out.println();
	    	System.out.println("Transaction direkt:");
	    	System.out.println("Tobias = " + tx.getTobiasSplit());
	    	System.out.println("Bernd  = " + tx.getBerndSplit());

	    	TransactionManager testManager = new TransactionManager();
	    	testManager.addTransaction(tx);

	    	System.out.println();
	    	System.out.println("Im Manager:");
	    	System.out.println("Tobias = " + testManager.getCurrentTobiasBalance());
	    	System.out.println("Bernd  = " + testManager.getCurrentBerndBalance());
	       TransactionManager manager = new TransactionManager();
	        AverageBalanceCalculator averageCalculator = new AverageBalanceCalculator(manager);
	        InterestCalculator interestCalculator = new InterestCalculator(averageCalculator);

	        System.out.println("=== TEST START ===");

	        // --------------------------------------------------
	        // 1) Anfangsbestand zum 31.12.2025
	        // --------------------------------------------------
	        manager.addTransaction(normalTx(
	                LocalDate.of(2025, 12, 31),
	                "Startsaldo Tobias",
	                bd("1000.00"),
	                bd("1000.00"),
	                bd("0.00")
	        ));

	        manager.addTransaction(normalTx(
	                LocalDate.of(2025, 12, 31),
	                "Startsaldo Bernd",
	                bd("1000.00"),
	                bd("0.00"),
	                bd("1000.00")
	        ));

	        check("Start Gesamtkonto", manager.getCurrentAccountBalance(), "2000.00");
	        check("Start Tobias", manager.getCurrentTobiasBalance(), "1000.00");
	        check("Start Bernd", manager.getCurrentBerndBalance(), "1000.00");

	        // --------------------------------------------------
	        // 2) Normale Buchungen im Januar 2026
	        // --------------------------------------------------
	        manager.addTransaction(normalTx(
	                LocalDate.of(2026, 1, 10),
	                "Einzahlung Tobias Januar",
	                bd("310.00"),
	                bd("310.00"),
	                bd("0.00")
	        ));

	        manager.addTransaction(normalTx(
	                LocalDate.of(2026, 1, 20),
	                "Einzahlung Bernd Januar",
	                bd("620.00"),
	                bd("0.00"),
	                bd("620.00")
	        ));

	        check("Aktuell Gesamtkonto vor Zins", manager.getCurrentAccountBalance(), "2930.00");
	        check("Aktuell Tobias vor Zins", manager.getCurrentTobiasBalance(), "1310.00");
	        check("Aktuell Bernd vor Zins", manager.getCurrentBerndBalance(), "1620.00");

	        check("Gesamtkonto am 19.01.2026", manager.getAccountBalanceAt(LocalDate.of(2026, 1, 19)), "2310.00");
	        check("Tobias am 19.01.2026", manager.getTobiasBalanceAt(LocalDate.of(2026, 1, 19)), "1310.00");
	        check("Bernd am 19.01.2026", manager.getBerndBalanceAt(LocalDate.of(2026, 1, 19)), "1000.00");

	        // --------------------------------------------------
	        // 3) Durchschnittssalden Januar prüfen
	        //
	        // Tobias:
	        // 01.-09.01. = 1000 für 9 Tage
	        // 10.-31.01. = 1310 für 22 Tage
	        // Durchschnitt = 1220.00
	        //
	        // Bernd:
	        // 01.-19.01. = 1000 für 19 Tage
	        // 20.-31.01. = 1620 für 12 Tage
	        // Durchschnitt = 1240.00
	        //
	        // Total = 2460.00
	        // --------------------------------------------------
	        check("Jan Durchschnitt Tobias",
	                averageCalculator.getAverageTobiasBalance(
	                        LocalDate.of(2026, 1, 1),
	                        LocalDate.of(2026, 1, 31)),
	                "1220.00");

	        check("Jan Durchschnitt Bernd",
	                averageCalculator.getAverageBerndBalance(
	                        LocalDate.of(2026, 1, 1),
	                        LocalDate.of(2026, 1, 31)),
	                "1240.00");

	        check("Jan Durchschnitt Total",
	                averageCalculator.getAverageTotalBalance(
	                        LocalDate.of(2026, 1, 1),
	                        LocalDate.of(2026, 1, 31)),
	                "2460.00");

	        // --------------------------------------------------
	        // 4) Zinsbuchung Januar erzeugen
	        //
	        // Bruttozins: 24.60
	        // Steuer Bernd: 2.40
	        //
	        // Tobias Brutto = 24.60 * 1220 / 2460 = 12.20
	        // Bernd Brutto = 12.40
	        // Bernd Netto = 10.00
	        //
	        // Kontoeffekt = 22.20
	        // --------------------------------------------------
	        Transaction januaryInterest = interestCalculator.createInterestTransaction(
	                LocalDate.of(2026, 1, 1),
	                LocalDate.of(2026, 1, 31),
	                LocalDate.of(2026, 1, 31),
	                "Zinsen Januar 2026",
	                bd("24.60"),
	                bd("2.40")
	        );

	        check("Zinsbuchung Kontoeffekt", januaryInterest.getAccountEffekt(), "22.20");
	        check("Zinsbuchung Tobias", januaryInterest.getTobiasSplit(), "12.20");
	        check("Zinsbuchung Bernd", januaryInterest.getBerndSplit(), "10.00");

	        manager.addTransaction(januaryInterest);

	        check("Gesamtkonto nach Zins", manager.getCurrentAccountBalance(), "2952.20");
	        check("Tobias nach Zins", manager.getCurrentTobiasBalance(), "1322.20");
	        check("Bernd nach Zins", manager.getCurrentBerndBalance(), "1630.00");

	        // --------------------------------------------------
	        // 5) Interne Umbuchung testen
	        // 100 von Bernd zu Tobias, realer Kontoeffekt = 0
	        // --------------------------------------------------
	        manager.addTransaction(normalTx(
	                LocalDate.of(2026, 2, 5),
	                "Interne Umbuchung Bernd -> Tobias",
	                bd("0.00"),
	                bd("100.00"),
	                bd("-100.00")
	        ));

	        check("Gesamtkonto nach interner Umbuchung", manager.getCurrentAccountBalance(), "2952.20");
	        check("Tobias nach interner Umbuchung", manager.getCurrentTobiasBalance(), "1422.20");
	        check("Bernd nach interner Umbuchung", manager.getCurrentBerndBalance(), "1530.00");

	        // --------------------------------------------------
	        // 6) Februar-Durchschnitt testen
	        //
	        // Start Februar:
	        // Tobias = 1322.20
	        // Bernd  = 1630.00
	        //
	        // Ab 05.02:
	        // Tobias = 1422.20
	        // Bernd  = 1530.00
	        //
	        // Tobias Februar:
	        // 01.-04.02. = 1322.20 für 4 Tage
	        // 05.-28.02. = 1422.20 für 24 Tage
	        // Durchschnitt = 1407.91
	        //
	        // Bernd Februar:
	        // 01.-04.02. = 1630.00 für 4 Tage
	        // 05.-28.02. = 1530.00 für 24 Tage
	        // Durchschnitt = 1544.29
	        //
	        // Total bleibt konstant 2952.20
	        // --------------------------------------------------
	        check("Feb Durchschnitt Tobias",
	                averageCalculator.getAverageTobiasBalance(
	                        LocalDate.of(2026, 2, 1),
	                        LocalDate.of(2026, 2, 28)),
	                "1407.91");

	        check("Feb Durchschnitt Bernd",
	                averageCalculator.getAverageBerndBalance(
	                        LocalDate.of(2026, 2, 1),
	                        LocalDate.of(2026, 2, 28)),
	                "1544.29");

	        check("Feb Durchschnitt Total",
	                averageCalculator.getAverageTotalBalance(
	                        LocalDate.of(2026, 2, 1),
	                        LocalDate.of(2026, 2, 28)),
	                "2952.20");

	        // --------------------------------------------------
	        // 7) Zeitraum-Abfrage testen
	        // Januar enthält:
	        // - 10.01 Einzahlung Tobias
	        // - 20.01 Einzahlung Bernd
	        // - 31.01 Zinsbuchung
	        // --------------------------------------------------
	        checkInt("Anzahl Januar-Transaktionen",
	                manager.getTransactionsBetweenInclusive(
	                        LocalDate.of(2026, 1, 1),
	                        LocalDate.of(2026, 1, 31)
	                ).size(),
	                3);

	        // --------------------------------------------------
	        // 8) Alle Transaktionen ausgeben
	        // --------------------------------------------------
	        System.out.println();
	        System.out.println("=== ALLE TRANSAKTIONEN ===");
	        for (Transaction t : manager.getAllTransactions()) {
	            System.out.println(t);
	        }

	        System.out.println();
	        System.out.println("=== TEST ENDE ===");
	    }

	    // --------------------------------------------------
	    // HILFSMETHODEN
	    // --------------------------------------------------

	    private static BigDecimal bd(String value) {
	        return new BigDecimal(value);
	    }

	    private static void check(String label, BigDecimal actual, String expected) {
	        BigDecimal expectedValue = bd(expected);

	        if (actual.compareTo(expectedValue) == 0) {
	            System.out.println("[OK]   " + label + " -> " + actual);
	        } else {
	            System.out.println("[FAIL] " + label + " -> ist: " + actual + ", soll: " + expectedValue);
	        }
	    }

	    private static void checkInt(String label, int actual, int expected) {
	        if (actual == expected) {
	            System.out.println("[OK]   " + label + " -> " + actual);
	        } else {
	            System.out.println("[FAIL] " + label + " -> ist: " + actual + ", soll: " + expected);
	        }
	    }

	    private static Transaction normalTx(LocalDate date,
	                                        String description,
	                                        BigDecimal accountEffect,
	                                        BigDecimal tobiasSplit,
	                                        BigDecimal berndSplit) {

	        Split split = new Split(tobiasSplit, berndSplit);

	        // WICHTIG:
	        // Diese eine Zeile musst du evtl. an deinen normalen Transaction-Konstruktor anpassen.
	        // Ebenso den Enum-Wert, falls "Correction" bei dir anders heißt.

	        return new Transaction(
	                date,
	                TransactionType.Transfer,
	                description,
	                accountEffect,
	                split
	        );
	        */

	    
	}
}


