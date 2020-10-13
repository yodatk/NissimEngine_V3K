import enums.Piece
import enums.Square

/**
 * Defining zorbist keys for tranportation tables and hashing
 */
@ExperimentalUnsignedTypes
object ZorbistKeys {

    val HASH_FLAG_EXACT = 0
    val HASH_FLAG_ALPHA = 1
    val HASH_FLAG_BETA = 2

    val HASH_TABLE_SIZE = 0x400000

    data class TT(var hashKey:ULong,var depth: Int,var flag:Int,var score:Int)

    var pieceKeys :Array<Array<ULong>> = Array(12) { Array(64) { 0UL} }

    var enpassantKeys :Array<ULong> =  Array(64) { 0UL}

    var castleKeys :Array<ULong> =  Array(16) { 0UL}

    var sideKey : ULong = 0UL

    var hashTable:Array<TT> = Array(HASH_TABLE_SIZE) {TT(0UL,0,0,0)}

    fun clearHashTable() {
        for (tt in hashTable){
            tt.hashKey = 0UL
            tt.depth = 0
            tt.flag = 0
            tt.score = 0
        }
    }


    fun initRandomKeys() {
        RandomNumbers.uIntRandomState = 1804289383U

        // Init pieces keys
        for(piece in Piece.allPieces.indices){
            for(square in 0..63){
                pieceKeys[piece][square] = RandomNumbers.getRandomULongNumber()
            }
        }

        for (square in 0..63){
            enpassantKeys[square] = RandomNumbers.getRandomULongNumber()
        }

        for(i in 0..15){
            castleKeys[i] = RandomNumbers.getRandomULongNumber()
        }

        sideKey = RandomNumbers.getRandomULongNumber()

    }

}