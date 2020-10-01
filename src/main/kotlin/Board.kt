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
    val pieceBitboards: Array<BitBoard>

    /**
     * array of 3 bit boards - white pieces, black pieces, and both
     */
    val occupenciesBitboards: Array<BitBoard>

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
        this.occupenciesBitboards = Array(3) { BitBoard() }
        this.side = Color.WHITE
        this.enpassant = Square.NO_SQUARE
        this.castle = -1
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
}