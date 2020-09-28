/**
 * Enum for all the squares in the chess board
"a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8"
"a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7"
"a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6"
"a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5"
"a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4"
"a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3"
"a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2"
"a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1"
 */

enum class Square(val bit: Int) {
    a8(0), b8(1), c8(2), d8(3), e8(4), f8(5), g8(6), h8(7),
    a7(8), b7(9), c7(10), d7(11), e7(12), f7(13), g7(14), h7(15),
    a6(16), b6(17), c6(18), d6(19), e6(20), f6(21), g6(22), h6(23),
    a5(24), b5(25), c5(26), d5(27), e5(28), f5(29), g5(30), h5(31),
    a4(32), b4(33), c4(34), d4(35), e4(36), f4(37), g4(38), h4(39),
    a3(40), b3(41), c3(42), d3(43), e3(44), f3(45), g3(46), h3(47),
    a2(48), b2(49), c2(50), d2(51), e2(52), f2(53), g2(54), h2(55),
    a1(56), b1(57), c1(58), d1(59), e1(60), f1(61), g1(62), h1(63);
    companion object{
        fun fromIntegerToSquare(toConvert: Int): Square? {
            return when (toConvert) {
                a8.bit -> a8
                b8.bit -> b8
                c8.bit -> c8
                d8.bit -> d8
                e8.bit -> e8
                f8.bit -> f8
                g8.bit -> g8
                h8.bit -> h8
                a7.bit -> a7
                b7.bit -> b7
                c7.bit -> c7
                d7.bit -> d7
                e7.bit -> e7
                f7.bit -> f7
                g7.bit -> g7
                h7.bit -> h7
                a6.bit -> a6
                b6.bit -> b6
                c6.bit -> c6
                d6.bit -> d6
                e6.bit -> e6
                f6.bit -> f6
                g6.bit -> g6
                h6.bit -> h6
                a5.bit -> a5
                b5.bit -> b5
                c5.bit -> c5
                d5.bit -> d5
                e5.bit -> e5
                f5.bit -> f5
                g5.bit -> g5
                h5.bit -> h5
                a4.bit -> a4
                b4.bit -> b4
                c4.bit -> c4
                d4.bit -> d4
                e4.bit -> e4
                f4.bit -> f4
                g4.bit -> g4
                h4.bit -> h4
                a3.bit -> a3
                b3.bit -> b3
                c3.bit -> c3
                d3.bit -> d3
                e3.bit -> e3
                f3.bit -> f3
                g3.bit -> g3
                h3.bit -> h3
                a2.bit -> a2
                b2.bit -> b2
                c2.bit -> c2
                d2.bit -> d2
                e2.bit -> e2
                f2.bit -> f2
                g2.bit -> g2
                h2.bit -> h2
                a1.bit -> a1
                b1.bit -> b1
                c1.bit -> c1
                d1.bit -> d1
                e1.bit -> e1
                f1.bit -> f1
                g1.bit -> g1
                h1.bit -> h1
                else -> {
                    println("NOT GOOD!")
                    null
                }

            }

        }
    }


}

