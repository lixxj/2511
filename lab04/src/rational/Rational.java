package rational;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Rational {

    private int num;
    private int den;

    /**
     * @pre den != 0
     * @param num
     * @param den
     */
    public Rational(int num, int den) {
        this.num = num;
        this.den = den;
        simplify();
    }

    public int getNumerator() { return this.num; }
    public int getDenominator() { return this.den; }

    private int greatestCommonDivisor(int a, int b) {
        a = Math.abs(a);
        b = Math.abs(b);
        if (a == b) return a;
        if (a > b) return greatestCommonDivisor(a - b, a);
        return greatestCommonDivisor(a, b - a);
    }

    private void simplify() {
        while (greatestCommonDivisor(num, den) != 1) {
            this.num /= greatestCommonDivisor(num, den);
            this.den /= greatestCommonDivisor(num, den);
        }
    }

    public String toString() {

        // Simplify the mixed fraction -- should not be necessary
        simplify();

        // // Get multiplicity factor
        // int mul = 0;
        // while (num > den) {
        //     this.num -= den;
        //     mul += 1;
        // }2
        String fraction = (this.num != this.den) ? convertIntSuper(num) + "/" + convertIntSub(den) : "";
        // if (mul == 0) return fraction;
        // return mul + " " + fraction;

        return fraction;
    }

    public Rational add(Rational number) {
        int numerator1 = getNumerator() * number.getDenominator();
        int numerator2 = number.getNumerator() * getDenominator();
        
        int denominator = getDenominator() * number.getDenominator();
        
        return new Rational(numerator1 + numerator2, denominator);
    }

    public Rational subtract(Rational number) {
        int numerator1 = getNumerator() * number.getDenominator();
        int numerator2 = number.getNumerator() * getDenominator();
        
        int denominator = getDenominator() * number.getDenominator();
        
        return new Rational(numerator1 - numerator2, denominator);
    }

    public Rational multiply(Rational number) {
        return new Rational(this.num * number.getNumerator(), this.den * number.getDenominator());
    }

    public Rational divide(Rational number) {
        return new Rational(this.num * number.getDenominator(), this.den * number.getNumerator());
    }

    private String convertIntSuper(int num) {
        return String.valueOf(num);
    }

    private String convertIntSub(int den) {
        return String.valueOf(den);
    }

    private final List<String> SUPER_NUMS = new ArrayList<String>(Arrays.asList(new String[] {"⁰", "¹", "²", "³", "⁴", "⁵", "⁶", "⁷", "⁸", "⁹"}));

    private final List<String> SUB_NUMS = new ArrayList<String>(Arrays.asList(new String[] {"₀", "₁", "₂", "₃", "₄", "₅", "₆", "₇", "₈", "₉"}));

}