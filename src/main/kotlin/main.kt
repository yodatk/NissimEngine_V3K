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
    b.printBoard()
    val backup = Board(b)
    b.parseFEN(FENDebugConstants.EMPTY.fen)
    b.printBoard()
    b = backup
    b.printBoard()


//    val movesList = b.generateMoves()
//    b.printBoard()
//
//    Moves.printMoveList(movesList)
//
//
//    /*
//
//
//
//    */

}