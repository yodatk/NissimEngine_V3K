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
//        val b = Board("rnbqkbnr/ppp1pppp/8/8/3Pp3/2N5/PPP2PPP/R1BQKBNR b KQkq - 1 3" )
//        b.printBoard()
//        Search.searchPosition(b,1)
        println("move score: ${Moves.MVV_LVA[Piece.P.ordinal][Piece.n.ordinal]}")

    }
    else{

        UCI.uciLoop()
    }

}

