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
    Attacks.initLeaperAttacks()
    //val attackMask = Attacks.maskBishopAttacks(Square.d4)
    val attackMask = Attacks.maskRookAttacks(Square.d4)

    for (i in 0..100){
        val occupancy = Attacks.setOccupancy(i,attackMask.countBits(),attackMask);
        occupancy.printBitboard()
        readLine()
    }

}