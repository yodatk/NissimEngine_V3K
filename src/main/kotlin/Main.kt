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
        val b = Board.createStartBoard()
        b.printBoard()

    }
    else{
        Attacks.initAll()
        UCI.uciLoop()
    }

}

