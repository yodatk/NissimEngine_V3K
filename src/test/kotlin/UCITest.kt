import enums.FENDebugConstants
import enums.Piece
import enums.Square
import kotlin.test.Test
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

@ExperimentalUnsignedTypes
internal class UCITest {



    val possibleMovesStartPositionString = arrayOf(
        "e2e4","d2d4","g1f3","b1a3"
    )


    val possibleMovesForStartPosition = arrayOf(
        Moves.encodeMove(Square.e2,Square.e4, Piece.P),
        Moves.encodeMove(Square.d2,Square.d4, Piece.P),
        Moves.encodeMove(Square.g1,Square.f3, Piece.N),
        Moves.encodeMove(Square.b1,Square.a3, Piece.N),
    )

    val impossibleMovesForStartPosition = arrayOf(
        "e2e5","a2a1","b1d2","g1e2",
    )

    @BeforeTest
    internal fun setUp() {
        Attacks.initAll()
    }

    @Test
    fun parseMoveStartPositionSuccessful() {
        val board = Board(FENDebugConstants.START_POSITION.fen)
        for(i in possibleMovesStartPositionString.indices){
            val move:Int = UCI.parseMove(possibleMovesStartPositionString[i],board = board)
            assertEquals(possibleMovesForStartPosition[i],move)
        }
    }

    @Test
    fun parseMoveStartPositionFail() {
        val board = Board(FENDebugConstants.START_POSITION.fen)
        for(i in possibleMovesStartPositionString){
            val move:Int = UCI.parseMove(i,board = board)
            assertEquals(0,move)
        }
    }
}