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
    if(debug){
        val b = Board(FENDebugConstants.TRICKY_POSITION.fen)

        b.printBoard()


//        Evaluation.checkSort(b,0)

//         var lst = Evaluation.sortedPossibleMoves(b,0)

//        Moves.printMoveList(lst)
//
        Search.searchPosition(b,Search.MAX_NODE_DEPTH)
        println("to beat: 418203") // beaten with 418203
    }
    else{

        UCI.uciLoop()
    }

}

