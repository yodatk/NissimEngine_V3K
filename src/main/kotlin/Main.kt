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
    val debug = false
    if (debug) {
        val b = Board(FENDebugConstants.START_POSITION.fen)
        b.printBoard()
        Search.searchPosition(b, 10)
        b.makeMove(Search.principalVariationTable[0][0])
        Search.searchPosition(b,10)
        //START
        // info score cp 20 depth 7 nodes 56762 pv b1c3 d7d5 d2d4 b8c6 g1f3 g8f6 c1f4
        //bestmove b1c3

        //TRICKY                          354535
        //info score cp -20 depth 7 nodes 416297 pv e2a6 e6d5 c3d5 b6d5 a6b7 a8b8 e4d5

    } else {

        UCI.uciLoop()
    }

}

