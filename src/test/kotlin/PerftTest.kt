import enums.FENDebugConstants
import kotlin.test.assertEquals
import kotlin.test.Test


@ExperimentalUnsignedTypes
internal class PerftTest {


    @ExperimentalUnsignedTypes
    data class PerftTestResult(
        var nodes: ULong = 0UL,
        var captures: ULong = 0UL,
        var enPassant: ULong = 0UL,
        var castle: ULong = 0UL,
        var promotions: ULong = 0UL,
        var checks: ULong = 0UL,
        var checkMate: ULong = 0UL
    ) {
        override fun equals(other: Any?): Boolean {
            if (other is PerftTestResult) {
                return this.nodes == other.nodes
                        && this.captures == other.captures
                        && this.enPassant == other.enPassant
                        && this.castle == other.castle

            }
            return false
        }

        override fun hashCode(): Int {
            var result = nodes.hashCode()
            result = 31 * result + captures.hashCode()
            result = 31 * result + enPassant.hashCode()
            result = 31 * result + castle.hashCode()
            result = 31 * result + promotions.hashCode()
            result = 31 * result + checks.hashCode()
            result = 31 * result + checkMate.hashCode()
            return result
        }
    }

    private val resultsForStartPosition: Array<PerftTestResult> = arrayOf(
        PerftTestResult(nodes = 20UL),
        PerftTestResult(nodes = 400UL),
        PerftTestResult(nodes = 8902UL, captures = 34UL, checks = 12UL),
        PerftTestResult(nodes = 197281UL, captures = 1576UL, checks = 469UL, checkMate = 8UL),
        PerftTestResult(nodes = 4865609UL, captures = 82719UL, enPassant = 258UL, checks = 27351UL, checkMate = 347UL),
        PerftTestResult(
            nodes = 119060324UL,
            captures = 2812008UL,
            enPassant = 5248UL,
            checks = 809099UL,
            checkMate = 10828UL
        )

    )

    private val resultsForTrickyPosition: Array<PerftTestResult> = arrayOf(
        PerftTestResult(nodes = 48UL, captures = 8UL, castle = 2UL),
        PerftTestResult(nodes = 2039UL, captures = 351UL, enPassant = 1UL, castle = 91UL, checks = 3UL),
        PerftTestResult(
            nodes = 97862UL,
            captures = 17102UL,
            enPassant = 45UL,
            castle = 3162UL,
            checks = 993UL,
            checkMate = 1UL
        ),
        PerftTestResult(
            nodes = 4085603UL,
            captures = 757163UL,
            enPassant = 1929UL,
            castle = 128013UL,
            promotions = 15172UL,
            checks = 25523UL,
            checkMate = 43UL
        ),
        PerftTestResult(
            nodes = 193690690UL,
            captures = 35043416UL,
            enPassant = 73365UL,
            castle = 4993637UL,
            promotions = 8392UL,
            checks = 3309887UL,
            checkMate = 30171UL
        )
        //PerftTestResult(nodes=119060324UL,captures = 2812008UL,enPassant = 5248UL,checks=809099UL,checkMate = 10828UL)

    )

    private val resultsForPosition3: Array<PerftTestResult> = arrayOf(
        PerftTestResult(nodes = 14UL, captures = 1UL, checks = 2UL),
        PerftTestResult(nodes = 191UL, captures = 14UL, checks = 10UL),
        PerftTestResult(nodes = 2812UL, captures = 209UL, enPassant = 2UL, checks = 267UL),
        PerftTestResult(nodes = 43238UL, captures = 3348UL, enPassant = 123UL, checks = 1680UL, checkMate = 17UL),
        PerftTestResult(nodes = 674624UL, captures = 52051UL, enPassant = 1165UL, checks = 52950UL),
        PerftTestResult(
            nodes = 11030083UL,
            captures = 940350UL,
            enPassant = 33325UL,
            promotions = 7552UL,
            checks = 452473UL,
            checkMate = 2733UL
        ),
        PerftTestResult(
            nodes = 178633661UL,
            captures = 14519036UL,
            enPassant = 294874UL,
            promotions = 140024UL,
            checks = 12797406UL,
            checkMate = 87UL
        )


    )


    var result = PerftTestResult(0UL, 0UL, 0UL, 0UL, 0UL)
    fun perftDriver(board: Board, depth: Int) {
        if (depth == 0) {
            result.nodes++
            return
        }
        val moveList = board.generateMoves()
        for (move in moveList) {


            val tempCastle = Board.castle
            val tempSide = Board.side
            val tempEnpassant = Board.enpassant
            val tempPieceBitboards = arrayOf(
                Board.pieceBitboards[0],
                Board.pieceBitboards[1], Board.pieceBitboards[2],
                Board.pieceBitboards[3],
                Board.pieceBitboards[4],
                Board.pieceBitboards[5],
                Board.pieceBitboards[6],
                Board.pieceBitboards[7],
                Board.pieceBitboards[8],
                Board.pieceBitboards[9],
                Board.pieceBitboards[10],
                Board.pieceBitboards[11]
            )

            val tempOccupanciesBitboards = arrayOf(
                Board.occupanciesBitboards[0],
                Board.occupanciesBitboards[1],
                Board.occupanciesBitboards[2],
            )
            val tempHashKey = Board.hashKey

            if (!board.makeMove(move)) {
                continue
            }

            if (depth == 1) {
                if (Moves.getCaptureFromMove(move = move)) {
                    result.captures++
                }
                if (Moves.getEnPassantFromMove(move)) {
                    result.enPassant++
                }
                if (Moves.getCastlingFromMove(move)) {
                    result.castle++
                }
                if (Moves.getPromotedFromMove(move) != null) {
                    result.promotions++
                }
            }
            perftDriver(board, depth - 1)

            board.copyOtherBoard(tempCastle,tempSide,tempEnpassant,tempPieceBitboards,tempOccupanciesBitboards,tempHashKey)
//            val hash_from_scratch = board.generateHashKey()
//            if(board.hashKey!=hash_from_scratch){
//                println("\nMAKE MOVE")
//                println("move: ${Moves.moveUCI(move)}")
//                board.printBoard()
//                println("hash key should be: ${hash_from_scratch}")
//                //readLine()
//            }
        }

    }

    fun runPerftTestOnGivenFEN(fen: String, initialDepth: Int): PerftTestResult {
        result = PerftTestResult()
        Attacks.initAll()
        Board.parseFEN(fen)
        Board.printBoard()
        //val movesList: List<Int> = b.generateMoves()
        val start = System.currentTimeMillis()
        perftDriver(Board, initialDepth)

        println("time that took for the test (ms): ${System.currentTimeMillis() - start}")
        println("Num of Nodes: ${result.nodes}")
        println("Num of Captures: ${result.captures}")
        println("Num of enPassant: ${result.enPassant}")
        println("Num of castles: ${result.castle}")
        println("Num of promotions: ${result.promotions}")
        return result

    }

    fun runPerftTestOnGivenFenWithInitialNodeInfo(fen: String, initialDepth: Int) {
        println(
            "PERFORMANCE TEST:\n" +
                    "\tfen: $fen\n\tdepth: $initialDepth"
        )
        result = PerftTestResult()
        Attacks.initAll()
        Board.parseFEN(fen)
        Board.printBoard()
        val movesList = Board.generateMoves()
        for (move in movesList) {
            val tempCastle = Board.castle
            val tempSide = Board.side
            val tempEnpassant = Board.enpassant
            val tempPieceBitboards = arrayOf(
                Board.pieceBitboards[0],
                Board.pieceBitboards[1], Board.pieceBitboards[2],
                Board.pieceBitboards[3],
                Board.pieceBitboards[4],
                Board.pieceBitboards[5],
                Board.pieceBitboards[6],
                Board.pieceBitboards[7],
                Board.pieceBitboards[8],
                Board.pieceBitboards[9],
                Board.pieceBitboards[10],
                Board.pieceBitboards[11]
            )

            val tempOccupanciesBitboards = arrayOf(
                Board.occupanciesBitboards[0],
                Board.occupanciesBitboards[1],
                Board.occupanciesBitboards[2],
            )
            val tempHashKey = Board.hashKey
            if (!Board.makeMove(move)) {
                continue
            }
            val cmmulative_nodes = result.nodes
            perftDriver(Board, depth = initialDepth - 1)
            val oldNodes = result.nodes - cmmulative_nodes
            Board.copyOtherBoard(tempCastle,tempSide,tempEnpassant,tempPieceBitboards,tempOccupanciesBitboards,tempHashKey)
            println("\t\tmove: ${Moves.moveUCI(move)} nodes: $oldNodes")


        }

    }

    @Test
    fun nodesTest(){
        runPerftTestOnGivenFenWithInitialNodeInfo(FENDebugConstants.TRICKY_POSITION.fen,1)

    }


    @Test
    fun perftTestStartPosition() {

        for (i in 1..6) {
            println("START POSITION, depth = $i")
            val result = runPerftTestOnGivenFEN(FENDebugConstants.START_POSITION.fen, i)
            assertEquals(resultsForStartPosition[i - 1], result) //(result,resultsForStartPosition[i-1])
        }

    }

    @Test
    fun perftTestTrickyPosition() {

        for (i in 1..5) {
            println("TRICKY POSITION, depth = $i")
            val result = runPerftTestOnGivenFEN(FENDebugConstants.TRICKY_POSITION.fen, i)
            assertEquals(resultsForTrickyPosition[i - 1], result)
        }
    }

    @Test
    fun perftTestPosition3() {
        for (i in 1..7) {
            println("POSITION 3, depth = $i")
            val result = runPerftTestOnGivenFEN(FENDebugConstants.POSITION3.fen, i)
            assertEquals(resultsForPosition3[i - 1], result)
        }
    }

    @Test
    fun tempTest() {
        runPerftTestOnGivenFenWithInitialNodeInfo(FENDebugConstants.TRICKY_POSITION.fen, 5)
    }
}