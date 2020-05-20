package games.gameOfFifteen

interface GameOfFifteenInitializer {
    /*
     * Even permutation of numbers 1..15
     * used to initialized the first 15 cells on a board.
     * The last cell is empty.
     */
    val initialPermutation: List<Int>
}

class RandomGameInitializer : GameOfFifteenInitializer {
    /**
     * Generate a random permutation from 1 to 15.
     * `shuffled()` function might be helpful.
     * If the permutation is not even, make it even (for instance,
     * by swapping two numbers).
     */
    override val initialPermutation by lazy {
        // Create the permutations from numbers of 1..15, and then shuffle them
        var thePermutations = (1..15).shuffled()

        // The initial list, with the numbers 1..15 sorted.
        val initialList = (1..15).toList()

        // If the permutations is not even, and the two lists are not the same, the shuffle again
        if (!isEven(thePermutations) && thePermutations != initialList) {
            thePermutations.shuffled()
        }
        // "return" the permutations
        thePermutations
    }
}

