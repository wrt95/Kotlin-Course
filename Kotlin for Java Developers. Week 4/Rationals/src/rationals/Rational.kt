package rationals

import java.lang.IllegalArgumentException
import java.math.BigInteger

/**
 * This is a class for rational numbers, it takes two numbers.
 * - numerator:      The value before '/'
 * - denominator:    The value after  '/'
 */
class Rational (val numerator: BigInteger, val denominator: BigInteger): Comparable<Rational> {

    // constructor
    init {
        // Check that denominator is not zero
        if (denominator == BigInteger.ZERO) {
            throw IllegalArgumentException("Denominator can´t be 0!")
        }
    }

    /**
     * Function that converts to string.
     * If the denominator is 1, we can remove it (divide by 1 = itself),
     * otherwise the numerator and denominator is returned with a '/' between.
     */
    override fun toString(): String {
        // Normalise the rational number
        val number = normalise(this)

        return if (number.denominator == BigInteger.ONE) {
            "${number.numerator}"
        } else {
            "${number.numerator}/${number.denominator}"
        }
    }

    /**
     * Function to compare two rationals.
     * if the new number passed in is null or it is not a rational, it returns false
     * Otherwise, it normalises both numbers, and compares numerator with numerator,
     * and denominator with denominator, and returns true or false.
     */
    override fun equals(newNumber: Any?): Boolean {
        return if (newNumber == null || newNumber !is Rational) {
            false
        } else {
            val thisRational = normalise(this)
            val newNumberRational = normalise(newNumber)
            // Value to be returned:
            thisRational.numerator == newNumberRational.numerator
                && thisRational.denominator == newNumberRational.denominator
        }
    }

    /**
     * Function that compares two rational numbers.
     * It compares left hand side (LHS) with right hand side (RHS), and returns
     * -1 if LHS < RHS, 1 if LHS > RHS, and 0 else.
     */
    override fun compareTo(newNumber: Rational): Int {
        val lhs  = this.numerator   * newNumber.denominator
        val rhs = this.denominator * newNumber.numerator

        return when {
            lhs < rhs -> -1
            lhs > rhs ->  1
            else      ->  0
        }
    }

    /**
     * Function that normalises a rational number.
     * It uses the greatest common divisor (gcd) to get to the answer.
     */
    private fun normalise(rNumber: Rational): Rational {
        val gcd = rNumber.numerator.gcd(rNumber.denominator)

        // If the numerator < bigint zero AND denominator < bigint zero,
        // gets the absolute value and divides them by the gcd, respectively.
        return if (rNumber.numerator < BigInteger.ZERO && rNumber.denominator < BigInteger.ZERO)
            Rational(rNumber.numerator.abs() / gcd, rNumber.denominator.abs() / gcd)

        // If the numerator > bigint zero AND denominator < bigint zero, it negates the
        // numerator and divides by gcd, and gets the absolute value of denominator and divides by gcd.
        else if (rNumber.numerator > BigInteger.ZERO && rNumber.denominator < BigInteger.ZERO)
            Rational(rNumber.numerator.negate() / gcd, rNumber.denominator.abs() / gcd)

        // Otherwise, just divide each by gcd
        else
            Rational(rNumber.numerator / gcd, rNumber.denominator / gcd)
    }

    /**
     * Function to get the hash code for the rational number
     */
    override fun hashCode(): Int {
        var result = numerator.hashCode()
        result = 31 * result + denominator.hashCode()
        return result
    }
}

/**
 * Function that performs addition on two rational numbers.
 * It uses math rules as follows:                                  a/b + c/d = (ad+bc)/bd
 */
operator fun Rational.plus (newNumber: Rational): Rational {
    var newNumerator   = (numerator   * newNumber.denominator) + (denominator * newNumber.numerator)
    var newDenominator = (denominator * newNumber.denominator)

    return Rational(newNumerator, newDenominator)
}

/**
 * Function that performs subtraction on two rational numbers.
 * It uses math rules as follows:                                  a/b - c/d = (ad-bc)/bd
 */
operator fun Rational.minus (newNumber: Rational): Rational {
    var newNumerator   = (numerator   * newNumber.denominator) - (denominator * newNumber.numerator)
    var newDenominator = (denominator * newNumber.denominator)

    return Rational(newNumerator, newDenominator)
}

/**
 * Function that performs multiplication on two rational numbers.
 * It uses math rules as follows:                                  a/b * c/d = ac/bd
 */
operator fun Rational.times (newNumber: Rational): Rational {
    var newNumerator   = numerator   * newNumber.numerator
    var newDenominator = denominator * newNumber.denominator

    return Rational(newNumerator, newDenominator)
}

/**
 * Function that performs division on two rational numbers.
 * It uses math rules as follows:                                  (a/b) / (c/d) = ad/bc
 */
operator fun Rational.div (newNumber: Rational): Rational {
    var newNumerator   = numerator   * newNumber.denominator
    var newDenominator = denominator * newNumber.numerator

    return Rational(newNumerator, newDenominator)
}

/**
 * Function that converts a rational number to its unary minus.
 * It works as follows:
 *      normal:         a/b
 *      unary minus:   -a/b
 */
operator fun Rational.unaryMinus (): Rational {
    return Rational(-this.numerator, this.denominator)
}

/**
 * Function to create a rational number from two BigIntegers.
 * Numerator is the number before the function, denominator is the number passed it.
 */
infix fun BigInteger.divBy (denominator: BigInteger): Rational {
    return Rational(this, denominator)
}

/**
 * Function to create a rational number from two Ints.
 * Numerator is the number before the function, denominator is the number passed it.
 */
infix fun Int.divBy (denominator: Int): Rational {
    return Rational(this.toBigInteger(), denominator.toBigInteger())
}

/**
 * Function to create a rational number from two Longs.
 * Numerator is the number before the function, denominator is the number passed it.
 */
infix fun Long.divBy (denominator: Long): Rational {
    return Rational(this.toBigInteger(), denominator.toBigInteger())
}

/**
 * Function that converts string to rational number.
 * If the number is in wrong format, IllegalArgumentException is thrown.
 * If the string doesnt contain '/', then put the value as numerator, and 1 as denominator.
 * Otherwise, put the value before '/' as numerator, and the value after '/' as denominator.
 */
fun String.toRational (): Rational {
    fun String.toBigIntegerOrFail() = toBigIntegerOrNull() ?: throw IllegalArgumentException (
            "Expecting rational in the form of 'n/d' or 'n', " +
                    "was: '${this@toRational}'"
    )
    return when {
        !this.contains("/") -> Rational(this.toBigInteger(), BigInteger.ONE)
        else -> Rational(substringBefore('/').toBigInteger(), substringAfter('/').toBigInteger())
    }
}

/**
 * The main function.
 * It performs a few tests to check that the above methods are implemented correct.
 * If the implementation is correct, only 'true' should be printed.
 */
fun main() {
    val half  = 1 divBy 2
    val third = 1 divBy 3

    val sum: Rational = half + third
    println(5 divBy 6 == sum)

    val difference: Rational = half - third
    println(1 divBy 6 == difference)

    val product: Rational = half * third
    println(1 divBy 6 == product)

    val quotient: Rational = half / third
    println(3 divBy 2 == quotient)

    val negation: Rational = -half
    println(-1 divBy 2 == negation)

    println((2 divBy 1).toString()  == "2")
    println((-2 divBy 4).toString() == "-1/2")
    println("117/1098".toRational().toString() == "13/122")

    val twoThirds = 2 divBy 3
    println(half < twoThirds)

    println(half in third..twoThirds)

    println(2000000000L divBy 4000000000L == 1 divBy 2)

    println("912016490186296920119201192141970416029".toBigInteger() divBy
            "1824032980372593840238402384283940832058".toBigInteger() == 1 divBy 2)
}


