package games.gameOfFifteen

import board.*
import games.game.Game

/**
 * Implement the Game of Fifteen (https://en.wikipedia.org/wiki/15_puzzle).
 * When you finish, you can play the game by executing 'PlayGameOfFifteen'.
 */
fun newGameOfFifteen(initializer: GameOfFifteenInitializer = RandomGameInitializer()): Game =
        GameOfFifteenImpl(initializer)

/**
 * Class that implements the game of fifteen
 */
class GameOfFifteenImpl(private val initializer: GameOfFifteenInitializer) : Game {
    // The width of the board
    private val width = 4

    // The gameboard to use
    private val board = createGameBoard<Int>(width)

    /**
     * Function that initialises the game
     */
    override fun initialize() {
        var boardRow = initializer.initialPermutation.chunked(width)
        for (i in 1..4) {
            for (j in 1..4) {
                board[Cell(i, j)] = boardRow[i-1].getOrNull(j-1)
            }
        }
    }

    /**
     * Function that checks if moving is possible
     */
    override fun canMove(): Boolean {
        return board.any { it == null }
    }

    /**
     * Function that checks if the game is over, when winning.
     * It compares the current list with the sorted optimal list,
     * and returns whether they are equal or not.
     */
    override fun hasWon(): Boolean {
        // Get the positions in the board where there is no nulls.
        val currentList = board.getAllCells().asSequence().map { theCell ->
            board[theCell]
        }.filterNotNull().toList()

        val winningList = currentList.sorted()

        return currentList == winningList
    }

    /**
     * Function that processes the move.
     * It finds where the null is in the board, the gets the neighbouring
     * cells, before moving them around.
     */
    override fun processMove(direction: Direction) {
        with (board) {
            // Find where it is null in the gameboard.
            find {
                it == null
            }?.also { theCell ->
                // get the neighbours of the reversed of the direction.
                theCell.getNeighbour(direction.reversed())?.also { neighbourCell ->
                    // Set the cell to its neighbour, and the neighbour to null
                    this[theCell] = this[neighbourCell]
                    this[neighbourCell] = null
                }
            }
        }
    }

    override fun get(i: Int, j: Int): Int? {
        // Run the board
        return board.run {
            get(getCell(i, j))
        }
    }
}