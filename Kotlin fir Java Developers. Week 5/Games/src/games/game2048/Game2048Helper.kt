package games.game2048

/**
 * This function moves all the non-null elements to the beginning of the list
 * (by removing nulls) and merges equal elements.
 * The parameter 'merge' specifies the way how to merge equal elements:
 * it returns a new element that should be present in the resulting list
 * instead of two merged elements.
 *
 * If the function 'merge("a")' returns "aa",
 * then the function 'moveAndMergeEqual' transforms the input in the following way:
 *   a, a, b -> aa, b
 *   a, null -> a
 *   b, null, a, a -> b, aa
 *   a, a, null, a -> aa, a
 *   a, null, a, a -> aa, a
 *
 * You can find more examples in 'TestGame2048Helper'.
*/
fun <T : Any> List<T?>.moveAndMergeEqual(merge: (T) -> T): List<T> {
    // Filter out where it is not null, and store in a list.
    val nonNulls = this.filterNotNull().toMutableList()

    with (nonNulls) {
        // counter variable
        var counter = 1

        // While the counter is smaller than the size
        while (counter < this.size) {
            // Check if the two positions have same value
            if (this[counter] == this[counter - 1]) {
                // then set previous position to the merged result of this position
                this[counter - 1] = merge(this[counter])
                // Remove the value at this position
                this.removeAt(counter)
            }
            // Increment the counter
            counter++
        }
    }
    // Return the list of non nulls
    return nonNulls
}

