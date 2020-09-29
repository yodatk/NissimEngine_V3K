/**
 * ================================================
 *  BitBoard Manipulation
 *  @author: Tomer Gonen
 * ================================================
 */
@ExperimentalUnsignedTypes
class BitBoard {

    var board : ULong = 0UL

    constructor(board: ULong){
        this.board = board
    }

    /***
     * get the chosen bit of the wanted square from the given BitBoard
     * @return ULong of the given bit
     */
    fun getBit(square: Square): ULong = (board and (1UL shl square.bit))

    /***
     * setting on bit in the given square
     */
    fun setBitOn(square: Square) {
        board =  (board or (1UL shl square.bit))
    }

    /**
     * setting off bit in given square if possible
     */
    fun setBitOff(square: Square) {
         if (getBit(square) != 0UL) {
            board = board xor (1UL shl square.bit)
        }
    }


    fun printBitboard() {
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
                print(" ${if (getBit(Square.fromIntegerToSquare(square)!!) != 0UL) 1 else 0}")
            }
            println()


        }
        //printing files
        println("\n    A B C D E F G H\n")
        println("    Bitboard: ${board}")
    }
    fun bitwiseOR(other:BitBoard){
        this.board = this.board or other.board
    }
    fun bitwiseOR(other:ULong){
        this.board = this.board or other
    }

    fun bitwiseAnd(other:BitBoard){
        this.board = this.board and other.board
    }

    fun bitwiseAnd(other:ULong){
        this.board = this.board and other
    }

    fun countBits(): Int {
        var counter = 0;
        while(this.board!=0UL){
            counter++;
            this.bitwiseAnd(BitBoard(this.board-1UL))
        }
        return counter

    }

}