package mastermind

data class Evaluation(val rightPosition: Int, val wrongPosition: Int)

fun evaluateGuess(secret: String, guess: String): Evaluation {
    // Variable declaration:
    var correctPos = 0                          // Counter for correct positions
    var wrongPos = 0                            // Counter for wrong positions
    var wrongList: List<Char> = emptyList()     // List to store the characters at wrong position
    var remainingList: List<Char> = emptyList() // List to store the remaining characters

    // Loop from 0 to 3, since there are 4 chars in the secret
    for (i in 0..3) {
        // the current character value in the guesses. If it is equal to the secret, increment.
        var ch = guess [i]
        if(ch == secret[i]) {
            correctPos++
        }
        // If it is not, add the value from secret to wrong list, and the character from guess to remaining
        else {
            wrongList += secret[i]
            remainingList += ch
        }
    }
    // Loop through all characters in the wrongList
    for (ch in wrongList) {
        // If the character is in the remaining list, increment wrong position, and delete from remaining
        if (ch in remainingList) {
            wrongPos++
            remainingList -= ch
        }
    }
    // Return the numbers
    return Evaluation(correctPos, wrongPos)
}

/*
FUNCTIONAL STYLE:

data class Evaluation(val rightPosition: Int, val wrongPosition: Int)

fun evaluateGuess(secret: String, guess: String): Evaluation {

    val rightPositions = secret.zip(guess).count { it.first == it.second }      // Zip creates pairs (aaa).zip(bbb) = (ab, ab, ab)

    val commonLetters = "ABCDEF".sumBy { ch ->

        Math.min(secret.count { it == ch }, guess.count { it == ch })
    }
    return Evaluation(rightPositions, commonLetters - rightPositions)
}

fun main(args: Array<String>) {
    val result = Evaluation(rightPosition = 1, wrongPosition = 1)
    evaluateGuess("BCDF", "ACEB") eq result
    evaluateGuess("AAAF", "ABCA") eq result
    evaluateGuess("ABCA", "AAAF") eq result
}
 */
