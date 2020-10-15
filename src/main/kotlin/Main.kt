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
    if (debug) {


    } else {

        UCI.uciLoop()
    }

}
  // position fen R7/8/6k1/8/8/8/8/2K5 w - - 0 1 moves c1d2

