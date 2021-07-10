
import enums.Square

/**
 * ================================================
 *  BitBoard Manipulation
 *  @author: Tomer Gonen
 * ================================================
 */

object BitBoard {

    /***
     * get the chosen bit of the wanted square from the given BitBoard
     * @return ULong of the given bit
     */
    @JvmStatic
    fun getBit(board: ULong,square: Square): ULong = (board and (1UL shl square.ordinal))


    /***
     * get the chosen bit of the wanted square from the given BitBoard
     * @return ULong of the given bit
     */
    @JvmStatic
    fun getBit(board: ULong,square: Int): ULong = (board and (1UL shl square))

    /***
     * setting on bit in the given square
     */
    @JvmStatic
    fun setBitOn(board: ULong, square: Square) = (board or (1UL shl square.ordinal))


    /***
     * setting on bit in the given square
     */
    @JvmStatic
    fun setBitOn(board: ULong, square: Int) = (board or (1UL shl square))

    /**
     * setting off bit in given square if possible
     */
    @JvmStatic
    fun setBitOff(board:ULong ,square: Square) = board and (1UL shl square.ordinal).inv()

    /**
     * setting off bit in given square if possible
     */
    @JvmStatic
    fun setBitOff(board:ULong ,square: Int) = board and (1UL shl square).inv()


    @JvmStatic
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

                print(" ${if (getBit(board,Square.fromIntegerToSquare(square)!!) != 0UL) 1 else 0}")
            }
            println()


        }
        //printing files
        println("\n    A B C D E F G H\n")
        println("    Bitboard: $board")
    }

    @JvmStatic
    fun countBits(board:ULong): Int {
        var counter = 0
        var copyBoard = board
        while(copyBoard !=0UL){
            counter++
            copyBoard = copyBoard and (copyBoard-1UL)
        }
        return counter
    }
    @JvmStatic
    fun getLSB(board:ULong): Int {
        return if(board!=0UL){
            countBits((board and (0UL-board)) -1UL)
        } else{
            //empty board -> does not have lsb
            -1
        }
    }
}