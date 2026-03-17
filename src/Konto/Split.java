package Konto;
import java.math.BigDecimal;
import java.util.Objects;
public class Split {
	private final BigDecimal Bernd;
	private final BigDecimal Tobias;
	public Split(BigDecimal Tobias, BigDecimal Bernd) {
		this.Bernd = Objects.requireNonNull(Bernd);
		this.Tobias = Objects.requireNonNull(Tobias);
	}
	
	public BigDecimal sum() {
		return Tobias.add(Bernd);
	}
	
	public BigDecimal getTobias() {
		return Tobias;
	}
	public BigDecimal getBernd() {
		return Bernd;
	}
@Override
public String toString() {
	return String.format("| %6s | %5s |", Tobias, Bernd);
}
}
