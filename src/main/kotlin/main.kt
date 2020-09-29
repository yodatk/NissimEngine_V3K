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
    println(block.countBits())
}