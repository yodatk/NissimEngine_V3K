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
    block.setBitOn(Square.d7)
    block.setBitOn(Square.d2)
    block.setBitOn(Square.d1)
    block.setBitOn(Square.b4)
    block.setBitOn(Square.g4)
    var curr = block.getLSB();
    println("index: $curr square to coordinates ${Square.fromIntegerToSquare(curr)!!.name}")
    val test = BitBoard(0UL);
    test.setBitOn(Square.d7);
    curr = test.getLSB();
    println("index: $curr square to coordinates ${Square.fromIntegerToSquare(curr)!!.name}")

}