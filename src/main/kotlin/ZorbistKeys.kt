import enums.Piece
import enums.Square

/**
 * Defining zorbist keys for tranportation tables and hashing
 */
@ExperimentalUnsignedTypes
object ZorbistKeys {

    /**
     * Flag for finding exact Principle variation node
     */
    val HASH_FLAG_EXACT = 0
    /**
     * Flag for Fail low node
     */
    val HASH_FLAG_ALPHA = 1
    /**
     * Flag for Fail high node
     */
    val HASH_FLAG_BETA = 2

    /**
     * Value for unknown entry
     */
    val UNKOWN_VALUE = 100000

    /**
     * default size of entry
     */
    val HASH_TABLE_SIZE = 800000

    /**
     * Class to represent an entry in Transposition table
     */
    data class TT(var hashKey:ULong,var depth: Int,var flag:Int,var score:Int)

    /**
     * Keys for pieces according to their position on board for hashing purposes
     */
    var pieceKeys :Array<Array<ULong>> = Array(12) { Array(64) { 0UL} }

    /**
     * Keys for enpassant position on board for hashing purposes
     */
    var enpassantKeys :Array<ULong> =  Array(64) { 0UL}

    /**
     * Keys for castling rights for hashing purposes
     */
    var castleKeys :Array<ULong> =  Array(16) { 0UL}

    /**
     * Key for black side only for hashing purposes (if white -> don't add to hash)
     */
    var sideKey : ULong = 0UL

    /**
     * Transposition table
     */
    var hashTable:Array<TT> = Array(HASH_TABLE_SIZE) {TT(0UL,0,0,0)}

    /**
     * Cleaning the hash table
     */
    fun clearHashTable() {
        for (tt in hashTable){
            tt.hashKey = 0UL
            tt.depth = 0
            tt.flag = 0
            tt.score = 0
        }
    }

    fun generatingTableIndex(hashKey: ULong) = hashKey.rem(HASH_TABLE_SIZE.toULong()).toInt()

    /**
     * Read data from TT according to given parameters
     * @param hashKey:  ULong represents the hash key of the current board
     * @param alpha:    Int represents the alpha value of the current search
     * @param beta:     Int represents the beta value of the current search
     * @param depth:    Int represent the wanted depth
     * @return Int value represents the value that is saved for the given position in TT table. if not found, return 100000
     */
    fun readHashData(hashKey: ULong,alpha:Int,beta:Int,depth:Int,ply:Int) : Int{
        val currentEntry = hashTable[generatingTableIndex(hashKey)]
        if(currentEntry.hashKey == hashKey){
            // checking that the given hash key matches the entry in table
            if(currentEntry.depth >= depth){
                // checking the depth in entry is deep enough for wanted depth

                var score = currentEntry.score

                // retrieve score independent from the actual path
                // from root node (position) to the current node(position)
                if(score < -Search.MATE_SCORE) {
                    score += ply
                }
                if(score > Search.MATE_SCORE){
                    score -= ply
                }


                if(currentEntry.flag == HASH_FLAG_EXACT){
                    // return exact score
                    return score
                }
                if(currentEntry.flag == HASH_FLAG_ALPHA && score <= alpha){
                    // fail low node
                    return alpha
                }
                if(currentEntry.flag == HASH_FLAG_BETA && score >= beta){
                    // fail high node
                    return beta
                }

            }
        }
        // couldn't find something relevant in hash table
        return UNKOWN_VALUE
    }

    /**
     * write data to TT according to given parameters
     * @param hashKey:      ULong represents the hash key of the current board
     * @param score:        Int represent the current found value of the position
     * @param depth:        Int represent the wanted depth
     * @param hashFlag:     Int represent the position category(PV,Beta or Alpha)
     */
    fun writeEntry(hashKey: ULong,_score: Int,depth: Int,hashFlag : Int,ply:Int){
        var score = _score
        val newEntry = hashTable[generatingTableIndex(hashKey)]


        // store score independent from the actual path from root to current position(node)
        if(score < -Search.MATE_SCORE){
            score -= ply
        }
        if(score > Search.MATE_SCORE){
            score += ply
        }

        newEntry.hashKey = hashKey
        newEntry.score = score
        newEntry.flag = hashFlag
        newEntry.depth = depth
    }


    /**
     * Init Zorbiest Keys for all different arrays
     */
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