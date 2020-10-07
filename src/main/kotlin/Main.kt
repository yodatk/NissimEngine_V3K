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
        val b = Board("1QR5/R4pk1/7p/6p1/4p1P1/3n2PP/8/6K1 w - -")
        b.printBoard()
        Search.searchPosition(b,4)

    }
    else{

        UCI.uciLoop()
    }

}

