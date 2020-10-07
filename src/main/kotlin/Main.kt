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
    val debug = true
    if(debug){
        val b = Board("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR w KQkq - 0 1 ")
        b.printBoard()
        println("score: ${Evaluation.evaluate(b)}")

    }
    else{
        Attacks.initAll()
        UCI.uciLoop()
    }

}

