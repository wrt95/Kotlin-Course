package nicestring

/**
 *     A string is nice if at least two of the following conditions are satisfied:
 *     1. It doesn't contain substrings bu, ba or be;
 *     2. It contains at least three vowels (vowels are a, e, i, o and u);
 *     3. It contains a double letter (at least two similar letters following one another), like b in "abba".
 */

fun String.isNice(): Boolean {
    // Counter variable to keep count of the true conditions
    var trueCounter = 0
    // Variable to store a list of the illegal pairs bu, ba, be
    var illegalString = listOf (                    // List of pairs of 2 characters
                                Pair('b', 'u'),
                                Pair('b', 'a'),
                                Pair('b', 'e')
                               )
    // Variable to store a list of the vowels
    var vowels        = listOf ('a', 'e', 'i', 'o', 'u')
    // Variable to store two characters next to each other
    var twoChars     = this.zipWithNext()

    // Condition #1
    // Filter out two and two characters,
    // if two characters next to each other doesnt have any illegal strings, increment the counter
    if (twoChars.filter { illegalString.contains(it)}.count() == 0){
        trueCounter++
    }
    // Condition #2
    // Filter out the characters, check if the vowel list contains it,
    // if the count is greater than 2 (3 or more), increment the counter
    if (this.filter { vowels.contains(it) }.count() > 2) {
        trueCounter++
    }
    // Condition #3
    // Filter out two and two characters,
    // if the two characters next to each other is equal, increment the counter
    if (twoChars.filter { it.first == it.second }.count() > 0 ){
        trueCounter++
    }
    // If the counter is greater than 1 (2 or more), return true
    if (trueCounter > 1) {
        return true
    }
    return false
}