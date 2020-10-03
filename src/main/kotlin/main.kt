import enums.Color
import enums.FENDebugConstants
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
    Attacks.initAll()
    var b = Board("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPpP/R3K2R b KQkq - 0 1 ")
    b.generateMoves()
    /*
    * should contain these:
    pawn push: c7c6
    double pawn push: c7c5
    pawn push: d7d6
    pawn push: g6g5
    pawn push: b4b3
    pawn promotion: g2g1q
    pawn promotion: g2g1r
    pawn promotion: g2g1b
    pawn promotion: g2g1n
    * */
    b= Board("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPpP/R3K2R w KQkq - 0 1 ")
    println()
    println()
    b.generateMoves()

    b.printBoard()

    /*
    pawn push: d5d6
    pawn push: a2a3
    double pawn push: a2a4
    pawn push: b2b3
    */
}