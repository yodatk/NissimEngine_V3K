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
    val occ = BitBoard()
    occ.setBitOn(Square.b6)
    occ.setBitOn(Square.d6)
    occ.setBitOn(Square.f6)
    occ.setBitOn(Square.b4)
    occ.setBitOn(Square.g4)
    occ.setBitOn(Square.c3)
    occ.setBitOn(Square.d3)
    occ.setBitOn(Square.e3)
    Attacks.getQueenAttacks(Square.d4,occ).printBitboard()
}