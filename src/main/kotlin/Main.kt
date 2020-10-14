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
    val debug = true
    if (debug) {
        val b = Board(FENDebugConstants.START_POSITION.fen)
        b.printBoard()
        Search.searchPosition(b, 7)

    } else {

        UCI.uciLoop()
    }

}

