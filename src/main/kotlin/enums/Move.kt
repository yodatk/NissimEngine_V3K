package enums

/**
 * Moves are encoded as int in the following manner
binary move bits                                   hexidecimal constants

0000 0000 0000 0000 0011 1111    source square       0x3f
0000 0000 0000 1111 1100 0000    target square       0xfc0
0000 0000 1111 0000 0000 0000    piece               0xf000
0000 1111 0000 0000 0000 0000    promoted piece      0xf0000
0001 0000 0000 0000 0000 0000    capture flag        0x100000
0010 0000 0000 0000 0000 0000    double push flag    0x200000
0100 0000 0000 0000 0000 0000    enpassant flag      0x400000
1000 0000 0000 0000 0000 0000    castling flag       0x800000

 */
enum class Move(val flag: Int, val shift: Int) {
    SOURCE(flag = 0x3f, 0),
    TARGET(flag = 0xfc0, shift = 6),
    PIECE(flag = 0xf000, shift = 12),
    PROMOTED(flag = 0xf0000, shift = 16),
    CAPTURE(flag = 0x100000, shift = 20),
    DOUBLE(flag = 0x200000, shift = 21),
    EN_PASSANT(flag = 0x400000, shift = 22),
    CASTLING(flag = 0x800000, shift = 23);


    companion object {
        fun encodeMove(
            source: Int, target: Int, piece: Int,
            promoted: Int, capture: Int, double: Int,
            enpassant: Int, castling: Int
        ): Int {
            return (source) or
                    (target shl TARGET.shift) or
                    (piece shl PIECE.shift) or
                    (promoted shl PROMOTED.shift) or
                    (capture shl CAPTURE.shift) or
                    (double shl DOUBLE.shift) or
                    (enpassant shl EN_PASSANT.shift) or
                    (castling shl CASTLING.shift)

        }

        fun encodeMove(
            source: Square, target: Square, piece: Piece,
            promoted: Piece?, capture: Boolean, double: Boolean,
            enpassant: Boolean, castling: Boolean
        ): Int {
            return encodeMove(
                source = source.ordinal,
                target = target.ordinal,
                piece = piece.ordinal,
                promoted = promoted?.ordinal ?: 12,
                capture = if (capture) 1 else 0,
                double = if (double) 1 else 0,
                enpassant = if (enpassant) 1 else 0,
                castling = if (castling) 1 else 0,
            )

        }

        fun getSourceFromMoveAsInt(move: Int): Int = (move and SOURCE.flag)
        fun getSourceFromMove(move: Int): Square = Square.fromIntegerToSquare(move and SOURCE.flag)!!
        fun getTargetFromMoveAsInt(move: Int): Int = (move and TARGET.flag) shr TARGET.shift
        fun getTargetFromMove(move: Int): Square = Square.fromIntegerToSquare((move and TARGET.flag) shr TARGET.shift)!!
        fun getPieceFromMoveAsInt(move: Int): Int = (move and PIECE.flag) shr PIECE.shift
        fun getPieceFromMove(move: Int): Piece = Piece.convertIndexToPiece((move and PIECE.flag) shr PIECE.shift)!!
        fun getPromotedFromMoveAsInt(move: Int): Int = (move and PROMOTED.flag) shr PROMOTED.shift
        fun getPromotedFromMove(move: Int): Piece? =
            Piece.convertIndexToPiece((move and PROMOTED.flag) shr PROMOTED.shift)

        fun getCaptureFromMoveAsInt(move: Int): Int = (move and CAPTURE.flag) shr CAPTURE.shift
        fun getCaptureFromMove(move: Int): Boolean = ((move and CAPTURE.flag) shr CAPTURE.shift) != 0
        fun getDoubleFromMoveAsInt(move: Int): Int = (move and DOUBLE.flag) shr DOUBLE.shift
        fun getDoubleFromMove(move: Int): Boolean = ((move and DOUBLE.flag) shr DOUBLE.shift) != 0
        fun getEnPassantFromMoveAsInt(move: Int): Int = (move and EN_PASSANT.flag) shr EN_PASSANT.shift
        fun getEnPassantFromMove(move: Int): Boolean = ((move and EN_PASSANT.flag) shr EN_PASSANT.shift) != 0
        fun getCastlingFromMoveAsInt(move: Int): Int = (move and CASTLING.flag) shr CASTLING.shift
        fun getCastlingFromMove(move: Int): Boolean = ((move and CASTLING.flag) shr CASTLING.shift) != 0

    }


}