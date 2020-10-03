import enums.Move
import enums.Piece
import enums.Square

object Moves {

        fun encodeMove(
            source: Int, target: Int, piece: Int,
            promoted: Int, capture: Int, double: Int,
            enpassant: Int, castling: Int
        ): Int {
            return (source) or
                    (target shl Move.TARGET.shift) or
                    (piece shl Move.PIECE.shift) or
                    (promoted shl Move.PROMOTED.shift) or
                    (capture shl Move.CAPTURE.shift) or
                    (double shl Move.DOUBLE.shift) or
                    (enpassant shl Move.EN_PASSANT.shift) or
                    (castling shl Move.CASTLING.shift)

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

        fun getSourceFromMoveAsInt(move: Int): Int = (move and Move.SOURCE.flag)
        fun getSourceFromMove(move: Int): Square = Square.fromIntegerToSquare(move and Move.SOURCE.flag)!!
        fun getTargetFromMoveAsInt(move: Int): Int = (move and Move.TARGET.flag) shr Move.TARGET.shift
        fun getTargetFromMove(move: Int): Square = Square.fromIntegerToSquare((move and Move.TARGET.flag) shr Move.TARGET.shift)!!
        fun getPieceFromMoveAsInt(move: Int): Int = (move and Move.PIECE.flag) shr Move.PIECE.shift
        fun getPieceFromMove(move: Int): Piece = Piece.convertIndexToPiece((move and Move.PIECE.flag) shr Move.PIECE.shift)!!
        fun getPromotedFromMoveAsInt(move: Int): Int = (move and Move.PROMOTED.flag) shr Move.PROMOTED.shift
        fun getPromotedFromMove(move: Int): Piece? =
            Piece.convertIndexToPiece((move and Move.PROMOTED.flag) shr Move.PROMOTED.shift)

        fun getCaptureFromMoveAsInt(move: Int): Int = (move and Move.CAPTURE.flag) shr Move.CAPTURE.shift
        fun getCaptureFromMove(move: Int): Boolean = ((move and Move.CAPTURE.flag) shr Move.CAPTURE.shift) != 0
        fun getDoubleFromMoveAsInt(move: Int): Int = (move and Move.DOUBLE.flag) shr Move.DOUBLE.shift
        fun getDoubleFromMove(move: Int): Boolean = ((move and Move.DOUBLE.flag) shr Move.DOUBLE.shift) != 0
        fun getEnPassantFromMoveAsInt(move: Int): Int = (move and Move.EN_PASSANT.flag) shr Move.EN_PASSANT.shift
        fun getEnPassantFromMove(move: Int): Boolean = ((move and Move.EN_PASSANT.flag) shr Move.EN_PASSANT.shift) != 0
        fun getCastlingFromMove(move: Int):Boolean = ((move and Move.CASTLING.flag) shr Move.CASTLING.shift) !=0
        fun getCastlingFromMoveAsInt(move: Int): Int = (move and Move.CASTLING.flag) shr Move.CASTLING.shift

        fun moveUCI(move: Int): String = "${getSourceFromMove(move)}${getTargetFromMove(move)}${getPromotedFromMove(move)?.name?.toLowerCase() ?: " "}"

        fun printMoveUCI(move:Int) = println(moveUCI(move))

        fun printMove(move:Int){
            println("  ${moveUCI(move)}     ${getPieceFromMove(move)}       ${getCaptureFromMove(move)}     ${getDoubleFromMove(move)}    ${getEnPassantFromMove(move)}       ${getCastlingFromMove(move)}\n")

        }
        fun printMoveList(list: List<Int>){
            println("\n  Move      Piece   Capture   Double   Enpassant   Castling\n")
            for(move in list){
                printMove(move)
            }
            println("\n   Total number of moves: ${list.size}\n")

        }

}