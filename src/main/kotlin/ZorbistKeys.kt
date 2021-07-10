import enums.Piece

/**
 * Defining zorbist keys for tranportation tables and hashing
 */
object ZorbistKeys {

    /**
     * Flag for finding exact Principle variation node
     */
    const val HASH_FLAG_EXACT = 0
    /**
     * Flag for Fail low node
     */

    const val HASH_FLAG_ALPHA = 1
    /**
     * Flag for Fail high node
     */
    const val HASH_FLAG_BETA = 2

    /**
     * Value for unknown entry
     */

    const val UNKNOWN_VALUE = 100000


    const val CLASS_TT_SIZE = 20

    /**
     * Class to represent an entry in Transposition table
     */
    data class TT(var hashKey:ULong,var depth: Int,var flag:Int,var score:Int)

    /**
     * Keys for pieces according to their position on board for hashing purposes
     */
    @JvmStatic
    var pieceKeys :Array<Array<ULong>> = Array(12) { Array(64) { 0UL} }

    /**
     * Keys for enpassant position on board for hashing purposes
     */
    @JvmStatic
    var enpassantKeys :Array<ULong> =  Array(64) { 0UL}

    /**
     * Keys for castling rights for hashing purposes
     */
    @JvmStatic
    var castleKeys :Array<ULong> =  Array(16) { 0UL}

    /**
     * Key for black side only for hashing purposes (if white -> don't add to hash)
     */
    @JvmStatic
    var sideKey : ULong = 0UL

    @JvmStatic
    var hashEntries = 0

    /**
     * Transposition table
     */
    @JvmStatic
    //var hashTable:Array<TT>? = Array(hashEntries) {TT(0UL,0,0,0)}
    var hashTable : HashMap<ULong,TT> = HashMap()


    /**
     * Cleaning the hash table
     */
    @JvmStatic
    fun clearHashTable() {
        hashTable = HashMap()

    }
//    @JvmStatic
//    fun initHashTable(mb: Int) {
//        val hashSize = 0x100000 * mb
//        hashEntries = hashSize / CLASS_TT_SIZE
////        if(hashTable!=null){
////            clearHashTable()
////        }
//        hashTable = Array(hashEntries) {TT(0UL,0,0,0)}
//    }
    @JvmStatic
    fun generatingTableIndex(hashKey: ULong) = hashKey.rem(hashEntries.toULong()).toInt()

    /**
     * Read data from TT according to given parameters
     * @param hashKey:  ULong represents the hash key of the current board
     * @param alpha:    Int represents the alpha value of the current search
     * @param beta:     Int represents the beta value of the current search
     * @param depth:    Int represent the wanted depth
     * @return Int value represents the value that is saved for the given position in TT table. if not found, return 100000
     */
    @JvmStatic
    fun readHashData(hashKey: ULong,alpha:Int,beta:Int,depth:Int,ply:Int) : Int{
        val currentEntry = if(hashTable.containsKey(hashKey)) hashTable[hashKey] else null
        if(currentEntry!=null){
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
        }

        // couldn't find something relevant in hash table
        return UNKNOWN_VALUE
    }

    /**
     * write data to TT according to given parameters
     * @param hashKey:      ULong represents the hash key of the current board
     * @param _score:        Int represent the current found value of the position
     * @param depth:        Int represent the wanted depth
     * @param hashFlag:     Int represent the position category(PV,Beta or Alpha)
     */
    @JvmStatic
    fun writeEntry(hashKey: ULong,_score: Int,depth: Int,hashFlag : Int,ply:Int){
        var score = _score

        // store score independent from the actual path from root to current position(node)
        if(score < -Search.MATE_SCORE){
            score -= ply
        }
        if(score > Search.MATE_SCORE){
            score += ply
        }

        val newEntry = TT(hashKey,depth,hashFlag,score)
        hashTable[hashKey] = newEntry
    }


    /**
     * Init Zorbiest Keys for all different arrays
     */
    @JvmStatic
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