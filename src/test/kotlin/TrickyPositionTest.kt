import enums.FENDebugConstants
import enums.Piece
import enums.Square
import kotlin.test.*

internal class TrickyPositionTest {
    @ExperimentalUnsignedTypes
    @Test
    fun parseMoveStartPositionSuccessfulWhite() {
        Attacks.initAll()
        val results = Array<Long>(3) {0}
        for(i in 0..2){
            val startTime = System.currentTimeMillis()
            Board.parseFEN(FENDebugConstants.TRICKY_POSITION.fen)
            Board.printBoard()
            Search.searchPosition(10)
            val endTime = System.currentTimeMillis()
            results[i] = endTime - startTime
            ZorbistKeys.clearHashTable()

        }
        println("\t\tresult:")
        for(res in results){
            println(res)
        }
        println("average: ${(results[0]+results[1]+results[2])/3}")
    }
}