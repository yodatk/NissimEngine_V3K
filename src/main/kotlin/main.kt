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
    var b = Board("r3k2r/pPppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPpP/R3K2R b KQkq - 0 1 ")
    b.printBoard()
    val movesList : List<Int> = b.generateMoves()
    for (move in movesList){
        val bCopy = Board(b)
        b.makeMove(move)
        b.printBoard()
        readLine()
        b = bCopy
    }


}