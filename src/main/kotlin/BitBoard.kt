
import enums.Square

/**
 * ================================================
 *  BitBoard Manipulation
 *  @author: Tomer Gonen
 * ================================================
 */
@ExperimentalUnsignedTypes
object BitBoard {

//    var board : ULong = 0UL
//
//    constructor(){
//        this.board = 0UL
//    }
//
//    constructor(board: ULong){
//        this.board = board
//    }
//
//    constructor(other: BitBoard){
//        this.board = other.board
//    }

    /***
     * get the chosen bit of the wanted square from the given BitBoard
     * @return ULong of the given bit
     */
    fun getBit(board: ULong,square: Square): ULong = (board and (1UL shl square.ordinal))

    /***
     * setting on bit in the given square
     */
    fun setBitOn(board: ULong, square: Square) = (board or (1UL shl square.ordinal))

    /**
     * setting off bit in given square if possible
     */
    fun setBitOff(board:ULong ,square: Square) = board and (1UL shl square.ordinal).inv()



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

    fun countBits(board:ULong): Int {
        var counter = 0
        var copyBoard = board
        while(copyBoard !=0UL){
            counter++
            copyBoard = copyBoard and (copyBoard-1UL)
        }
        return counter
    }

    fun getLSB(board:ULong): Int {
        return if(board!=0UL){
            countBits((board and (0UL-board)) -1UL)
        } else{
            //empty board -> does not have lsb
            -1
        }
    }
}