import enums.Color
import enums.Piece
import enums.Square

/**
 * ================================================
 *
 * NISSIM ENGINE (HALLAWA)
 * @author: Tomer Gonen
 *
 * ================================================
 */







/**
================================================
Main
================================================
 */

@ExperimentalUnsignedTypes
fun main() {
    println("Nissim: HALLAWA!\n===================================\n")

    Attacks.initAll()
    val board = Board.createStartBoard()
    //board.enpassant = Square.e3
    //board.side = Color.BLACK
    board.castle =0
    //board.castle = 1
    board.castle = board.castle or 2
    board.castle = board.castle or 4
    //board.castle = board.castle or 8

    board.printBoard()




}