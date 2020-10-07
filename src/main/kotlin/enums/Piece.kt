package enums

/**
 * Enum for the pieces. white pieces are going to be capital letters, black letters, original letters
 */
enum class Piece(val value: Int) {

    P(100),
    N(300),
    B(350),
    R(500),
    Q(1000),
    K(10000),
    p(-100),
    n(-300),
    b(-350),
    r(-500),
    q(-1000),
    k(-10000);

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