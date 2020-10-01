import enums.Color
import enums.Piece

@ExperimentalUnsignedTypes
class Board {
    /**
     * array of bitboards to represents the board - 6 for each color.
     *
     */
    val pieceBitboards: Array<BitBoard>

    /**
     * array of 3 bit boards - white pieces, black pieces, and both
     */
    val occupenciesBitboards: Array<BitBoard>

    /**
     * int to represent which side is now playing
     */
    var side: Int

    /**
     * Int to represent which tile is now en-passant available. if none of the tiles -> value is -1
     */
    var enpassant: Int

    /**
     * Int to represnet the current game castling rights
     */
    var castle: Int


    constructor() {
        //temp constructor
        this.pieceBitboards = Array(12) { BitBoard() }
        this.occupenciesBitboards = Array(3) { BitBoard() }
        this.side = -1
        this.enpassant = -1
        this.castle = -1
    }
}