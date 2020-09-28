
@ExperimentalUnsignedTypes
fun getBit(board: ULong, square: Int): ULong = (board and (1UL shl square))

@ExperimentalUnsignedTypes
fun setBitOn(board: ULong, square: Int): ULong {
    return (board or (1UL shl square))
}

@ExperimentalUnsignedTypes
fun setBitOff(board: ULong, square: Int): ULong {
    // (get_bit(bitboard, square) ? bitboard ^= (1ULL << square) : 0)
    return if (getBit(board, square) != 0UL) {
        board xor (1UL shl square)
    } else {
        0UL
    }
}

@ExperimentalUnsignedTypes
fun printBitboard(board: ULong) {
    println()
    for (rank in 0..7) {
        //for each rank
        for (file in 0..7) {
            //for each file
            val square = rank * 8 + file
            if (file == 0) {
                //printing ranks
                print(" ${8 - rank} ")
            }
            //printing '1' or '0' bit
            print(" ${if (getBit(board, square) != 0UL) 1 else 0}")
        }
        println()


    }
    //printing files
    println("\n    A B C D E F G H\n")
    println("    Bitboard: ${board}")
}