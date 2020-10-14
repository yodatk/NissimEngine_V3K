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
        val b = Board(FENDebugConstants.TRICKY_POSITION.fen)
       // println(b.generateHashKey())
            // 13160609849181585112

        b.printBoard()

        ZorbistKeys.writeEntry(b.hashKey,25,1,hashFlag = ZorbistKeys.HASH_FLAG_EXACT)
        val score = ZorbistKeys.readHashData(hashKey = b.hashKey,20,30,1)
        println("score: $score")


//        Evaluation.checkSort(b,0)

//         var lst = Evaluation.sortedPossibleMoves(b,0)

//        Moves.printMoveList(lst)
//
        // Search.searchPosition(b,Search.MAX_NODE_DEPTH)
//        println("to beat: 418203") // beaten with 418203
    }
    else{

        UCI.uciLoop()
    }

}

