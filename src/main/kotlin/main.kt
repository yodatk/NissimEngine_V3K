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
    var b = Board(FENDebugConstants.TRICKY_POSITION.fen)
    val movesList = b.generateMoves()
    b.printBoard()

    Moves.printMoveList(movesList)


    /*



    */

}