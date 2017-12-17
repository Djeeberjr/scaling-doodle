import java.math.BigInteger;
/**
 * Repräsentiert einen Bruch
 *
 * @author Niklas Kapelle
 * @author Henri Bußmann
 * @version 1.1 11.12.2017
 */
public class Fraction extends Number implements Comparable<Fraction> {

    public static final Fraction NaN = new Fraction("0", "0");

    final private BigInteger numerator;
    final private BigInteger denominator;


    public Fraction(BigInteger numerator, BigInteger denominator) {

        if(denominator.compareTo(BigInteger.ZERO) < 0){
            denominator = denominator.negate();
            numerator = numerator.negate();
        }

        if(denominator.compareTo(BigInteger.ZERO) == 0) {
            this.numerator = BigInteger.ZERO;
            this.denominator = BigInteger.ZERO;
        } else {
            BigInteger foo = ggt(numerator, denominator);

            if (foo.compareTo(BigInteger.ZERO) == 0) {
                this.numerator = BigInteger.ZERO;
                this.denominator = BigInteger.ZERO;
            } else {
                this.numerator = numerator.divide(foo);
                this.denominator = denominator.divide(foo);

            }
        }
    }

    public Fraction(String numerator, String denominator) {
        this(new BigInteger(numerator),new BigInteger(denominator));
    }

    public Fraction(int numerator, int denominator) {
        this(BigInteger.valueOf(numerator), BigInteger.valueOf(denominator));
    }

    @Override
    public int intValue() throws ArithmeticException {
        return numerator.intValue() / denominator.intValue();
    }

    @Override
    public long longValue() throws ArithmeticException {
        return numerator.longValue() / denominator.longValue();
    }

    @Override
    public float floatValue() {
        if (isNaN()) return Float.NaN;
        return numerator.floatValue() / denominator.floatValue();
    }

    @Override
    public double doubleValue() {
        if (isNaN()) return Double.NaN;
        return numerator.doubleValue() / denominator.doubleValue();
    }

    public BigInteger getBigInt() {
        return this.numerator.divide(this.denominator);
    }

    public Fraction add(Fraction r) {
        if (isNaN()) return NaN;
        if (r.denominator.compareTo(this.denominator) == 0) {
            return new Fraction(r.numerator.add(this.numerator), this.denominator);
        } else {
            Fraction a = this.extend(r.denominator);
            Fraction b = r.extend(this.denominator);

            return new Fraction(a.numerator.add(b.numerator), b.denominator);
        }
    }

    public Fraction subtract(Fraction r) {
        if (isNaN()) return NaN;
        Fraction rtn = new Fraction(r.numerator.negate(), r.denominator);
        return this.add(rtn);
    }

    public Fraction multiply(Fraction r) {
        if (isNaN()) return NaN;
        BigInteger newNumerator = this.numerator.multiply(r.numerator);
        BigInteger newDenominator = this.denominator.multiply(r.denominator);

        return new Fraction(newNumerator, newDenominator);
    }

    public Fraction divide(Fraction r) {
        if (isNaN()) return NaN;
        return new Fraction(this.numerator.multiply(r.denominator), this.denominator.multiply(r.numerator));
    }

    boolean isInteger() {
        return (this.denominator.equals(BigInteger.ONE));
    }

    public Fraction extend(BigInteger foo) {
        return new Fraction(numerator.multiply(foo), denominator.multiply(foo));
    }

    public boolean isNaN() {
        return denominator.equals(BigInteger.ZERO);
    }

    BigInteger getNumerator() {
        return numerator;
    }

    BigInteger getDenominator() {
        return denominator;
    }


    @Override
    public String toString() {
        if(isInteger()){
            return intValue() + "";
        }else{
            return isNaN() ? "NaN" : numerator + "/" + denominator;
        }

    }

    @Override
    public int compareTo(Fraction o) {

        BigInteger t = this.numerator.multiply(o.denominator);
        BigInteger f = o.numerator.multiply(this.denominator);

        return t.compareTo(f);

    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Fraction)
            return this.compareTo((Fraction)obj) == 0;

        throw new IllegalArgumentException("object is not a fraction");
    }

    @Override
    public int hashCode() {
        return numerator.hashCode()+denominator.hashCode();
    }


    private BigInteger ggt(BigInteger a, BigInteger b) {
        if(b.compareTo(BigInteger.ZERO) == 0)
            return a;
        BigInteger mod = a.mod(b);
        return mod.equals(BigInteger.ZERO) ? b : ggt(b, mod);
    }
}