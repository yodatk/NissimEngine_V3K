import RandomNumbers.FFFF

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
    BitBoard(RandomNumbers.getRandomUIntNumber().toULong()).printBitboard()
    BitBoard(RandomNumbers.getRandomUIntNumber().toULong() and FFFF.toULong()).printBitboard()
    BitBoard(RandomNumbers.getRandomULongNumber()).printBitboard()
    BitBoard(RandomNumbers.generateMagicNumber()).printBitboard()


}