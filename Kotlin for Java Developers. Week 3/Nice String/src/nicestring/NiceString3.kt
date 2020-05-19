package nicestring

/**
 *  A string is nice if at least two of the following conditions are satisfied:
 *  1. It doesn't contain substrings bu, ba or be;
 *  2. It contains at least three vowels (vowels are a, e, i, o and u);
 *  3. It contains a double letter (at least two similar letters following one another), like b in "abba".
 */

// This is an improved version of sample solution NiceString2:
fun String.isNice3(): Boolean {
    val noBadSubstring2 = setOf("ba", "be", "bu").none { this.contains(it) }
    // Can replace none with all but then need to add '!'
    // setOf("ba", "be", "bu").all { !this.contains(it) }

    val hasThreeVowels = count { it in "aeiou" } >= 3
    // Can replace with  count { it in setOf ('a', 'e', 'i', 'o', 'u') } >= 3

    val hasDouble = zipWithNext().any { it.first == it.second }
    // alternatively: (0 until lastIndex).any{ this[it] == this [it+1] }
    // or:            windowed(2).any {it[0] == it[1] }

    // Create a list of the 3 conditions, if the count of true in this list is
    // greater than or equal to 2, then it will return true, else false
    return listOf (noBadSubstring2, hasThreeVowels, hasDouble).count{ it } >= 2
}