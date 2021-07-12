package EngineLogic.enums

/**
 * Enum for all the squares in the chess board
"a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8"
"a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7"
"a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6"
"a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5"
"a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4"
"a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3"
"a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2"
"a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1","no_square" = null
 */

enum class Square() {
    a8, b8, c8, d8, e8, f8, g8, h8,
    a7, b7, c7, d7, e7, f7, g7, h7,
    a6, b6, c6, d6, e6, f6, g6, h6,
    a5, b5, c5, d5, e5, f5, g5, h5,
    a4, b4, c4, d4, e4, f4, g4, h4,
    a3, b3, c3, d3, e3, f3, g3, h3,
    a2, b2, c2, d2, e2, f2, g2, h2,
    a1, b1, c1, d1, e1, f1, g1, h1, NO_SQUARE;

    /**no_square = null*/
    companion object {
        fun fromIntegerToSquare(toConvert: Int): Square? {
            return when (toConvert) {
                a8.ordinal -> a8
                b8.ordinal -> b8
                c8.ordinal -> c8
                d8.ordinal -> d8
                e8.ordinal -> e8
                f8.ordinal -> f8
                g8.ordinal -> g8
                h8.ordinal -> h8
                a7.ordinal -> a7
                b7.ordinal -> b7
                c7.ordinal -> c7
                d7.ordinal -> d7
                e7.ordinal -> e7
                f7.ordinal -> f7
                g7.ordinal -> g7
                h7.ordinal -> h7
                a6.ordinal -> a6
                b6.ordinal -> b6
                c6.ordinal -> c6
                d6.ordinal -> d6
                e6.ordinal -> e6
                f6.ordinal -> f6
                g6.ordinal -> g6
                h6.ordinal -> h6
                a5.ordinal -> a5
                b5.ordinal -> b5
                c5.ordinal -> c5
                d5.ordinal -> d5
                e5.ordinal -> e5
                f5.ordinal -> f5
                g5.ordinal -> g5
                h5.ordinal -> h5
                a4.ordinal -> a4
                b4.ordinal -> b4
                c4.ordinal -> c4
                d4.ordinal -> d4
                e4.ordinal -> e4
                f4.ordinal -> f4
                g4.ordinal -> g4
                h4.ordinal -> h4
                a3.ordinal -> a3
                b3.ordinal -> b3
                c3.ordinal -> c3
                d3.ordinal -> d3
                e3.ordinal -> e3
                f3.ordinal -> f3
                g3.ordinal -> g3
                h3.ordinal -> h3
                a2.ordinal -> a2
                b2.ordinal -> b2
                c2.ordinal -> c2
                d2.ordinal -> d2
                e2.ordinal -> e2
                f2.ordinal -> f2
                g2.ordinal -> g2
                h2.ordinal -> h2
                a1.ordinal -> a1
                b1.ordinal -> b1
                c1.ordinal -> c1
                d1.ordinal -> d1
                e1.ordinal -> e1
                f1.ordinal -> f1
                g1.ordinal -> g1
                h1.ordinal -> h1
                else -> {
                    null
                }

            }

        }
    }


}

