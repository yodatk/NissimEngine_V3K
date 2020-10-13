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
    if(debug){
        val b = Board("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR b KQkq - 0 1 ")
            // 13160609849181585112

        b.printBoard()

//
////        Evaluation.checkSort(b,0)
//
////         var lst = Evaluation.sortedPossibleMoves(b,0)
//
////        Moves.printMoveList(lst)
////
//        Search.searchPosition(b,Search.MAX_NODE_DEPTH)
//        println("to beat: 418203") // beaten with 418203
    }
    else{

        UCI.uciLoop()
    }

}

