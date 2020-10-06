import enums.*

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
    Attacks.initAll()
    var b = Board("r3k2r/ppppqpb1/bn2pnp1/2pPN3/Pp2P3/2N2Q1p/P1PBBPpP/R3K2R w KQkq a3 0 1 ")
    b.printBoard()

}