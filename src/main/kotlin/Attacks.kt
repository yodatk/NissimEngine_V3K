import enums.Color
import enums.Square

/**
 * ================================================
 *  Attacks
 *  @author: Tomer Gonen
 * ================================================
 */

@ExperimentalUnsignedTypes
object Attacks {
    /**
     *================================================
     * Attacks
     * @author Tomer Gonen
     *
     *================================================
     */


    /**
    NOT 'A' FILE
    8  0 1 1 1 1 1 1 1
    7  0 1 1 1 1 1 1 1
    6  0 1 1 1 1 1 1 1
    5  0 1 1 1 1 1 1 1
    4  0 1 1 1 1 1 1 1
    3  0 1 1 1 1 1 1 1
    2  0 1 1 1 1 1 1 1
    1  0 1 1 1 1 1 1 1

    A B C D E F G H
     */
    private val NOT_A_FILE: BitBoard = BitBoard(18374403900871474942UL)


    /**
    NOT 'H' FILE
    8  1 1 1 1 1 1 1 0
    7  1 1 1 1 1 1 1 0
    6  1 1 1 1 1 1 1 0
    5  1 1 1 1 1 1 1 0
    4  1 1 1 1 1 1 1 0
    3  1 1 1 1 1 1 1 0
    2  1 1 1 1 1 1 1 0
    1  1 1 1 1 1 1 1 0

    A B C D E F G H
     */
    private val NOT_H_FILE: BitBoard = BitBoard(9187201950435737471UL)


    /**
    NOT 'H' OR 'G' FILE
    8  1 1 1 1 1 1 0 0
    7  1 1 1 1 1 1 0 0
    6  1 1 1 1 1 1 0 0
    5  1 1 1 1 1 1 0 0
    4  1 1 1 1 1 1 0 0
    3  1 1 1 1 1 1 0 0
    2  1 1 1 1 1 1 0 0
    1  1 1 1 1 1 1 0 0

    A B C D E F G H
     */
    private val NOT_H_OR_G_FILE: BitBoard = BitBoard(4557430888798830399UL)


    /**
    NOT 'A' OR 'B' FILE
    8  1 1 1 1 1 1 0 0
    7  1 1 1 1 1 1 0 0
    6  1 1 1 1 1 1 0 0
    5  1 1 1 1 1 1 0 0
    4  1 1 1 1 1 1 0 0
    3  1 1 1 1 1 1 0 0
    2  1 1 1 1 1 1 0 0
    1  1 1 1 1 1 1 0 0

    A B C D E F G H
     */
    private val NOT_A_OR_B_FILE: BitBoard = BitBoard(18229723555195321596UL)


    /**
     * bishop relevant occupancy bit count for every square on board
     */
    var bishopRelevantBits: Array<Int> = arrayOf(
        6, 5, 5, 5, 5, 5, 5, 6,
        5, 5, 5, 5, 5, 5, 5, 5,
        5, 5, 7, 7, 7, 7, 5, 5,
        5, 5, 7, 9, 9, 7, 5, 5,
        5, 5, 7, 9, 9, 7, 5, 5,
        5, 5, 7, 7, 7, 7, 5, 5,
        5, 5, 5, 5, 5, 5, 5, 5,
        6, 5, 5, 5, 5, 5, 5, 6
    )


    /**
     * rook relevant occupancy bit count for every square on board
     */
    var rookRelevantBits: Array<Int> = arrayOf(
        12, 11, 11, 11, 11, 11, 11, 12,
        11, 10, 10, 10, 10, 10, 10, 11,
        11, 10, 10, 10, 10, 10, 10, 11,
        11, 10, 10, 10, 10, 10, 10, 11,
        11, 10, 10, 10, 10, 10, 10, 11,
        11, 10, 10, 10, 10, 10, 10, 11,
        11, 10, 10, 10, 10, 10, 10, 11,
        12, 11, 11, 11, 11, 11, 11, 12
    )

    /** Pawns attack table [ color ] [ square ]*/
    var pawnAttacks: Array<Array<BitBoard>> = Array(2) { Array(64) { BitBoard(0UL) } }

    /** Knight attack table [ square ]*/
    var knightAttacks: Array<BitBoard> = Array(64) { BitBoard(0UL) }

    /** King attack table [ square ]*/
    var kingAttacks: Array<BitBoard> = Array(64) { BitBoard(0UL) }


    /** Bishop attack table [ square ]*/
    var bishopMasks: Array<BitBoard> = Array(64) { BitBoard(0UL) }

    /** Rook attack table [ square ]*/
    var rookMasks: Array<BitBoard> = Array(64) { BitBoard(0UL) }


    /**bishop attack table[ square ][ occupencies ]*/
    var bishopAttacks: Array<Array<BitBoard>> = Array(64) { Array(512) { BitBoard() } }

    /**rook attack table[ square ][ occupencies ]*/
    var rookAttacks: Array<Array<BitBoard>> = Array(64) { Array(4096) { BitBoard() } }


    /**
     * generating pawn attack bitboard of a given color and a given square
     * @return Bitboard of the available moves with the pawn of the given color on the given square
     */
    fun maskPawnAttacks(side: Color, square: Square): BitBoard {

        val board = BitBoard(0UL)

        val attacks = BitBoard(0UL)
        //putting pawn in square
        board.setBitOn(square = square)

        if (side == Color.WHITE) {
            //if the pawn is white
            if (((board.board shr 7) and NOT_A_FILE.board) != 0UL) {
                attacks.bitwiseOR(BitBoard(board.board shr 7))
            }
            if (((board.board shr 9) and NOT_H_FILE.board) != 0UL) {
                attacks.bitwiseOR(BitBoard(board.board shr 9))
            }
        } else {
            //if the pawn is black
            if (((board.board shl 7) and NOT_H_FILE.board) != 0UL) {
                attacks.bitwiseOR(BitBoard(board.board shl 7))
            }
            if (((board.board shl 9) and NOT_A_FILE.board) != 0UL) {
                attacks.bitwiseOR(BitBoard(board.board shl 9))
            }
        }
        return attacks
    }

    /**
     * generating knights movements bitboard from a given square
     * @return Bitboard of the available moves with the Knight on the given square
     */
    fun maskKnightAttacks(square: Square): BitBoard {
        val board = BitBoard(0UL)

        val attacks = BitBoard(0UL)
        //putting pawn in square
        board.setBitOn(square = square)

        if (((board.board shr 17) and NOT_H_FILE.board) != 0UL) {
            attacks.bitwiseOR(BitBoard(board.board shr 17))
        }
        if (((board.board shr 15) and NOT_A_FILE.board) != 0UL) {
            attacks.bitwiseOR(BitBoard(board.board shr 15))
        }
        if (((board.board shr 10) and NOT_H_OR_G_FILE.board) != 0UL) {
            attacks.bitwiseOR(BitBoard(board.board shr 10))
        }
        if (((board.board shr 6) and NOT_A_OR_B_FILE.board) != 0UL) {
            attacks.bitwiseOR(BitBoard(board.board shr 6))
        }

        if (((board.board shl 17) and NOT_A_FILE.board) != 0UL) {
            attacks.bitwiseOR(BitBoard(board.board shl 17))
        }
        if (((board.board shl 15) and NOT_H_FILE.board) != 0UL) {
            attacks.bitwiseOR(BitBoard(board.board shl 15))
        }
        if (((board.board shl 10) and NOT_A_OR_B_FILE.board) != 0UL) {
            attacks.bitwiseOR(BitBoard(board.board shl 10))
        }
        if (((board.board shl 6) and NOT_H_OR_G_FILE.board) != 0UL) {
            attacks.bitwiseOR(BitBoard(board.board shl 6))
        }

        return attacks
    }

    /**
     * generating king movements bitboard from a given square
     * @return Bitboard of the available moves with the king on the given square
     */
    fun maskKingAttacks(square: Square): BitBoard {
        val board = BitBoard(0UL)

        val attacks = BitBoard(0UL)
        //putting pawn in square
        board.setBitOn(square = square)

        if ((board.board shr 8) != 0UL) {
            attacks.bitwiseOR(BitBoard(board.board shr 8))
        }
        if (((board.board shr 9) and NOT_H_FILE.board) != 0UL) {
            attacks.bitwiseOR(BitBoard(board.board shr 9))
        }
        if (((board.board shr 7) and NOT_A_FILE.board) != 0UL) {
            attacks.bitwiseOR(BitBoard(board.board shr 7))
        }
        if (((board.board shr 1) and NOT_H_FILE.board) != 0UL) {
            attacks.bitwiseOR(BitBoard(board.board shr 1))
        }
        if ((board.board shl 8) != 0UL) {
            attacks.bitwiseOR(BitBoard(board.board shl 8))
        }
        if (((board.board shl 9) and NOT_A_FILE.board) != 0UL) {
            attacks.bitwiseOR(BitBoard(board.board shl 9))
        }
        if (((board.board shl 7) and NOT_H_FILE.board) != 0UL) {
            attacks.bitwiseOR(BitBoard(board.board shl 7))
        }
        if (((board.board shl 1) and NOT_A_FILE.board) != 0UL) {
            attacks.bitwiseOR(BitBoard(board.board shl 1))
        }

        return attacks
    }

    /**
     * generating Bishop movements bitboard from a given square
     * @return Bitboard of the available moves with the Bishop on the given square
     */
    fun maskBishopAttacks(square: Square): BitBoard {
        val attacks = BitBoard(0UL)
        val targetR = square.ordinal / 8
        val targetF = square.ordinal % 8

        //up and right
        var rank = targetR + 1
        var file = targetF + 1
        while (rank <= 6 && file <= 6) {
            attacks.bitwiseOR((1UL shl (rank * 8 + file)))
            rank++
            file++
        }
        //up and left
        rank = targetR - 1
        file = targetF + 1
        while (rank >= 1 && file <= 6) {
            attacks.bitwiseOR((1UL shl (rank * 8 + file)))
            rank--
            file++
        }

        //down and right
        rank = targetR + 1
        file = targetF - 1
        while (rank <= 6 && file >= 1) {
            attacks.bitwiseOR((1UL shl (rank * 8 + file)))
            rank++
            file--
        }

        //down and left
        rank = targetR - 1
        file = targetF - 1
        while (rank >= 1 && file >= 1) {
            attacks.bitwiseOR((1UL shl (rank * 8 + file)))
            rank--
            file--
        }
        return attacks

    }

    /**
     * generating Bishop movements bitboard from a given square *CONSIDERING* given block array
     * @return Bitboard of the available moves with the Bishop on the given square
     */
    fun bishopAttacksOnTheFly(square: Square, block: BitBoard): BitBoard {
        val attacks = BitBoard(0UL)
        val targetR = square.ordinal / 8
        val targetF = square.ordinal % 8

        //up and right
        var rank = targetR + 1
        var file = targetF + 1
        while (rank <= 7 && file <= 7) {
            val current = (1UL shl (rank * 8 + file))
            attacks.bitwiseOR(current)
            if (current and block.board != 0UL) {
                break
            }
            rank++
            file++
        }
        //up and left
        rank = targetR - 1
        file = targetF + 1
        while (rank >= 0 && file <= 7) {
            val current = (1UL shl (rank * 8 + file))
            attacks.bitwiseOR(current)
            if (current and block.board != 0UL) {
                break
            }
            rank--
            file++
        }

        //down and right
        rank = targetR + 1
        file = targetF - 1
        while (rank <= 7 && file >= 0) {
            val current = (1UL shl (rank * 8 + file))
            attacks.bitwiseOR(current)
            if (current and block.board != 0UL) {
                break
            }

            rank++
            file--
        }

        //down and left
        rank = targetR - 1
        file = targetF - 1
        while (rank >= 0 && file >= 0) {
            val current = (1UL shl (rank * 8 + file))
            attacks.bitwiseOR(current)
            if (current and block.board != 0UL) {
                break
            }
            rank--
            file--
        }
        return attacks

    }

    /**
     * generating Rook movements bitboard from a given square *CONSIDERING* given block array
     * @return Bitboard of the available moves with the Rook on the given square
     */
    fun rookAttacksOnTheFly(square: Square, block: BitBoard): BitBoard {
        val attacks = BitBoard(0UL)
        val targetR = square.ordinal / 8
        val targetF = square.ordinal % 8


        for (r in targetR + 1..7) {
            val current = (1UL shl (r * 8 + targetF))
            attacks.bitwiseOR(current)
            if (current and block.board != 0UL) {
                break
            }
        }
        for (r in targetR - 1 downTo 0) {
            val current = (1UL shl (r * 8 + targetF))
            attacks.bitwiseOR(current)
            if (current and block.board != 0UL) {
                break
            }
        }
        for (f in targetF + 1..7) {
            val current = (1UL shl (targetR * 8 + f))

            attacks.bitwiseOR(current)
            if (current and block.board != 0UL) {
                break
            }
        }
        for (f in targetF - 1 downTo 0) {
            val current = (1UL shl (targetR * 8 + f))

            attacks.bitwiseOR(current)
            if (current and block.board != 0UL) {
                break
            }
        }
        return attacks

    }

    /**
     * generating Rook movements bitboard from a given square
     * @return Bitboard of the available moves with the Rook on the given square
     */
    fun maskRookAttacks(square: Square): BitBoard {
        val attacks = BitBoard(0UL)
        val targetR = square.ordinal / 8
        val targetF = square.ordinal % 8


        for (r in targetR + 1..6) {
            attacks.bitwiseOR((1UL shl (r * 8 + targetF)))
        }
        for (r in targetR - 1 downTo 1) {
            attacks.bitwiseOR((1UL shl (r * 8 + targetF)))
        }
        for (f in targetF + 1..6) {
            attacks.bitwiseOR((1UL shl (targetR * 8 + f)))
        }
        for (f in targetF - 1 downTo 1) {
            attacks.bitwiseOR((1UL shl (targetR * 8 + f)))
        }
        return attacks

    }

    fun setOccupancy(index: Int, numOfBits: Int, attackMask: BitBoard): BitBoard {
        val occupancy = BitBoard()
        val temp = BitBoard(attackMask)
        for (count in 0 until numOfBits) {
            val square = temp.getLSB()
            temp.setBitOff(Square.fromIntegerToSquare(square)!!)
            if (index and (1 shl count) != 0) {
                occupancy.bitwiseOR((1UL shl square))
            }

        }
        return occupancy
    }

    fun initLeaperAttacks() {
        enumValues<Square>().forEach {
            if (it != Square.NO_SQUARE) {
                pawnAttacks[Color.WHITE.ordinal][it.ordinal] = maskPawnAttacks(Color.WHITE, it)
                pawnAttacks[Color.BLACK.ordinal][it.ordinal] = maskPawnAttacks(Color.BLACK, it)
                knightAttacks[it.ordinal] = maskKnightAttacks(it)
                kingAttacks[it.ordinal] = maskKingAttacks(it)
            }
        }
    }

    fun initSliderAttacksForPiece(isBishop: Boolean) {
        //initialize mask
        enumValues<Square>().forEach {
            if (it != Square.NO_SQUARE) {

                bishopMasks[it.ordinal] = maskBishopAttacks(it)
                rookMasks[it.ordinal] = maskRookAttacks(it)
                val attackMask = if (isBishop) bishopMasks[it.ordinal] else rookMasks[it.ordinal]
                val relevantBits = BitBoard.countBits(attackMask)
                val occupancyIndices = (1 shl relevantBits)
                for (index in 0 until occupancyIndices) {
                    val occupancy: BitBoard = setOccupancy(index, relevantBits, attackMask)

                    if (isBishop) {
                        val magicIndex =
                            ((occupancy.board * MagicNumbers.bishopMagicNumbers[it.ordinal]) shr (64 - bishopRelevantBits[it.ordinal])).toInt()
                        bishopAttacks[it.ordinal][magicIndex] = bishopAttacksOnTheFly(it, occupancy)
                    } else {

                        val magicIndex =
                            ((occupancy.board * MagicNumbers.rookMagicNumbers[it.ordinal]) shr (64 - rookRelevantBits[it.ordinal])).toInt()
                        rookAttacks[it.ordinal][magicIndex] = rookAttacksOnTheFly(it, occupancy)
                    }
                }
            }
        }
    }

    /**
     * get bishop attacks for a given square and occupancy
     */
    fun  getBishopAttacks(_square: Square, _occupancy: BitBoard): BitBoard {
        val square = _square.ordinal
        val occupancy = BitBoard(_occupancy)
        occupancy.bitwiseAnd(bishopMasks[square])
        occupancy.board *= MagicNumbers.bishopMagicNumbers[square]
        occupancy.board = occupancy.board shr (64 - bishopRelevantBits[square])
        return bishopAttacks[square][occupancy.board.toInt()]
    }

    /**
     * get rook attacks for a given square and occupancy
     */
    fun getRookAttacks(_square: Square, _occupancy: BitBoard): BitBoard {
        val square = _square.ordinal
        val occupancy = BitBoard(_occupancy)
        occupancy.bitwiseAnd(rookMasks[square])
        occupancy.board *= MagicNumbers.rookMagicNumbers[square]
        occupancy.board = occupancy.board shr (64 - rookRelevantBits[square])
        return rookAttacks[square][occupancy.board.toInt()]

    }

    /***
     * get queen attack for a given square and occupancy
     */
    /**
     * get rook attacks for a given square and occupancy
     */
    fun getQueenAttacks(_square: Square, _occupancy: BitBoard): BitBoard {
        return BitBoard(getRookAttacks(_square,_occupancy).board or getBishopAttacks(_square,_occupancy).board)
    }

    fun initAll() {
        initLeaperAttacks()
        initSliderAttacksForPiece(isBishop = true)
        initSliderAttacksForPiece(isBishop = false)

    }

}