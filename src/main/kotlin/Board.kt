import enums.*

@ExperimentalUnsignedTypes
class Board {
    /**
     * array of bitboards to represents the board - 6 for each color.
     *
     */
    var pieceBitboards: Array<BitBoard>

    /**
     * array of 3 bit boards - white pieces, black pieces, and both
     */
    var occupanciesBitboards: Array<BitBoard>

    /**
     * int to represent which side is now playing
     */
    var side: Color

    /**
     * Int to represent which tile is now en-passant available
     */
    var enpassant: Square

    /**
     * Int to represnet the current game castling rights
     */
    var castle: Int


    constructor() {
        this.pieceBitboards = Array(12) { BitBoard() }
        this.occupanciesBitboards = Array(3) { BitBoard() }
        this.side = Color.WHITE
        this.enpassant = Square.NO_SQUARE
        this.castle = 0
    }


    constructor(fen: String) : this() {
        this.parseFEN(fen, toReset = false)
    }

    private fun emptyBoard() {
        this.pieceBitboards = Array(12) { BitBoard() }
        this.occupanciesBitboards = Array(3) { BitBoard() }
        this.side = Color.WHITE
        this.enpassant = Square.NO_SQUARE
        this.castle = 0
    }

    fun parseFEN(fen: String, toReset: Boolean = true) {
        if (fen.isEmpty()) {
            throw FENException("empty string fen")
        }
        if (toReset) {
            this.emptyBoard()
        }
        var index = 0
        var rank = 0
        var file = 0

        while (rank < 8) {
            //for each rank
            file = 0
            while (file < 8) {
                //for each file
                val square: Square = Square.fromIntegerToSquare(rank * 8 + file)!!
                if ((fen[index] in 'a'..'z') || (fen[index] in 'A'..'Z')) {
                    // putting piece on board. if invalid piece character -> throws error
                    val piece: Piece =
                        Piece.convertCharToPiece(fen[index]) ?: throw FENException("invalid piece: '${fen[index]}'")
                    this.pieceBitboards[piece.ordinal].setBitOn(square)
                    index++
                }
                if (fen[index] in '0'..'9') {
                    // skipping empty squares
                    val offset = fen[index] - '0'
                    var piece: Piece? = null
                    for (p in Piece.allPieces) {
                        if (this.pieceBitboards[p.ordinal].getBit(square) != 0UL) {
                            piece = p
                            break
                        }
                    }
                    if (piece == null) {
                        //if current square has no piece-> don't advance to match
                        file--
                    }
                    file += offset
                    index++

                }
                if (fen[index] == '/') {
                    // rank separator
                    index++
                }
                file++
            }
            rank++
        }
        index++
        //parsing side to move
        side = if (fen[index] == 'w' || fen[index] == 'W') {
            Color.WHITE
        } else {
            Color.BLACK
        }
        index += 2
        //parsing castling rights
        while (fen[index] != ' ') {
            when (fen[index]) {
                'K' -> {
                    castle = castle or CastlingRights.WK.value
                }
                'Q' -> {
                    castle = castle or CastlingRights.WQ.value
                }
                'k' -> {
                    castle = castle or CastlingRights.BK.value
                }
                'q' -> {
                    castle = castle or CastlingRights.BQ.value
                }
            }
            index++
        }
        index++
        if (fen[index] != '-') {
            //en-Passant square available
            file = fen[index] - 'a'
            rank = 8 - (fen[index + 1] - '0')
            enpassant = Square.fromIntegerToSquare(rank * 8 + file)
                ?: throw FENException("invalid en-passant chars '${fen[index]}${fen[++index]}'")
        } else {
            //en-Passant square not available
            enpassant = Square.NO_SQUARE
        }
        //initializing Occupancies
        matchOccupanciesToPiecesBitBoards()


    }

    private fun matchOccupanciesToPiecesBitBoards() {
        for (p in Piece.whitePieces) {
            occupanciesBitboards[Color.WHITE.ordinal].bitwiseOR(pieceBitboards[p.ordinal])
        }
        for (p in Piece.blackPieces) {
            occupanciesBitboards[Color.BLACK.ordinal].bitwiseOR(pieceBitboards[p.ordinal])
        }
        occupanciesBitboards[Color.BOTH.ordinal].bitwiseOR(occupanciesBitboards[Color.WHITE.ordinal])
        occupanciesBitboards[Color.BOTH.ordinal].bitwiseOR(occupanciesBitboards[Color.BLACK.ordinal])
    }


    fun printBoard() {
        println()
        for (rank in 0..7) {
            for (file in 0..7) {
                val square = Square.fromIntegerToSquare(rank * 8 + file)
                if (file == 0) {
                    print(" ${8 - rank} ")
                }
                var piece = -1
                for (p in 0..11) {
                    if (this.pieceBitboards[p].getBit(square!!) != 0UL) {

                        piece = p
                        break
                    }
                }
                print(if (piece == -1) ". " else "${Piece.convertIndexToPiece(piece)} ")

            }
            println()

        }
        println("\n   A B C D E F G H\n")
        println("    Side:               ${side.name}")
        println("    En-Passant:         ${this.enpassant.name}")
        println("    Castling Rights:    ${if ((castle and CastlingRights.WK.value) != 0) 'K' else '-'}${if ((castle and CastlingRights.WQ.value) != 0) 'Q' else '-'}${if ((castle and CastlingRights.BK.value) != 0) 'k' else '-'}${if ((castle and CastlingRights.BQ.value) != 0) 'q' else '-'}\n")

    }

    fun isSquareAttacked(square: Square, side: Color): Boolean {
        // attacked by white pawns
        if ((side == Color.WHITE) && ((Attacks.pawnAttacks[Color.BLACK.ordinal][square.ordinal].board and this.pieceBitboards[Piece.P.ordinal].board) != 0UL)) {
            return true
        }
        // attacked by black pawns
        if ((side == Color.BLACK) && ((Attacks.pawnAttacks[Color.WHITE.ordinal][square.ordinal].board and this.pieceBitboards[Piece.p.ordinal].board) != 0UL)) {
            return true
        }

        // attacked by knights
        if ((Attacks.knightAttacks[square.ordinal].board and (if (side == Color.WHITE) this.pieceBitboards[Piece.N.ordinal].board else this.pieceBitboards[Piece.n.ordinal].board)) != 0UL) {
            return true
        }

        // attacked by king
        if ((Attacks.kingAttacks[square.ordinal].board and (if (side == Color.WHITE) this.pieceBitboards[Piece.K.ordinal].board else this.pieceBitboards[Piece.k.ordinal].board)) != 0UL) {
            return true
        }
        //attacked by bishop
        val bAttacks = Attacks.getBishopAttacks(square, this.occupanciesBitboards[Color.BOTH.ordinal])
        if ((bAttacks.board and (if (side == Color.WHITE) this.pieceBitboards[Piece.B.ordinal].board else this.pieceBitboards[Piece.b.ordinal].board)) != 0UL) {
            return true
        }
        //attacked by rook
        val rAttacks = Attacks.getRookAttacks(square, this.occupanciesBitboards[Color.BOTH.ordinal])
        if ((rAttacks.board and (if (side == Color.WHITE) this.pieceBitboards[Piece.R.ordinal].board else this.pieceBitboards[Piece.r.ordinal].board)) != 0UL) {
            return true
        }

        //attacked by queen
        val qAttacks = Attacks.getQueenAttacks(square, this.occupanciesBitboards[Color.BOTH.ordinal])
        if ((qAttacks.board and (if (side == Color.WHITE) this.pieceBitboards[Piece.Q.ordinal].board else this.pieceBitboards[Piece.q.ordinal].board)) != 0UL) {
            return true
        }
        //default - return 0
        return false
    }

    fun generateMoves() {
        var sourceSquare: Square
        var targetSquare: Square
        var bitboardCopy: BitBoard
        var attacks: BitBoard = BitBoard()
        val isWhite = side == Color.WHITE

        for (piece in if (isWhite) Piece.whitePieces else Piece.blackPieces) {
            bitboardCopy = BitBoard(this.pieceBitboards[piece.ordinal].board)
            if (piece == Piece.P || piece == Piece.p) {
                generateMovesForPawns(bitboardCopy, isWhite)
            }
            if (piece == Piece.k || piece == Piece.K) {
                generateCastlingMoves(isWhite)
            }

            //generate knight moves

            //generate bishop moves

            //generate queen moves

            //generate king moves
        }


    }

    fun generateCastlingMoves(isWhite: Boolean) {
        if (isKingSide(isWhite)) {
            println("castling king side ${if (isWhite) "e1g1" else "e8g8"}")
        }
        if (isQueenSide(isWhite)) {
            println("castling queen side ${if (isWhite) "e1c1" else "e8c8"}")
        }

    }

    private fun isQueenSide(isWhite: Boolean): Boolean {

        /// white player have castling rights
        val isHavingCastleRight =
            (castle and (if (isWhite) CastlingRights.WQ.value else CastlingRights.BQ.value)) != 0


        // no obstacles in the way of king side castling
        val isClear =

            this.occupanciesBitboards[Color.BOTH.ordinal].getBit(if (isWhite) Square.c1 else Square.c8) == 0UL
                    && this.occupanciesBitboards[Color.BOTH.ordinal].getBit(if (isWhite) Square.b1 else Square.b8) == 0UL
                    && this.occupanciesBitboards[Color.BOTH.ordinal].getBit(if (isWhite) Square.d1 else Square.d8) == 0UL

        // no threat on castling squares
        val isThreatened =
            (if (isWhite) this.isSquareAttacked(
                Square.e1,
                Color.BLACK
            ) else this.isSquareAttacked(Square.e8, Color.WHITE))
                    || (if (isWhite) this.isSquareAttacked(
                Square.d1,
                Color.BLACK
            ) else this.isSquareAttacked(Square.d8, Color.WHITE))



        return isHavingCastleRight && isClear && !isThreatened
    }

    private fun isKingSide(isWhite: Boolean): Boolean {
        /// white player have castling rights
        val isHavingCastleRight =
            ((castle and (if (isWhite) CastlingRights.WK.value else CastlingRights.BK.value)) != 0)

        // no obstacles in the way of king side castling
        val isClear =
            (this.occupanciesBitboards[Color.BOTH.ordinal].getBit(if (isWhite) Square.g1 else Square.g8) == 0UL
                    && this.occupanciesBitboards[Color.BOTH.ordinal].getBit(if (isWhite) Square.f1 else Square.f8) == 0UL)
        // no threat on castling squares
        val isThreatened =
            ((if (isWhite) this.isSquareAttacked(
                Square.e1,
                Color.BLACK
            ) else this.isSquareAttacked(Square.e8, Color.WHITE))

                    || (if (isWhite) this.isSquareAttacked(
                Square.f1,
                Color.BLACK
            ) else this.isSquareAttacked(Square.f8, Color.WHITE)))

        return isHavingCastleRight && isClear && !isThreatened

    }


    fun generateMovesForPawns(bitboardCopy: BitBoard, isWhite: Boolean) {

        while (bitboardCopy.board != 0UL) {
            val sourceSquare = Square.fromIntegerToSquare(bitboardCopy.getLSB())!!
            var targetSquare =
                if (side == Color.WHITE) Square.fromIntegerToSquare(sourceSquare.ordinal - 8)!! else Square.fromIntegerToSquare(
                    sourceSquare.ordinal + 8
                )!!
            val isPromotionPossible =
                if (isWhite) sourceSquare.ordinal in Square.a7.ordinal..Square.h7.ordinal else sourceSquare.ordinal in Square.a2.ordinal..Square.h2.ordinal
            val isTargetOccupied: Boolean = occupanciesBitboards[Color.BOTH.ordinal].getBit(targetSquare) != 0UL
            var isInRange =
                if (isWhite) targetSquare.ordinal >= Square.a8.ordinal else targetSquare.ordinal <= Square.h1.ordinal
            if (isInRange && !isTargetOccupied) {
                //check promotion
                if (isPromotionPossible) {
                    println("pawn promotion ${sourceSquare}${targetSquare}q")
                    println("pawn promotion ${sourceSquare}${targetSquare}r")
                    println("pawn promotion ${sourceSquare}${targetSquare}b")
                    println("pawn promotion ${sourceSquare}${targetSquare}n")
                } else {
                    //adding single push
                    println("pawn push ${sourceSquare}${targetSquare}")

                    //checking double push

                    targetSquare =
                        if (isWhite) Square.fromIntegerToSquare(targetSquare.ordinal - 8)!! else Square.fromIntegerToSquare(
                            targetSquare.ordinal + 8
                        )!!
                    val isDoubleTargetOccupied: Boolean =
                        occupanciesBitboards[Color.BOTH.ordinal].getBit(targetSquare) != 0UL
                    isInRange =
                        if (isWhite) sourceSquare.ordinal in Square.a2.ordinal..Square.h2.ordinal else sourceSquare.ordinal in Square.a7.ordinal..Square.h7.ordinal
                    if (isInRange && !isDoubleTargetOccupied) {
                        println("double pawn push ${sourceSquare}${targetSquare}")
                    }
                }
            }
            //handeling capture moves
            val attacks =
                BitBoard(Attacks.pawnAttacks[side.ordinal][sourceSquare.ordinal].board and occupanciesBitboards[(if (isWhite) Color.BLACK else Color.WHITE).ordinal].board)
            while (attacks.board != 0UL) {
                targetSquare = Square.fromIntegerToSquare(attacks.getLSB())!!
                if (isPromotionPossible) {
                    //capture promotion
                    println("pawn capture promotion ${sourceSquare}${targetSquare}q")
                    println("pawn capture promotion ${sourceSquare}${targetSquare}r")
                    println("pawn capture promotion ${sourceSquare}${targetSquare}b")
                    println("pawn capture promotion ${sourceSquare}${targetSquare}n")
                } else {
                    println("pawn capture ${sourceSquare}${targetSquare}")
                }

                attacks.setBitOff(targetSquare)
            }

            if (enpassant != Square.NO_SQUARE) {
                //checking enpassant move
                val enpassantBit: ULong = (1UL shl enpassant.ordinal)
                val enpassantAtK =
                    BitBoard((Attacks.pawnAttacks[side.ordinal][sourceSquare.ordinal].board and enpassantBit))
                if (enpassantAtK.board != 0UL) {
                    targetSquare = Square.fromIntegerToSquare(enpassantAtK.getLSB())!!
                    println("pawn en-passant capture ${sourceSquare}${targetSquare}")
                }
            }
            // moving to the next bit
            bitboardCopy.setBitOff(sourceSquare)
        }

    }

    fun printAttackedSquares(side: Color) {
        println()
        for (rank in 0..7) {
            for (file in 0..7) {
                val square = Square.fromIntegerToSquare(rank * 8 + file)!!
                if (file == 0) {
                    print("  ${8 - rank} ")
                }
                print(" ${if (isSquareAttacked(square, side)) 1 else 0}")
            }
            println()
        }
        println("\n     A B C D E F G H\n")

    }

    companion object {
        fun createStartBoard(): Board {
            return Board(FENDebugConstants.START_POSITION.fen)
        }
    }

    class FENException(message: String) : Exception(message)
}