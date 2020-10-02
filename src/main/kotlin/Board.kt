import enums.CastlingRights
import enums.Color
import enums.Piece
import enums.Square

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
        //temp constructor
        this.pieceBitboards = Array(12) { BitBoard() }
        this.occupanciesBitboards = Array(3) { BitBoard() }
        this.side = Color.WHITE
        this.enpassant = Square.NO_SQUARE
        this.castle = 0
    }

    constructor(fen: String) : this() {
        this.parseFEN(fen)
    }

    private fun emptyBoard() {
        this.pieceBitboards = Array(12) { BitBoard() }
        this.occupanciesBitboards = Array(3) { BitBoard() }
        this.side = Color.WHITE
        this.enpassant = Square.NO_SQUARE
        this.castle = 0
    }

    fun parseFEN(fen: String) {
        if (fen.isEmpty()) {
            throw FENException("empty string fen")
        }
        var index = 0
        var rank = 0
        var file = 0
        this.emptyBoard()
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
        if(fen[index]!='-'){
            //en-Passant square available
            file = fen[index] - 'a'
            rank = fen[index+1] - '0'
            enpassant = Square.fromIntegerToSquare(rank*8+file)?: throw FENException("invalid en-passant chars '${fen[index]}${fen[++index]}'")
        }
        else{
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
        if((side== Color.WHITE) && ((Attacks.pawnAttacks[Color.BLACK.ordinal][square.ordinal].board and this.pieceBitboards[Piece.P.ordinal].board) != 0UL )){
            return true
        }
        // attacked by black pawns
        if((side== Color.BLACK) && ((Attacks.pawnAttacks[Color.WHITE.ordinal][square.ordinal].board and this.pieceBitboards[Piece.p.ordinal].board) != 0UL )){
            return true
        }

        // attacked by knights
        if((Attacks.knightAttacks[square.ordinal].board and (if(side == Color.WHITE) this.pieceBitboards[Piece.N.ordinal].board else this.pieceBitboards[Piece.n.ordinal].board)) != 0UL ){
            return true
        }

        // attacked by king
        if((Attacks.kingAttacks[square.ordinal].board and (if(side == Color.WHITE) this.pieceBitboards[Piece.K.ordinal].board else this.pieceBitboards[Piece.k.ordinal].board)) != 0UL ){
            return true
        }
        //attacked by bishop
        val bAttacks = Attacks.getBishopAttacks(square,this.occupanciesBitboards[Color.BOTH.ordinal])
        if((bAttacks.board and (if(side == Color.WHITE) this.pieceBitboards[Piece.B.ordinal].board else this.pieceBitboards[Piece.b.ordinal].board))!=0UL){
            return true
        }
        //attacked by rook
        val rAttacks = Attacks.getRookAttacks(square,this.occupanciesBitboards[Color.BOTH.ordinal])
        if((rAttacks.board and (if(side == Color.WHITE) this.pieceBitboards[Piece.R.ordinal].board else this.pieceBitboards[Piece.r.ordinal].board))!=0UL){
            return true
        }

        //attacked by queen
        val qAttacks = Attacks.getQueenAttacks(square,this.occupanciesBitboards[Color.BOTH.ordinal])
        if((qAttacks.board and (if(side == Color.WHITE) this.pieceBitboards[Piece.Q.ordinal].board else this.pieceBitboards[Piece.q.ordinal].board))!=0UL){
            return true
        }
        //default - return 0
        return false
    }

    fun generateMoves()  {
        var sourceSquare :Int
        var targetSquare :Int
        var bitboardCopy:BitBoard
        var attacks:BitBoard

        for(piece in Piece.allPieces){
            bitboardCopy = BitBoard(this.pieceBitboards[piece.ordinal].board)

            if(side == Color.WHITE){
                // generate white pawns & white king castle
            }
            else{
                //generate black pawns & black king castle

            }

            //generate knight moves

            //generate bishop moves

            //generate queen moves

            //generate king moves
        }



    }

    fun printAttackedSquares(side: Color) {
        println()
        for(rank in 0..7){
            for(file in 0..7){
                val square  = Square.fromIntegerToSquare(rank*8+file)!!
                if(file==0){
                    print("  ${8-rank} ")
                }
                print(" ${if (isSquareAttacked(square, side)) 1 else 0}")
            }
            println()
        }
        println("\n     A B C D E F G H\n")

    }

    companion object {
        fun createStartBoard(): Board {
            val output = Board()
            output.side = Color.WHITE
            output.enpassant = Square.NO_SQUARE
            output.castle = 15
            output.pieceBitboards[Piece.P.ordinal].setBitOn(Square.a2)
            output.pieceBitboards[Piece.P.ordinal].setBitOn(Square.b2)
            output.pieceBitboards[Piece.P.ordinal].setBitOn(Square.c2)
            output.pieceBitboards[Piece.P.ordinal].setBitOn(Square.d2)
            output.pieceBitboards[Piece.P.ordinal].setBitOn(Square.e2)
            output.pieceBitboards[Piece.P.ordinal].setBitOn(Square.f2)
            output.pieceBitboards[Piece.P.ordinal].setBitOn(Square.g2)
            output.pieceBitboards[Piece.P.ordinal].setBitOn(Square.h2)

            output.pieceBitboards[Piece.N.ordinal].setBitOn(Square.b1)
            output.pieceBitboards[Piece.N.ordinal].setBitOn(Square.g1)
            output.pieceBitboards[Piece.B.ordinal].setBitOn(Square.c1)
            output.pieceBitboards[Piece.B.ordinal].setBitOn(Square.f1)
            output.pieceBitboards[Piece.R.ordinal].setBitOn(Square.a1)
            output.pieceBitboards[Piece.R.ordinal].setBitOn(Square.h1)
            output.pieceBitboards[Piece.Q.ordinal].setBitOn(Square.d1)
            output.pieceBitboards[Piece.K.ordinal].setBitOn(Square.e1)

            output.pieceBitboards[Piece.p.ordinal].setBitOn(Square.a7)
            output.pieceBitboards[Piece.p.ordinal].setBitOn(Square.b7)
            output.pieceBitboards[Piece.p.ordinal].setBitOn(Square.c7)
            output.pieceBitboards[Piece.p.ordinal].setBitOn(Square.d7)
            output.pieceBitboards[Piece.p.ordinal].setBitOn(Square.e7)
            output.pieceBitboards[Piece.p.ordinal].setBitOn(Square.f7)
            output.pieceBitboards[Piece.p.ordinal].setBitOn(Square.g7)
            output.pieceBitboards[Piece.p.ordinal].setBitOn(Square.h7)

            output.pieceBitboards[Piece.n.ordinal].setBitOn(Square.b8)
            output.pieceBitboards[Piece.n.ordinal].setBitOn(Square.g8)
            output.pieceBitboards[Piece.b.ordinal].setBitOn(Square.c8)
            output.pieceBitboards[Piece.b.ordinal].setBitOn(Square.f8)
            output.pieceBitboards[Piece.r.ordinal].setBitOn(Square.a8)
            output.pieceBitboards[Piece.r.ordinal].setBitOn(Square.h8)
            output.pieceBitboards[Piece.q.ordinal].setBitOn(Square.d8)
            output.pieceBitboards[Piece.k.ordinal].setBitOn(Square.e8)

            return output
        }
    }

    class FENException(message: String) : Exception(message)
}