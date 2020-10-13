import enums.*
import kotlin.text.StringBuilder


@ExperimentalUnsignedTypes
class Board {

    /**
     * array of bitboards to represents the board - 6 for each color.
     *
     */
    var pieceBitboards: Array<ULong>

    /**
     * array of 3 bit boards - white pieces, black pieces, and both
     */
    var occupanciesBitboards: Array<ULong>

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

    /**
     *
     */
    var hashKey: ULong


    constructor() {
        this.pieceBitboards = Array(12) { 0UL }
        this.occupanciesBitboards = Array(3) { 0UL }
        this.side = Color.WHITE
        this.enpassant = Square.NO_SQUARE
        this.castle = 0
        this.hashKey = FENDebugConstants.EMPTY_BOARD_HASH
    }


    constructor(fen: String) : this() {
        this.parseFEN(fen, toReset = false)
    }

    constructor(other: Board) {
        this.castle = other.castle
        this.side = other.side
        this.enpassant = other.enpassant
        this.pieceBitboards = arrayOf(
            other.pieceBitboards[0],
            other.pieceBitboards[1], other.pieceBitboards[2],
            other.pieceBitboards[3],
            other.pieceBitboards[4],
            other.pieceBitboards[5],
            other.pieceBitboards[6],
            other.pieceBitboards[7],
            other.pieceBitboards[8],
            other.pieceBitboards[9],
            other.pieceBitboards[10],
            other.pieceBitboards[11]
        )

        this.occupanciesBitboards = arrayOf(
            other.occupanciesBitboards[0],
            other.occupanciesBitboards[1],
            other.occupanciesBitboards[2],
        )
        this.hashKey = other.hashKey
    }

    /**
     * reset the board to an empty one
     */
    private fun emptyBoard() {
        this.pieceBitboards = Array(12) { 0UL }
        this.occupanciesBitboards = Array(3) { 0UL }
        this.side = Color.WHITE
        this.enpassant = Square.NO_SQUARE
        this.castle = 0
    }

    fun copyOtherBoard(other: Board) {
        this.castle = other.castle
        this.side = other.side
        this.enpassant = other.enpassant
        this.pieceBitboards = other.pieceBitboards
        this.occupanciesBitboards = other.occupanciesBitboards
        this.hashKey = other.hashKey

    }

    override fun equals(other: Any?): Boolean {
        if(other is Board){
            for(i in this.pieceBitboards.indices){
                if(this.pieceBitboards[i]!= other.pieceBitboards[i]){
                    return false
                }
            }
            for(i in this.occupanciesBitboards.indices){
                if(this.occupanciesBitboards[i]!= other.occupanciesBitboards[i]){
                    return false
                }
            }
            return this.castle == other.castle && this.side == other.side && this.enpassant == other.enpassant
        }
        return false
    }

    /**
     * change the state of the board according to the given FEN
     *
     * @param fen: String with FEN format for the current board situation
     * @param toReset: boolean to determine if the board needs to be reset before parsing
     * @throws FENException if the given FEN is not valid
     */
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
                    this.pieceBitboards[piece.ordinal] = BitBoard.setBitOn(this.pieceBitboards[piece.ordinal],square)
                    index++
                }
                if (fen[index] in '0'..'9') {
                    // skipping empty squares
                    val offset = fen[index] - '0'
                    var piece: Piece? = null
                    for (p in Piece.allPieces) {
                        if (BitBoard.getBit(this.pieceBitboards[p.ordinal],square) != 0UL) {
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

        this.hashKey = generateHashKey()


    }

    /**
     * build the current occupancies of the board according to the 12 bitboards of the pieces
     */
    private fun matchOccupanciesToPiecesBitBoards(reset: Boolean = true) {
        if (reset) {
            this.occupanciesBitboards = Array(3) { 0UL }
        }
        for (p in Piece.whitePieces) {
            occupanciesBitboards[Color.WHITE.ordinal] = occupanciesBitboards[Color.WHITE.ordinal] or pieceBitboards[p.ordinal]
        }
        for (p in Piece.blackPieces) {
            occupanciesBitboards[Color.BLACK.ordinal] =  occupanciesBitboards[Color.BLACK.ordinal] or (pieceBitboards[p.ordinal])
        }
        occupanciesBitboards[Color.BOTH.ordinal] = occupanciesBitboards[Color.BOTH.ordinal] or (occupanciesBitboards[Color.WHITE.ordinal])
        occupanciesBitboards[Color.BOTH.ordinal] = occupanciesBitboards[Color.BOTH.ordinal] or (occupanciesBitboards[Color.BLACK.ordinal])
    }


    override fun toString(): String {
        val builder = StringBuilder("\n")
        for (rank in 0..7) {
            for (file in 0..7) {
                val square = Square.fromIntegerToSquare(rank * 8 + file)!!
                if (file == 0) {
                    builder.append(" ${8 - rank} ")
                }
                var piece = -1
                for (p in 0..11) {
                    if (BitBoard.getBit(this.pieceBitboards[p],square) != 0UL) {

                        piece = p
                        break
                    }
                }
                builder.append(if (piece == -1) "[ ]" else "[${Piece.convertIndexToPiece(piece)}]")

            }
            builder.append("\n")

        }
        builder.append("\n  ")
        for(c in "ABCDEFGH"){
            builder.append("  $c")
        }
        builder.append("\n\n")
        builder.append("    Side:               ${side.name}\n")
        builder.append("    En-Passant:         ${this.enpassant.name}\n")
        builder.append("    Castling Rights:    ${if ((castle and CastlingRights.WK.value) != 0) 'K' else '-'}${if ((castle and CastlingRights.WQ.value) != 0) 'Q' else '-'}${if ((castle and CastlingRights.BK.value) != 0) 'k' else '-'}${if ((castle and CastlingRights.BQ.value) != 0) 'q' else '-'}\n")
        builder.append("    Hash Key:           ${this.generateHashKey()}")
        return builder.toString()
    }


    /**
     * print the current situation in the board:
     * '.' -> empty square
     * P -> white pawn         p -> black pawn
     * N -> white knight       n -> black knight
     * B -> white bishop       b -> black bishop
     * R -> white rook         r -> black rook
     * Q -> white queen        q -> black queen
     * K -> white king         k -> black king
     */
    fun printBoard() {
        println(this)
    }

    /**
     * determining if the given square is attacked by any of the pieces of the given color
     * @param square: the given square to check
     * @param side: the color which is attcking
     * @return: true if square is attacked, false other wise
     */
    fun isSquareAttacked(square: Square, side: Color): Boolean {
        // attacked by white pawns
        if ((side == Color.WHITE) && ((Attacks.pawnAttacks[Color.BLACK.ordinal][square.ordinal] and this.pieceBitboards[Piece.P.ordinal]) != 0UL)) {
            return true
        }
        // attacked by black pawns
        if ((side == Color.BLACK) && ((Attacks.pawnAttacks[Color.WHITE.ordinal][square.ordinal] and this.pieceBitboards[Piece.p.ordinal]) != 0UL)) {
            return true
        }

        // attacked by knights
        if ((Attacks.knightAttacks[square.ordinal] and (if (side == Color.WHITE) this.pieceBitboards[Piece.N.ordinal] else this.pieceBitboards[Piece.n.ordinal])) != 0UL) {
            return true
        }

        // attacked by king
        if ((Attacks.kingAttacks[square.ordinal] and (if (side == Color.WHITE) this.pieceBitboards[Piece.K.ordinal] else this.pieceBitboards[Piece.k.ordinal])) != 0UL) {
            return true
        }
        //attacked by bishop
        val bAttacks = Attacks.getBishopAttacks(square, this.occupanciesBitboards[Color.BOTH.ordinal])
        if ((bAttacks and (if (side == Color.WHITE) this.pieceBitboards[Piece.B.ordinal] else this.pieceBitboards[Piece.b.ordinal])) != 0UL) {
            return true
        }
        //attacked by rook
        val rAttacks = Attacks.getRookAttacks(square, this.occupanciesBitboards[Color.BOTH.ordinal])
        if ((rAttacks and (if (side == Color.WHITE) this.pieceBitboards[Piece.R.ordinal] else this.pieceBitboards[Piece.r.ordinal])) != 0UL) {
            return true
        }

        //attacked by queen
        val qAttacks = Attacks.getQueenAttacks(square, this.occupanciesBitboards[Color.BOTH.ordinal])
        if ((qAttacks and (if (side == Color.WHITE) this.pieceBitboards[Piece.Q.ordinal] else this.pieceBitboards[Piece.q.ordinal])) != 0UL) {
            return true
        }
        //default - return 0
        return false
    }

    /**
     * generate all possible moves for the current side in the current board situation
     * @return List of integers that represents all the possible moves on the board
     */
    fun generateMoves(): MutableList<Int> {
        var bitboardCopy: ULong
        val isWhite = side == Color.WHITE
        var moveList: MutableList<Int> = mutableListOf()

        for (piece in if (isWhite) Piece.whitePieces else Piece.blackPieces) {
            bitboardCopy = (this.pieceBitboards[piece.ordinal])
            if (piece == Piece.P || piece == Piece.p) {
                generateMovesForPawns(bitboardCopy, isWhite, moveList)
            } else {
                if (piece == Piece.k || piece == Piece.K) {
                    generateCastlingMoves(isWhite, moveList)
                }

                //generate knight / bishop / queen / king moves
                generateMovesForPiece(piece, bitboardCopy, isWhite, moveList)
            }
        }
        return moveList
    }

    /**
     * generate all possible moves for the given piece type, in the given board, for the given color
     * @param piece: determining the piece type
     * @param bitboardCopy: copy of the bitboard of that piece
     * @param isWhite: true-> generate for white. false -> generate for black
     * @param moveList: list of integers that collects all the moves available so far to edit
     */
    fun generateMovesForPiece(piece: Piece, _bitboardCopy: ULong, isWhite: Boolean, moveList: MutableList<Int>) {
        var bitboardCopy = _bitboardCopy
        var sourceSquare: Square
        var targetSquare: Square
        var attacks: ULong
        while (bitboardCopy != 0UL) {
            sourceSquare = Square.fromIntegerToSquare(BitBoard.getLSB(bitboardCopy))!!
            val attacksSource = when (piece) {
                Piece.N, Piece.n -> Attacks.knightAttacks[sourceSquare.ordinal]
                Piece.B, Piece.b -> Attacks.getBishopAttacks(sourceSquare, occupanciesBitboards[Color.BOTH.ordinal])
                Piece.R, Piece.r -> Attacks.getRookAttacks(sourceSquare, occupanciesBitboards[Color.BOTH.ordinal])
                Piece.Q, Piece.q -> Attacks.getQueenAttacks(sourceSquare, occupanciesBitboards[Color.BOTH.ordinal])
                Piece.K, Piece.k -> Attacks.kingAttacks[sourceSquare.ordinal]
                else -> return
            }
            val occ : ULong =
                if (isWhite) occupanciesBitboards[Color.WHITE.ordinal].inv() else occupanciesBitboards[Color.BLACK.ordinal].inv()
            attacks = (attacksSource and occ)
            while (attacks != 0UL) {
                targetSquare = Square.fromIntegerToSquare(BitBoard.getLSB(attacks))!!

                val occ2 : ULong = if (isWhite) (occupanciesBitboards[Color.BLACK.ordinal]) else ( occupanciesBitboards[Color.WHITE.ordinal])


                if (BitBoard.getBit(occ2,targetSquare) == 0UL) {
                    // quiet moves
                    moveList.add(
                        Moves.encodeMove(
                            source = sourceSquare, target = targetSquare, piece, null,
                            capture = false,
                            double = false,
                            enpassant = false, castling = false
                        )
                    )

                } else {
                    // capture moves
                    moveList.add(
                        Moves.encodeMove(
                            source = sourceSquare, target = targetSquare, piece, null,
                            capture = true,
                            double = false,
                            enpassant = false, castling = false
                        )
                    )
                }

                attacks = BitBoard.setBitOff(attacks,targetSquare)
            }

            bitboardCopy = BitBoard.setBitOff(bitboardCopy,sourceSquare)

        }


    }

    /**
     * generate castling move for the given color
     * @param isWhite: true -> generate for White, false-> check for black
     * @param moveList: list of all the moves so far that are available on the board to edit
     */
    fun generateCastlingMoves(isWhite: Boolean, moveList: MutableList<Int>) {
        if (isKingSide(isWhite)) {
            moveList.add(
                Moves.encodeMove(
                    source = if (isWhite) Square.e1 else Square.e8,
                    target = if (isWhite) Square.g1 else Square.g8,
                    if (isWhite) Piece.K else Piece.k,
                    null,
                    capture = false,
                    double = false,
                    enpassant = false,
                    castling = true
                )
            )
        }
        if (isQueenSide(isWhite)) {
            moveList.add(
                Moves.encodeMove(
                    source = if (isWhite) Square.e1 else Square.e8,
                    target = if (isWhite) Square.c1 else Square.c8,
                    if (isWhite) Piece.K else Piece.k,
                    null,
                    capture = false,
                    double = false,
                    enpassant = false,
                    castling = true
                )
            )
        }

    }

    /**
     * determining for the given color if it's possible to castle queen side
     * @param isWhite Boolean  true->check for white, false -> check for black
     * @return: true if castling is possible, false otherwise
     */
    private fun isQueenSide(isWhite: Boolean): Boolean {

        /// white player have castling rights
        val isHavingCastleRight =
            (castle and (if (isWhite) CastlingRights.WQ.value else CastlingRights.BQ.value)) != 0


        // no obstacles in the way of king side castling
        val isClear =

            BitBoard.getBit(this.occupanciesBitboards[Color.BOTH.ordinal], (if (isWhite) Square.c1 else Square.c8)) == 0UL
                    &&  BitBoard.getBit(this.occupanciesBitboards[Color.BOTH.ordinal],(if (isWhite) Square.b1 else Square.b8) )== 0UL
                    && BitBoard.getBit(this.occupanciesBitboards[Color.BOTH.ordinal], (if (isWhite) Square.d1 else Square.d8) )== 0UL

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

    /**
     * determining for the given color if it's possible to castle king side
     * @param isWhite Boolean  true->check for white, false -> check for black
     * @return: true if castling is possible, false otherwise
     */
    private fun isKingSide(isWhite: Boolean): Boolean {
        /// white player have castling rights
        val isHavingCastleRight =
            ((castle and (if (isWhite) CastlingRights.WK.value else CastlingRights.BK.value)) != 0)

        // no obstacles in the way of king side castling
        val isClear =
            (BitBoard.getBit(this.occupanciesBitboards[Color.BOTH.ordinal],(if (isWhite) Square.g1 else Square.g8)) == 0UL
                    && BitBoard.getBit(this.occupanciesBitboards[Color.BOTH.ordinal],(if (isWhite) Square.f1 else Square.f8) )== 0UL)
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


    /***
     * find all the possible moves for pawns for the given board and color
     * @param bitboardCopy: BItboard represent the bitboard of the pawns
     * @param isWhite: Boolean to say the color of the pawn: true-> White, false->Black
     * @param moveList: Mutable list of integers represent all the possible moves so far
     */
    fun generateMovesForPawns(_bitboardCopy: ULong, isWhite: Boolean, moveList: MutableList<Int>) {
        var bitboardCopy = _bitboardCopy
        while (bitboardCopy != 0UL) {
            val sourceSquare = Square.fromIntegerToSquare(BitBoard.getLSB(bitboardCopy))!!
            var targetSquare =
                if (side == Color.WHITE) Square.fromIntegerToSquare(sourceSquare.ordinal - 8)!! else Square.fromIntegerToSquare(
                    sourceSquare.ordinal + 8
                )!!
            val isPromotionPossible =
                if (isWhite) sourceSquare.ordinal in Square.a7.ordinal..Square.h7.ordinal else sourceSquare.ordinal in Square.a2.ordinal..Square.h2.ordinal
            val isTargetOccupied: Boolean = BitBoard.getBit(occupanciesBitboards[Color.BOTH.ordinal],targetSquare) != 0UL
            var isInRange =
                if (isWhite) targetSquare.ordinal >= Square.a8.ordinal else targetSquare.ordinal <= Square.h1.ordinal
            if (isInRange && !isTargetOccupied) {
                //check promotion
                if (isPromotionPossible) {
                    addPromotionMoves(moveList, sourceSquare, targetSquare, isWhite)
                } else {
                    //adding single push

                    moveList.add(
                        Moves.encodeMove(
                            source = sourceSquare,
                            target = targetSquare,
                            piece = if (isWhite) Piece.P else Piece.p,
                            promoted = null,
                            capture = false,
                            double = false,
                            enpassant = false,
                            castling = false
                        )
                    )
                    //checking double push

                    targetSquare =
                        if (isWhite) Square.fromIntegerToSquare(targetSquare.ordinal - 8)!! else Square.fromIntegerToSquare(
                            targetSquare.ordinal + 8
                        )!!
                    val isDoubleTargetOccupied: Boolean =
                        BitBoard.getBit(occupanciesBitboards[Color.BOTH.ordinal],(targetSquare)) != 0UL
                    isInRange =
                        if (isWhite) sourceSquare.ordinal in Square.a2.ordinal..Square.h2.ordinal else sourceSquare.ordinal in Square.a7.ordinal..Square.h7.ordinal
                    if (isInRange && !isDoubleTargetOccupied) {
                        moveList.add(
                            Moves.encodeMove(
                                source = sourceSquare,
                                target = targetSquare,
                                piece = if (isWhite) Piece.P else Piece.p,
                                promoted = null,
                                capture = false,
                                double = true,
                                enpassant = false,
                                castling = false
                            )
                        )
                    }
                }
            }
            //handeling capture moves
            var attacks =
                (Attacks.pawnAttacks[side.ordinal][sourceSquare.ordinal] and occupanciesBitboards[(if (isWhite) Color.BLACK else Color.WHITE).ordinal])
            while (attacks != 0UL) {
                targetSquare = Square.fromIntegerToSquare(BitBoard.getLSB(attacks))!!
                if (isPromotionPossible) {
                    //capture promotion
                    addPromotionMoves(moveList, sourceSquare, targetSquare, isWhite, isCapture = true)
                } else {
                    moveList.add(
                        Moves.encodeMove(
                            source = sourceSquare,
                            target = targetSquare,
                            piece = if (isWhite) Piece.P else Piece.p,
                            promoted = null,
                            capture = true,
                            double = false,
                            enpassant = false,
                            castling = false
                        )
                    )
                }

                attacks = BitBoard.setBitOff(attacks,targetSquare)
            }

            if (enpassant != Square.NO_SQUARE) {
                //checking enpassant move
                val enpassantBit: ULong = (1UL shl enpassant.ordinal)
                val enpassantAtK =
                    ((Attacks.pawnAttacks[side.ordinal][sourceSquare.ordinal] and enpassantBit))
                if (enpassantAtK != 0UL) {
                    targetSquare = Square.fromIntegerToSquare(BitBoard.getLSB(enpassantAtK))!!
                    moveList.add(
                        Moves.encodeMove(
                            source = sourceSquare,
                            target = targetSquare,
                            piece = if (isWhite) Piece.P else Piece.p,
                            promoted = null,
                            capture = true,
                            double = false,
                            enpassant = true,
                            castling = false
                        )
                    )
                }
            }
            // moving to the next bit
            bitboardCopy = BitBoard.setBitOff(bitboardCopy,sourceSquare)
        }

    }

    private fun addPromotionMoves(
        moveList: MutableList<Int>,
        sourceSquare: Square,
        targetSquare: Square,
        isWhite: Boolean,
        isCapture: Boolean = false
    ) {
        moveList.add(
            Moves.encodeMove(
                source = sourceSquare, target = targetSquare, piece = if (isWhite) Piece.P else Piece.p,
                promoted = if (isWhite) Piece.Q else Piece.q,
                capture = isCapture,
                double = false,
                enpassant = false, castling = false
            )
        )
        moveList.add(
            Moves.encodeMove(
                source = sourceSquare, target = targetSquare, piece = if (isWhite) Piece.P else Piece.p,
                promoted = if (isWhite) Piece.R else Piece.r,
                capture = isCapture,
                double = false,
                enpassant = false, castling = false
            )
        )
        moveList.add(
            Moves.encodeMove(
                source = sourceSquare, target = targetSquare, piece = if (isWhite) Piece.P else Piece.p,
                promoted = if (isWhite) Piece.B else Piece.b,
                capture = isCapture,
                double = false,
                enpassant = false, castling = false
            )
        )
        moveList.add(
            Moves.encodeMove(
                source = sourceSquare, target = targetSquare, piece = if (isWhite) Piece.P else Piece.p,
                promoted = if (isWhite) Piece.N else Piece.n,
                capture = isCapture,
                double = false,
                enpassant = false, castling = false
            )
        )
    }

    /**
     * print all squared that are under attacked by the given color in board
     * @param side: color that is attacking in the board
     */
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

    /**
     * Making the given board on the board if possible. if the move is made return true, else (if the move is invalid) return false
     * @param move: encoded move to make
     * @param isCapturesOnly: if true, making the move only if it's including capture
     */
    fun makeMove(move: Int, isCapturesOnly: Boolean = false): Boolean {
        if (isCapturesOnly) {
            return if (Moves.getCaptureFromMove(move)) {
                makeMove(move, isCapturesOnly = false)
            } else {
                // not a real capture -> return 0;
                false
            }
        } else {
            //all moves -> get all information from move
            val boardCopy = Board(this)
            val source = Moves.getSourceFromMove(move)
            val target = Moves.getTargetFromMove(move)
            val piece = Moves.getPieceFromMove(move)
            val isPromoted = Moves.getPromotedFromMove(move)
            val isCapture = Moves.getCaptureFromMove(move)
            val isDouble = Moves.getDoubleFromMove(move)
            val isEnPassant = Moves.getEnPassantFromMove(move)
            val isCastle = Moves.getCastlingFromMove(move)

            // move piece
            this.pieceBitboards[piece.ordinal] = BitBoard.setBitOff(this.pieceBitboards[piece.ordinal],source)
            this.pieceBitboards[piece.ordinal] = BitBoard.setBitOn(this.pieceBitboards[piece.ordinal],target)

            // hashing
            hashKey = hashKey xor ZorbistKeys.pieceKeys[piece.ordinal][source.ordinal]
            hashKey = hashKey xor ZorbistKeys.pieceKeys[piece.ordinal][target.ordinal]

            if (isCapture) {
                // remove other piece
                for (p in (if (side == Color.WHITE) Piece.blackPieces else Piece.whitePieces)) {
                    if (BitBoard.getBit(this.pieceBitboards[p.ordinal],target) != 0UL) {
                        // if the current piece is the one to remove: remove it and stop search
                        this.pieceBitboards[p.ordinal] = BitBoard.setBitOff(this.pieceBitboards[p.ordinal],target)
                        hashKey = hashKey xor ZorbistKeys.pieceKeys[p.ordinal][target.ordinal]
                        break
                    }
                }
            }
            if (isPromoted != null) {
                this.pieceBitboards[piece.ordinal] =  BitBoard.setBitOff(this.pieceBitboards[piece.ordinal],target) // removing pawn
                //hash
                hashKey = hashKey xor ZorbistKeys.pieceKeys[piece.ordinal][target.ordinal]
                this.pieceBitboards[isPromoted.ordinal] = BitBoard.setBitOn(this.pieceBitboards[isPromoted.ordinal],target) // putting new piece
                //hash
                hashKey = hashKey xor ZorbistKeys.pieceKeys[isPromoted.ordinal][target.ordinal]
            }

            if (isEnPassant) {
                if (side == Color.WHITE) {
                    this.pieceBitboards[Piece.p.ordinal] = BitBoard.setBitOff(this.pieceBitboards[Piece.p.ordinal],(target.ordinal + 8))
                    // hash
                    hashKey = hashKey xor ZorbistKeys.pieceKeys[Piece.p.ordinal][target.ordinal + 8]
                } else {
                    this.pieceBitboards[Piece.P.ordinal] = BitBoard.setBitOff(this.pieceBitboards[Piece.P.ordinal],(target.ordinal - 8))
                    // hash
                    hashKey = hashKey xor ZorbistKeys.pieceKeys[Piece.P.ordinal][target.ordinal - 8]
                }
            }
            if(enpassant != Square.NO_SQUARE){
                hashKey = hashKey xor ZorbistKeys.enpassantKeys[enpassant.ordinal]
            }

            //turn off enpassant square
            this.enpassant = Square.NO_SQUARE

            if (isDouble) {

                if(side == Color.WHITE){
                    enpassant = Square.fromIntegerToSquare(target.ordinal+8)!!
                    hashKey = hashKey xor ZorbistKeys.enpassantKeys[target.ordinal+8]
                }

                else{
                    enpassant = Square.fromIntegerToSquare(target.ordinal-8)!!
                    hashKey = hashKey xor ZorbistKeys.enpassantKeys[target.ordinal-8]
                }
            }

            if (isCastle) {
                when (target) {
                    //white king side
                    Square.g1 -> {
                        this.pieceBitboards[Piece.R.ordinal] = BitBoard.setBitOff(this.pieceBitboards[Piece.R.ordinal],Square.h1)
                        this.pieceBitboards[Piece.R.ordinal] = BitBoard.setBitOn(this.pieceBitboards[Piece.R.ordinal],Square.f1)
                        hashKey = hashKey xor ZorbistKeys.pieceKeys[Piece.R.ordinal][Square.h1.ordinal]
                        hashKey = hashKey xor ZorbistKeys.pieceKeys[Piece.R.ordinal][Square.f1.ordinal]
                    }

                    //white queen side
                    Square.c1 -> {
                        this.pieceBitboards[Piece.R.ordinal] = BitBoard.setBitOff(this.pieceBitboards[Piece.R.ordinal],Square.a1)
                        this.pieceBitboards[Piece.R.ordinal] = BitBoard.setBitOn(this.pieceBitboards[Piece.R.ordinal],Square.d1)
                        hashKey = hashKey xor ZorbistKeys.pieceKeys[Piece.R.ordinal][Square.a1.ordinal]
                        hashKey = hashKey xor ZorbistKeys.pieceKeys[Piece.R.ordinal][Square.d1.ordinal]

                    }

                    //black king side
                    Square.g8 -> {
                        this.pieceBitboards[Piece.r.ordinal] = BitBoard.setBitOff(this.pieceBitboards[Piece.r.ordinal],Square.h8)
                        this.pieceBitboards[Piece.r.ordinal]= BitBoard.setBitOn(this.pieceBitboards[Piece.r.ordinal],Square.f8)
                        hashKey = hashKey xor ZorbistKeys.pieceKeys[Piece.r.ordinal][Square.h8.ordinal]
                        hashKey = hashKey xor ZorbistKeys.pieceKeys[Piece.r.ordinal][Square.f8.ordinal]

                    }

                    //white queen side
                    Square.c8 -> {
                        this.pieceBitboards[Piece.r.ordinal] = BitBoard.setBitOff(this.pieceBitboards[Piece.r.ordinal],Square.a8)
                        this.pieceBitboards[Piece.r.ordinal] = BitBoard.setBitOn(this.pieceBitboards[Piece.r.ordinal],Square.d8)
                        hashKey = hashKey xor ZorbistKeys.pieceKeys[Piece.r.ordinal][Square.a8.ordinal]
                        hashKey = hashKey xor ZorbistKeys.pieceKeys[Piece.r.ordinal][Square.d8.ordinal]
                    }

                    else -> {
                        this.copyOtherBoard(boardCopy)
                        return false
                    }
                }
            }
            //canceling previous castle hash
            hashKey = hashKey xor  ZorbistKeys.castleKeys[castle]

            // updating castling rights
            this.castle = this.castle and Moves.CASTLING_RIGHTS_UPDATE_ARRAY[source.ordinal]
            this.castle = this.castle and Moves.CASTLING_RIGHTS_UPDATE_ARRAY[target.ordinal]
            // updating previous castle hash
            hashKey = hashKey xor  ZorbistKeys.castleKeys[castle]


            // updating Occupancies
            matchOccupanciesToPiecesBitBoards()

            //change side
            this.side = if (side == Color.WHITE) Color.BLACK else Color.WHITE

            hashKey = hashKey xor ZorbistKeys.sideKey

//            //======================== for dubug only=============================
//            val hash_from_scratch = generateHashKey()
//            if(hashKey!=hash_from_scratch){
//                println("\nMAKE MOVE")
//                println("move: ${Moves.moveUCI(move)}")
//                this.printBoard()
//                println("hash key should be: ${hash_from_scratch}")
//                //readLine()
//            }
//
//
//            //======================== for dubug only=============================
            val kingSquare =
                if (side == Color.WHITE) Square.fromIntegerToSquare(BitBoard.getLSB(this.pieceBitboards[Piece.k.ordinal]))!!
                else Square.fromIntegerToSquare(BitBoard.getLSB(this.pieceBitboards[Piece.K.ordinal]))!!
            return if (isSquareAttacked(kingSquare, side)) {
                //move is invalid. restore and return false
                this.copyOtherBoard(boardCopy)
                false
            } else {
                true
            }

        }
    }

    fun generateHashKey() : ULong{
        var finalKey : ULong = 0UL
        var tempBitboard : ULong
        for(piece in Piece.allPieces){
            tempBitboard = this.pieceBitboards[piece.ordinal]

            while(tempBitboard!=0UL){
                val square : Int = BitBoard.getLSB(tempBitboard)

                finalKey = finalKey xor ZorbistKeys.pieceKeys[piece.ordinal][square]

                tempBitboard = BitBoard.setBitOff(tempBitboard,square)
            }

        }
        if(enpassant != Square.NO_SQUARE){
            finalKey = finalKey xor ZorbistKeys.enpassantKeys[enpassant.ordinal]
        }

            finalKey = finalKey xor ZorbistKeys.castleKeys[this.castle]



        if(this.side == Color.BLACK){
            finalKey = finalKey xor ZorbistKeys.sideKey
        }

        return finalKey
    }



    companion object {
        fun createStartBoard(): Board {
            return Board(FENDebugConstants.START_POSITION.fen)
        }


    }

    class FENException(message: String) : Exception(message)
}