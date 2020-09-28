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
    Attacks.initLeaperAttacks()
    enumValues<Square>().forEach {
        Attacks.pawnAttacks[Color.WHITE.value][it.bit].printBitboard()
    }
//    enumValues<Square>().forEach {
//        Attacks.pawnAttacks[Color.BLACK.value][it.bit]
//    }

}