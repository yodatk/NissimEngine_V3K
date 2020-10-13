import enums.Piece
import enums.Square

/**
 * Defining zorbist keys for tranportation tables and hashing
 */
@ExperimentalUnsignedTypes
object ZorbistKeys {

    var pieceKeys :Array<Array<ULong>> = Array(12) { Array(64) { 0UL} }

    var enpassantKeys :Array<ULong> =  Array(64) { 0UL}

    var castleKeys :Array<ULong> =  Array(16) { 0UL}

    var sideKey : ULong = 0UL

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