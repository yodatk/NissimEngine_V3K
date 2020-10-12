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
        Search.searchPosition(b,4)


    }
    else{

        UCI.uciLoop()
    }

}

