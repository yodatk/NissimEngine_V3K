import EngineLogic.Board
import EngineLogic.Attacks
import EngineLogic.Moves
import EngineLogic.UCI

import EngineLogic.enums.FENDebugConstants
import EngineLogic.enums.Piece
import EngineLogic.enums.Square
import kotlin.test.*


internal class UCITest {

    val possibleMovesForStartPositionWhite = mapOf(
        "e2e4" to  Moves.encodeMove(Square.e2, Square.e4, Piece.P,double=true),
        "d2d4" to Moves.encodeMove(Square.d2, Square.d4, Piece.P,double=true),
        "e2e3" to Moves.encodeMove(Square.e2, Square.e3, Piece.P),
        "g1f3" to Moves.encodeMove(Square.g1, Square.f3, Piece.N),
        "b1a3" to Moves.encodeMove(Square.b1, Square.a3, Piece.N),
    )

    val impossibleMovesForStartPositionWhite = arrayOf(
        "e2e5","a2a1","b1d2","g1e2",
    )

    val possibleMovesForStartPositionBlack = mapOf(
        "e7e5" to Moves.encodeMove(Square.e7, Square.e5, Piece.p,double=true),
        "d7d5" to Moves.encodeMove(Square.d7, Square.d5, Piece.p,double=true),
        "e7e6" to Moves.encodeMove(Square.e7, Square.e6, Piece.p),
        "g8f6" to Moves.encodeMove(Square.g8, Square.f6, Piece.n),
        "b8a6" to Moves.encodeMove(Square.b8, Square.a6, Piece.n),
    )

    val impossibleMovesForStartPositionBlack = arrayOf(
        "e2e5","a2a1","b1d2","g1e2",
    )

    val startPositionBlack = "rnbqkbnr/pppppppp/8/8/3P4/8/PPP1PPPP/RNBQKBNR b KQkq - 0 1 "

    val TRICKY_POSITION_WITH_ENPASSANT_AND_PROMOTION_WHITE = "r3k2r/pPppqpb1/bn2pnp1/2pPN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq c6 0 1 "

    val TRICKY_POSITION_WITH_ENPASSANT_AND_PROMOTION_BLACK = "r3k2r/ppppqpb1/bn2pnp1/2pPN3/Pp2P3/2N2Q1p/P1PBBPpP/R3K2R b KQkq a3 0 1 "

    val possibleMovesForTrickyPositionWhite = mapOf(
        "d5c6" to  Moves.encodeMove(Square.d5, Square.c6, Piece.P,capture = true,enpassant = true),
        "d5e6" to  Moves.encodeMove(Square.d5, Square.e6, Piece.P,capture = true),
        "b7a8q" to Moves.encodeMove(Square.b7, Square.a8, Piece.P,promoted = Piece.Q,capture = true),
        "b7a8r" to Moves.encodeMove(Square.b7, Square.a8, Piece.P,promoted = Piece.R,capture = true),
        "b7a8b" to Moves.encodeMove(Square.b7, Square.a8, Piece.P,promoted = Piece.B,capture = true),
        "b7a8n" to Moves.encodeMove(Square.b7, Square.a8, Piece.P,promoted = Piece.N,capture = true),
        "b7b8q" to Moves.encodeMove(Square.b7, Square.b8, Piece.P,promoted = Piece.Q),
        "b7b8r" to Moves.encodeMove(Square.b7, Square.b8, Piece.P,promoted = Piece.R),
        "b7b8b" to Moves.encodeMove(Square.b7, Square.b8, Piece.P,promoted = Piece.B),
        "b7b8n" to Moves.encodeMove(Square.b7, Square.b8, Piece.P,promoted = Piece.N),
    )

    val impossibleMovesForTrickyPositionWhite = arrayOf(
        "b7a8","b7b8","e1e2","e1d2",
    )

    val possibleMovesForTrickyPositionBlack = mapOf(
        "b4a3" to  Moves.encodeMove(Square.b4, Square.a3, Piece.p,capture = true,enpassant = true),
        "b4c3" to  Moves.encodeMove(Square.b4, Square.c3, Piece.p,capture = true),
        "g2h1q" to Moves.encodeMove(Square.g2, Square.h1, Piece.p,promoted = Piece.q,capture = true),
        "g2h1r" to Moves.encodeMove(Square.g2, Square.h1, Piece.p,promoted = Piece.r,capture = true),
        "g2h1b" to Moves.encodeMove(Square.g2, Square.h1, Piece.p,promoted = Piece.b,capture = true),
        "g2h1n" to Moves.encodeMove(Square.g2, Square.h1, Piece.p,promoted = Piece.n,capture = true),
        "g2g1q" to Moves.encodeMove(Square.g2, Square.g1, Piece.p,promoted = Piece.q),
        "g2g1r" to Moves.encodeMove(Square.g2, Square.g1, Piece.p,promoted = Piece.r),
        "g2g1b" to Moves.encodeMove(Square.g2, Square.g1, Piece.p,promoted = Piece.b),
        "g2g1n" to Moves.encodeMove(Square.g2, Square.g1, Piece.p,promoted = Piece.n),
    )

    val impossibleMovesForTrickyPositionBlack = arrayOf(
        "g2h1","g2g1","e8e7","e8d7",
    )


    @BeforeTest
    internal fun setUp() {
        Attacks.initAll()
    }

    fun runningSuccessfulTests(board: Board, mapOfMovesAndResults:Map<String,Int>) {
        for((stringMove,result) in mapOfMovesAndResults){
            val move:Int = UCI.parseMove(stringMove,board = board)
            Moves.printMove(move)
            Moves.printMove(result)
            assertEquals(expected = result,actual = move)
        }
    }

    fun runningFailedTests(board: Board, arrayOfStringMoves: Array<String>) {
        for(i in arrayOfStringMoves.indices){
            val move:Int = UCI.parseMove(arrayOfStringMoves[i],board = board)

            assertEquals(expected = 0,actual = move)
        }
    }

    @Test
    fun parseMoveStartPositionSuccessfulWhite() {
        runningSuccessfulTests(Board.createStartBoard(),possibleMovesForStartPositionWhite)
    }

    @Test
    fun parseMoveStartPositionFailWhite() {
        runningFailedTests(Board.createStartBoard(),impossibleMovesForStartPositionWhite)
    }

    @Test
    fun parseMoveStartPositionSuccessfulBlack() {
        runningSuccessfulTests(Board(startPositionBlack),possibleMovesForStartPositionBlack)
    }

    @Test
    fun parseMoveStartPositionFailBlack() {
        runningFailedTests(Board(startPositionBlack),impossibleMovesForStartPositionBlack)
    }
    @Test
    fun parseMoveTrickyPositionSuccessfulWhite() {
        runningSuccessfulTests(Board(TRICKY_POSITION_WITH_ENPASSANT_AND_PROMOTION_WHITE),possibleMovesForTrickyPositionWhite)
    }

    @Test
    fun parseMoveTrickyPositionFailWhite() {
        runningFailedTests(Board(TRICKY_POSITION_WITH_ENPASSANT_AND_PROMOTION_WHITE),impossibleMovesForTrickyPositionWhite)
    }

    @Test
    fun parseMoveTrickyPositionSuccessfulBlack() {
        runningSuccessfulTests(Board(TRICKY_POSITION_WITH_ENPASSANT_AND_PROMOTION_BLACK),possibleMovesForTrickyPositionBlack)
    }

    @Test
    fun parseMoveTrickyPositionFailBlack() {
        runningFailedTests(Board(TRICKY_POSITION_WITH_ENPASSANT_AND_PROMOTION_BLACK),impossibleMovesForTrickyPositionBlack)
    }

    @Test
    fun parsePosition(){
        assert(Board.createStartBoard().equals(UCI.parsePosition("position startpos")))
        assert(Board(FENDebugConstants.TRICKY_POSITION.fen).equals(UCI.parsePosition("position fen ${FENDebugConstants.TRICKY_POSITION.fen}")))
        assert(Board("rnbqkbnr/pppp1ppp/8/4p3/4P3/8/PPPP1PPP/RNBQKBNR w KQkq e6 0 2").equals(UCI.parsePosition("position startpos moves e2e4 e7e5")))
        try {
            UCI.parsePosition("pos startpos moves e2e4 e7e5")
            fail("supposed to throw UCIException because of invalid command 'pos startpos moves e2e4 e7e5'")
        }catch (e: UCI.UCIException){

        }
    }

}