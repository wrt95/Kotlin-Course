package games.gameOfFifteen

/**
 * This function should return the parity of the permutation.
 * true - the permutation is even
 * false - the permutation is odd
 * https://en.wikipedia.org/wiki/Parity_of_a_permutation

 * If the game of fifteen is started with the wrong parity, you
 * can't get the correct result (numbers sorted in the right order,
 * empty cell at last). Thus the initial permutation should be correct.
 */
fun isEven(permutation: List<Int>): Boolean {
    // counter variable
    var counter = 0
    // Variable to store the indices in the permutation
    val indices = permutation.indices

    // Loop through the indices
    for (i in indices) {
        // Loop through them again, for every i
        for (j in indices) {
            // compare the current position (i) with the next ones (j), and make suree j is
            // greater, and if the value at i is greater than the value at j,
            // increment the counter
            if (i < j && permutation[i] > permutation[j]) {
                counter++
            }
        }
    }
    // Return if it is even or not
    return counter % 2 == 0
}