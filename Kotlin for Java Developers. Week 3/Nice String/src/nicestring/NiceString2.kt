package nicestring

/**
 *  A string is nice if at least two of the following conditions are satisfied:
 *  1. It doesn't contain substrings bu, ba or be;
 *  2. It contains at least three vowels (vowels are a, e, i, o and u);
 *  3. It contains a double letter (at least two similar letters following one another), like b in "abba".
 */

// This is a sample solution:
fun String.isNice2(): Boolean {
    val noBadSubstring = !contains("ba") && !contains("be") && !contains("bu")

    val hasThreeVowels = count {
        it == 'a' || it == 'e' || it == 'i' || it == 'o' ||it == 'u'
    } >= 3

    var hasDouble = false

    if (length > 1) {
        var prevCh: Char? = null
        for (ch in this) {
            if (ch == prevCh) {
                hasDouble = true
            }
            prevCh = ch
        }
    }
    var conditions = 0
    if (noBadSubstring) conditions++
    if (hasThreeVowels) conditions++
    if (hasDouble)      conditions++

    if (conditions >= 2) return true
    return false
}