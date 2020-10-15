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
    if (debug) {
        val b = Board("6k1/5p1p/8/8/8/8/5P1P/6K1 w - -")
        b.printBoard()
        println("board score: ${Evaluation.evaluate(b)}")

    } else {

        UCI.uciLoop()
    }

}
  // position fen R7/8/6k1/8/8/8/8/2K5 w - - 0 1 moves c1d2

