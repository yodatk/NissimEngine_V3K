import enums.FENDebugConstants
import kotlin.test.assertEquals
import kotlin.test.Test

@ExperimentalUnsignedTypes
internal class PerftTest {


    var nodes = 0UL

    fun perftDriver(board:Board,depth:Int){
        if(depth == 0){
            nodes++
            return
        }
        val moveList = board.generateMoves()
        for (move in moveList){
            val copy = Board(board)
            if(!board.makeMove(move)){
                continue
            }
            perftDriver(board,depth-1)
            board.copyOtherBoard(copy)
        }

    }

    fun runPerftTestOnGivenFEN(fen:String,initialDepth:Int): ULong {
        nodes = 0UL
        Attacks.initAll()
        val b = Board(fen)
        b.printBoard()
        //val movesList: List<Int> = b.generateMoves()
        val start = System.currentTimeMillis()
        perftDriver(b,initialDepth)

        println("time that took for the test (ms): ${System.currentTimeMillis() - start}")
        println("Num of Nodes: $nodes")
        return nodes

    }


    @Test
    fun perftTestStartPosition() {
        println("START POSITION, depth = 1")
        assert(runPerftTestOnGivenFEN(FENDebugConstants.START_POSITION.fen,1) == 20UL)
        println("START POSITION, depth = 2")
        assert(runPerftTestOnGivenFEN(FENDebugConstants.START_POSITION.fen,2) == 400UL)
        println("START POSITION, depth = 3")
        assert(runPerftTestOnGivenFEN(FENDebugConstants.START_POSITION.fen,3) == 8902UL)
        println("START POSITION, depth = 4")
        assert(runPerftTestOnGivenFEN(FENDebugConstants.START_POSITION.fen,4) == 197281UL)
        println("START POSITION, depth = 5")
        assert(runPerftTestOnGivenFEN(FENDebugConstants.START_POSITION.fen,5) == 4865609UL)
        println("START POSITION, depth = 6")
        assert(runPerftTestOnGivenFEN(FENDebugConstants.START_POSITION.fen,6) == 119060324UL)

    }

    @Test
    fun perftTestTrickyPosition() {
        println("TRICKY POSITION, depth = 1")
        assert(runPerftTestOnGivenFEN(FENDebugConstants.TRICKY_POSITION.fen,1) == 48UL)
        println("TRICKY POSITION, depth = 2")
        assert(runPerftTestOnGivenFEN(FENDebugConstants.TRICKY_POSITION.fen,2) == 2039UL)
        println("TRICKY POSITION, depth = 3")
        assert(runPerftTestOnGivenFEN(FENDebugConstants.TRICKY_POSITION.fen,3) == 97862UL)
        println("TRICKY POSITION, depth = 4")
        assert(runPerftTestOnGivenFEN(FENDebugConstants.TRICKY_POSITION.fen,4) == 4085603UL)
        println("TRICKY POSITION, depth = 5")
        assert(runPerftTestOnGivenFEN(FENDebugConstants.TRICKY_POSITION.fen,5) == 193690690UL)
    }

    @Test
    fun perftTestPosition3() {
        println("POSITION3, depth = 1")
        assert(runPerftTestOnGivenFEN(FENDebugConstants.POSITION3.fen,1) == 14UL)
        println("POSITION3, depth = 2")
        assert(runPerftTestOnGivenFEN(FENDebugConstants.POSITION3.fen,2) == 191UL)
        println("POSITION3, depth = 3")
        assert(runPerftTestOnGivenFEN(FENDebugConstants.POSITION3.fen,3) == 2812UL)
        println("POSITION3, depth = 4")
        assert(runPerftTestOnGivenFEN(FENDebugConstants.POSITION3.fen,4) == 43238UL)
        println("POSITION3, depth = 5")
        assert(runPerftTestOnGivenFEN(FENDebugConstants.POSITION3.fen,5) == 674624UL)
        println("POSITION3, depth = 6")
        assert(runPerftTestOnGivenFEN(FENDebugConstants.POSITION3.fen,6) == 11030083UL)
        println("POSITION3, depth = 7")
        assert(runPerftTestOnGivenFEN(FENDebugConstants.POSITION3.fen,7) == 178633661UL)
    }
}