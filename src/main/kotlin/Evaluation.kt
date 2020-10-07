import enums.Color
import enums.Piece
import enums.Square


@ExperimentalUnsignedTypes
object Evaluation {

    val pawnPositionalScore = arrayOf(
        90, 90, 90, 90, 90, 90, 90, 90,
        30, 30, 30, 40, 40, 30, 30, 30,
        20, 20, 20, 30, 30, 30, 20, 20,
        10, 10, 10, 20, 20, 10, 10, 10,
        5, 5, 10, 20, 20, 5, 5, 5,
        0, 0, 0, 5, 5, 0, 0, 0,
        0, 0, 0, -10, -10, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0
    )

    val knightPositionalScore = arrayOf(
        -5, 0, 0, 0, 0, 0, 0, -5,
        -5, 0, 0, 10, 10, 0, 0, -5,
        -5, 5, 20, 20, 20, 20, 5, -5,
        -5, 10, 20, 30, 30, 20, 10, -5,
        -5, 10, 20, 30, 30, 20, 10, -5,
        -5, 5, 20, 10, 10, 20, 5, -5,
        -5, 0, 0, 0, 0, 0, 0, -5,
        -5, -10, 0, 0, 0, 0, -10, -5
    )

    val bishopPositionalScore = arrayOf(
        0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 10, 10, 0, 0, 0,
        0, 0, 10, 20, 20, 10, 0, 0,
        0, 0, 10, 20, 20, 10, 0, 0,
        0, 10, 0, 0, 0, 0, 10, 0,
        0, 30, 0, 0, 0, 0, 30, 0,
        0, 0, -10, 0, 0, -10, 0, 0
    )

    val rookPositionalScore = arrayOf(
        50, 50, 50, 50, 50, 50, 50, 50,
        50, 50, 50, 50, 50, 50, 50, 50,
        0, 0, 10, 20, 20, 10, 0, 0,
        0, 0, 10, 20, 20, 10, 0, 0,
        0, 0, 10, 20, 20, 10, 0, 0,
        0, 0, 10, 20, 20, 10, 0, 0,
        0, 0, 10, 20, 20, 10, 0, 0,
        0, 0, 0, 20, 20, 0, 0, 0
    )

    val kingPositionalScore = arrayOf(
        0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 5, 5, 5, 5, 0, 0,
        0, 5, 5, 10, 10, 5, 5, 0,
        0, 5, 10, 20, 20, 10, 5, 0,
        0, 5, 10, 20, 20, 10, 5, 0,
        0, 0, 5, 10, 10, 5, 0, 0,
        0, 5, 5, -5, -5, 0, 5, 0,
        0, 0, 5, 0, -15, 0, 10, 0
    )

    val mirrorScores = arrayOf(

        Square.a1.ordinal,
        Square.b1.ordinal,
        Square.c1.ordinal,
        Square.d1.ordinal,
        Square.e1.ordinal,
        Square.f1.ordinal,
        Square.g1.ordinal,
        Square.h1.ordinal,


        Square.a2.ordinal,
        Square.b2.ordinal,
        Square.c2.ordinal,
        Square.d2.ordinal,
        Square.e2.ordinal,
        Square.f2.ordinal,
        Square.g2.ordinal,
        Square.h2.ordinal,


        Square.a3.ordinal,
        Square.b3.ordinal,
        Square.c3.ordinal,
        Square.d3.ordinal,
        Square.e3.ordinal,
        Square.f3.ordinal,
        Square.g3.ordinal,
        Square.h3.ordinal,


        Square.a4.ordinal,
        Square.b4.ordinal,
        Square.c4.ordinal,
        Square.d4.ordinal,
        Square.e4.ordinal,
        Square.f4.ordinal,
        Square.g4.ordinal,
        Square.h4.ordinal,


        Square.a5.ordinal,
        Square.b5.ordinal,
        Square.c5.ordinal,
        Square.d5.ordinal,
        Square.e5.ordinal,
        Square.f5.ordinal,
        Square.g5.ordinal,
        Square.h5.ordinal,


        Square.a6.ordinal,
        Square.b6.ordinal,
        Square.c6.ordinal,
        Square.d6.ordinal,
        Square.e6.ordinal,
        Square.f6.ordinal,
        Square.g6.ordinal,
        Square.h6.ordinal,


        Square.a7.ordinal,
        Square.b7.ordinal,
        Square.c7.ordinal,
        Square.d7.ordinal,
        Square.e7.ordinal,
        Square.f7.ordinal,
        Square.g7.ordinal,
        Square.h7.ordinal,


        Square.a8.ordinal,
        Square.b8.ordinal,
        Square.c8.ordinal,
        Square.d8.ordinal,
        Square.e8.ordinal,
        Square.f8.ordinal,
        Square.g8.ordinal,
        Square.h8.ordinal,

        )



    fun evaluate(board: Board): Int {
        var score = 0
        var currBitboard: ULong
        var square: Int
        for (p in Piece.allPieces) {
            currBitboard = board.pieceBitboards[p.ordinal]
            while (currBitboard != 0UL) {
                square = BitBoard.getLSB(currBitboard)
                score += p.value
                when (p) {
                    //WHITE
                    Piece.P -> score += pawnPositionalScore[square]
                    Piece.N -> score += knightPositionalScore[square]
                    Piece.B -> score += bishopPositionalScore[square]
                    Piece.R -> score += rookPositionalScore[square]
                    Piece.K -> score += kingPositionalScore[square]

                    //BLACK
                    Piece.p -> score -= pawnPositionalScore[mirrorScores[square]]
                    Piece.n -> score -= knightPositionalScore[mirrorScores[square]]
                    Piece.b -> score -= bishopPositionalScore[mirrorScores[square]]
                    Piece.r -> score -= rookPositionalScore[mirrorScores[square]]
                    Piece.k -> score -= kingPositionalScore[mirrorScores[square]]

                    else -> break
                }
                currBitboard = BitBoard.setBitOff(currBitboard, Square.fromIntegerToSquare(square)!!)
            }
        }
        return if (board.side == Color.WHITE) score else -score
    }
}