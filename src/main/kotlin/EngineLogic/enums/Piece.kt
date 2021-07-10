package EngineLogic.enums

/**
 * Enum for the pieces. white pieces are going to be capital letters, black letters, original letters
 */
enum class Piece(val openingValue: Int,val endGameValue: Int) {

    P(openingValue = 82,endGameValue = 94),
    N(openingValue = 337,endGameValue = 281),
    B(openingValue = 365,endGameValue = 297),
    R(openingValue = 477,endGameValue = 512),
    Q(openingValue = 1025,endGameValue = 936),
    K(openingValue = 12000, endGameValue = 12000),
    p(openingValue = -82, endGameValue = -94),
    n(openingValue = -337, endGameValue = -281),
    b(openingValue = -365, endGameValue = -297),
    r(openingValue = -477, endGameValue = -512),
    q(openingValue = -1025, endGameValue = -936),
    k(openingValue = -12000, endGameValue = -12000);

//    override fun toString(): String {
//        return unicodeChars.getOrDefault(this,".")
//    }


    companion object {
        /**
         * unicode chars not working on windows.
         */
//        val unicodeChars = mapOf<Piece,String>(
//           P to  "♟",
//            N to "♞",
//            B to "♝",
//            R to "♜",
//            Q to "♛",
//            K to "♚",
//            p to "♙",
//            n to "♘",
//            b to "♗",
//            r to "♖",
//            q to "♕",
//            k to "♔"
//        )

        val whitePieces = arrayOf(P, N, B, R, Q, K)
        val blackPieces = arrayOf(p, n, b, r, q, k)
        val allPieces = arrayOf(P, N, B, R, Q, K, p, n, b, r, q, k)
        val promotedPiecesMap = mapOf<Piece?,Int>(
            Q to Q.ordinal,
            R to R.ordinal,
            B to B.ordinal,
            N to N.ordinal,
            q to q.ordinal,
            r to r.ordinal,
            b to b.ordinal,
            n to n.ordinal
        )
        fun convertIndexToPiece(i: Int): Piece? {
            return when (i) {
                0 -> P
                1 -> N
                2 -> B
                3 -> R
                4 -> Q
                5 -> K
                6 -> p
                7 -> n
                8 -> b
                9 -> r
                10 -> q
                11 -> k
                else -> null
            }
        }

        fun convertCharToPiece(i: Char): Piece? {
            return when (i) {
                'P' -> P
                'N' -> N
                'B' -> B
                'R' -> R
                'Q' -> Q
                'K' -> K
                'p' -> p
                'n' -> n
                'b' -> b
                'r' -> r
                'q' -> q
                'k' -> k
                else -> null
            }
        }
    }

}