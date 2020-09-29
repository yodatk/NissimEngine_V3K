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
    println("\n\n")
    //Attacks.initLeaperAttacks()
    var block = BitBoard(0UL)
    block.setBitOn(Square.b7)
    block.setBitOn(Square.c2)
    block.setBitOn(Square.g2)
    block.setBitOn(Square.g6)
    Attacks.bishopAttacksOnTheFly(Square.e4,block).printBitboard()
    block = BitBoard(0UL)
    block.setBitOn(Square.e7)
    block.setBitOn(Square.e2)
    block.setBitOn(Square.b4)
    block.setBitOn(Square.g4)
    Attacks.rookAttacksOnTheFly(Square.e4,block).printBitboard()
}