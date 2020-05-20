package games.game2048

import board.Cell
import board.Direction
import board.GameBoard
import board.createGameBoard
import games.game.Game

/*
 * Your task is to implement the game 2048 https://en.wikipedia.org/wiki/2048_(video_game).
 * Implement the utility methods below.
 *
 * After implementing it you can try to play the game running 'PlayGame2048'.
 */
fun newGame2048(initializer: Game2048Initializer<Int> = RandomGame2048Initializer): Game =
        Game2048(initializer)

class Game2048(private val initializer: Game2048Initializer<Int>) : Game {
    private val board = createGameBoard<Int?>(4)

    override fun initialize() {
        repeat(2) {
            board.addNewValue(initializer)
        }
    }

    override fun canMove() = board.any { it == null }

    override fun hasWon() = board.any { it == 2048 }

    override fun processMove(direction: Direction) {
        if (board.moveValues(direction)) {
            board.addNewValue(initializer)
        }
    }

    override fun get(i: Int, j: Int): Int? = board.run { get(getCell(i, j)) }
}

/**
 * Add a new value produced by 'initializer' to a specified cell in a board.
 */
fun GameBoard<Int?>.addNewValue(initializer: Game2048Initializer<Int>) {
    // The new initialised result
    val newResult = initializer.nextValue(this)

    // if it is not null, set the position of the first value, to the second
    if (newResult != null) {
        this[newResult.first] = newResult.second
    }
}

/**
 * Update the values stored in a board,
 * so that the values were "moved" in a specified rowOrColumn only.
 * Use the helper function 'moveAndMergeEqual' (in Game2048Helper.kt).
 * The values should be moved to the beginning of the row (or column),
 * in the same manner as in the function 'moveAndMergeEqual'.
 * Return 'true' if the values were moved and 'false' otherwise.
 */
fun GameBoard<Int?>.moveValuesInRowOrColumn(rowOrColumn: List<Cell>): Boolean {
    // A list to store the valued mapped on the position of this cell, with the moved and merged value *2 (move and merge 4 = 8)
    val list: List<Int> = rowOrColumn.map { theCell -> this[theCell] }.moveAndMergeEqual { theValue-> theValue * 2 }

    // For each index in the given list of cells, set the cell to the value at the index, or null
    rowOrColumn.forEachIndexed { index, cell -> set(cell, list.getOrNull(index)) }

    // Check that the list is not empty, and that the sizes are not the same
    return list.isNotEmpty() && list.size != rowOrColumn.size
}

/**
 * Update the values stored in a board,
 * so that the values were "moved" to the specified direction
 * following the rules of the 2048 game .
 * Use the 'moveValuesInRowOrColumn' function above.
 * Return 'true' if the values were moved and 'false' otherwise.
 */
fun GameBoard<Int?>.moveValues(direction: Direction): Boolean {
    // Variable to check if values were moved.
    var wereMoved = false

    // Go through all values in the board
    for (i in 1..width) {
        // Check the value when moving
        val rowOrColumn = when (direction) {
            Direction.UP    -> getColumn(1..width, i)
            Direction.DOWN  -> getColumn(1..width, i).asReversed()
            Direction.LEFT  -> getRow(i, 1..width)
            Direction.RIGHT -> getRow(i, 1..width).asReversed()
        }
        // Update the row and columns, and if they are moved, this will change to true (T || F = T)
        wereMoved = moveValuesInRowOrColumn(rowOrColumn) || wereMoved
    }
    // Return if they were moved or not
    return wereMoved
}