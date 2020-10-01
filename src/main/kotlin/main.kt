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
    val board = Board()
    board.pieceBitboards[Piece.P.ordinal]
    board.pieceBitboards[Piece.P.ordinal].setBitOn(Square.e2)
    board.pieceBitboards[Piece.P.ordinal].printBitboard()
    println("piece: ${Piece.P.name}")




}