
package board

import board.Direction.*
import java.lang.IllegalArgumentException
import kotlin.math.min

/**
 * This class implements the Interface SquareBoard.
 * It takes the width of the board to keep control of where we are in the board.
 */
open class SquareBoardImpl(override val width: Int): SquareBoard {

    // 2D Array to store the board of cells
    private val board = Array(width) { row ->
        Array(width) { column ->
            Cell (row + 1, column + 1)
        }
    }

    /**
     * The constructor.
     * It throws IllegalArgumentException if the width is 0 or smaller.
     */
    init {
        if (width < 1) {
            throw IllegalArgumentException("The width must be greater than 0")
        }
    }

    /**
     * Function that either gets the cell, or null.
     * If i or j is out of bounds, it returns null, otherwise it
     * returns the cell.
     */
    override fun getCellOrNull(i: Int, j: Int): Cell? {
        if (i < 1 || i > width || j < 1 || j > width) {
            return null
        }
        return board [i - 1][j - 1]     // -1 because of array starts at 0
    }

    /**
     * Function that gets the cell.
     * If i or j is out of bounds, it throws IllegalArgumentException,
     * otherwise it returns the cell.
     */
    override fun getCell(i: Int, j: Int): Cell {
        //return getCellOrNull(i, j) ?: throw IllegalArgumentException("Not correct values")
        if (i < 1 || i > width || j < 1 || j > width) {
            throw IllegalArgumentException()
        }
        return board [i - 1][j - 1]     // -1 because of array starts at 0
    }

    /**
     * Function that returns all the cells in the board.
     */
    override fun getAllCells(): Collection<Cell> {
        // return board
        return board.iterator().asSequence().flatMap {
            it.iterator().asSequence()
        }.toList()
    }

    /**
     * Function that returns all cells in a given row, with a given range.
     * It compares the range values (users can use both range high to low, and low to high.
     * For .getRow(1, 1..2) (or .getRow(1, 2 downTo 1), the function will return:  [(1,1), (1, 2)]
     */
    override fun getRow(i: Int, jRange: IntProgression): List<Cell> {
        // if the first value in the range is greater than the last
        return if (jRange.first > jRange.last) {
            val rowRange = jRange.last..(min(width, jRange.first))
            // Map the row range to the cells, and reverse it (so we get correct order)
            rowRange.map { getCell(i, it) }.reversed()
        }
        else {
            val rowRange = jRange.first..(min(width, jRange.last))
            // Map the row range to the cells.
            rowRange.map { getCell(i, it) }
        }
    }

    /**
     * Function that returns all cells in a given column, with a given range.
     * It compares the range values (users can use both range high to low, and low to high.
     * For .getColumn(1..2, 1) (or .getColumn(2 downTo 1, 1), the function will return:  [(1,1), (2, 1)]
     */
    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> {
        return if (iRange.first > iRange.last) {
            val columnRange = iRange.last..(min(iRange.first, width))
            // Map column range to the cells, and reverse it (so we get correct order)
            columnRange.map { getCell(it, j) }.reversed()
        }
        else {
            val columnRange = iRange.first..(min(iRange.last, width))
            // Map column range to the cells
            columnRange.map { getCell(it, j) }
        }
    }

    /**
     * Function that returns the neighbour cell based on the direction provided.
     * If no neighbour, it returns null
     *
     *                          up:    (i+1, j)
     *
     *      left: (i, j-1)      Current: (i, j)      right: (i, j+1)
     *
     *                          down   (i-1, j)
     */
    override fun Cell.getNeighbour(direction: Direction): Cell? {
        return when (direction) {
            UP    -> getCellOrNull(i-1, j)      // You move i-1
            LEFT  -> getCellOrNull(i, j-1)      // You move j-1
            DOWN  -> getCellOrNull(i+1, j)      // You move i+1
            RIGHT -> getCellOrNull(i, j+1)      // You move j+1
        }
    }
}


/**
 * Class that implements the game board Interface
 */
class GameBoardImpl<T>(width: Int) : GameBoard<T>, SquareBoardImpl(width) {

    // HashMap to store the values in the cells.
    private val cellValueMap = HashMap<Cell, T?>(width)

    /**
     * Constructor
     * Set all values in the map initially to null
     */
    init {
        getAllCells().asSequence().forEach {
            cellValueMap[it] = null
        }
    }

    /**
     * Function that returns the value in the cell
     */
    override fun get(cell: Cell): T? {
        return cellValueMap[cell]
    }

    /**
     * Function that sets the value in the cell
     */
    override fun set(cell: Cell, value: T?) {
        cellValueMap[cell] = value
    }

    /**
     * Function that returns a collection of cells, based on if the key from
     * the key-value pairs are matching the predicate.
     */
    override fun filter(predicate: (T?) -> Boolean): Collection<Cell> {
        return cellValueMap.filterValues(predicate).keys
    }

    /**
     * Function that returns the first match of the key from the key-value pairs
     */
    override fun find(predicate: (T?) -> Boolean): Cell? {
        return cellValueMap.filterValues(predicate).keys.first()
    }

    /**
     * Function that returns true is the set of key-value pairs
     * matching the predicate is not empty
     */
    override fun any(predicate: (T?) -> Boolean): Boolean {
        return cellValueMap.filterValues(predicate).isNotEmpty()
    }

    /**
     * Function that returns true if the size of all key-value pairs matching
     * the predicate is equal to the width * width
     */
    override fun all(predicate: (T?) -> Boolean): Boolean {
        return cellValueMap.filterValues(predicate).size == width * width
    }
}

fun   createSquareBoard(width: Int): SquareBoard  = SquareBoardImpl(width)
fun <T> createGameBoard(width: Int): GameBoard<T> = GameBoardImpl(width)

