import enums.FENDebugConstants

/**
 * ================================================
 *
 * NISSIM ENGINE (HALLAWA)
 * @author: Tomer Gonen (yodatk)
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

    val b = Board(FENDebugConstants.TRICKY_POSITION.fen)
    //val b = Board(FENDebugConstants.START_POSITION.fen)
    b.printBoard()
    Search.searchPosition(b,10)

    //UCI.uciLoop()

}

