
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
fun main(args: Array<String>) {
    println("Nissim: HALLAWA!\n===================================\n")
    Attacks.initAll()
    val occupancy = BitBoard()
    occupancy.setBitOn(Square.c5)
    occupancy.setBitOn(Square.f2)
    occupancy.setBitOn(Square.g7)
    occupancy.setBitOn(Square.g5)
    occupancy.setBitOn(Square.e2)
    occupancy.setBitOn(Square.e7)
    occupancy.setBitOn(Square.b2)
    occupancy.setBitOn(Square.h8)
    occupancy.printBitboard()
    Attacks.getBishopAttacks(Square.d4,occupancy).printBitboard()
    Attacks.getRookAttacks(Square.e5,occupancy).printBitboard()
}