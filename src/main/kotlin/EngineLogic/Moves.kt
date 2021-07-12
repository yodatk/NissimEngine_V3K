package EngineLogic

import EngineLogic.enums.Move
import EngineLogic.enums.Piece
import EngineLogic.enums.Square

object Moves {



    @JvmStatic
    val CASTLING_RIGHTS_UPDATE_ARRAY : Array<Int> = arrayOf(
        7, 15, 15, 15, 3, 15, 15, 11,
        15, 15, 15, 15, 15, 15, 15, 15,
        15, 15, 15, 15, 15, 15, 15, 15,
        15, 15, 15, 15, 15, 15, 15, 15,
        15, 15, 15, 15, 15, 15, 15, 15,
        15, 15, 15, 15, 15, 15, 15, 15,
        15, 15, 15, 15, 15, 15, 15, 15,
        13, 15, 15, 15, 12, 15, 15, 14
    )

    @JvmStatic
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

    @JvmStatic
    fun encodeMove(
        source: Square, target: Square, piece: Piece,
        promoted: Piece? = null, capture: Boolean = false, double: Boolean = false,
        enpassant: Boolean = false, castling: Boolean = false
    ): Int {
        //checking promoted
        return encodeMove(
            source = source.ordinal,
            target = target.ordinal,
            piece = piece.ordinal,
            promoted = Piece.promotedPiecesMap.getOrDefault(promoted,0),
            capture = if (capture) 1 else 0,
            double = if (double) 1 else 0,
            enpassant = if (enpassant) 1 else 0,
            castling = if (castling) 1 else 0,
        )

    }

    @JvmStatic
    fun getSourceFromMoveAsInt(move: Int): Int = (move and Move.SOURCE.flag)
    @JvmStatic
    fun getSourceFromMove(move: Int): Square = Square.fromIntegerToSquare(move and Move.SOURCE.flag)!!
    @JvmStatic
    fun getTargetFromMoveAsInt(move: Int): Int = (move and Move.TARGET.flag) shr Move.TARGET.shift
    @JvmStatic
    fun getTargetFromMove(move: Int): Square =
        Square.fromIntegerToSquare((move and Move.TARGET.flag) shr Move.TARGET.shift)!!
    @JvmStatic
    fun getPieceFromMoveAsInt(move: Int): Int = (move and Move.PIECE.flag) shr Move.PIECE.shift
    @JvmStatic
    fun getPieceFromMove(move: Int): Piece =
        Piece.convertIndexToPiece((move and Move.PIECE.flag) shr Move.PIECE.shift)!!
    @JvmStatic
    fun getPromotedFromMoveAsInt(move: Int): Int = (move and Move.PROMOTED.flag) shr Move.PROMOTED.shift
    @JvmStatic
    fun getPromotedFromMove(move: Int): Piece? =
        if((move and Move.PROMOTED.flag) == 0) null else  Piece.convertIndexToPiece((move and Move.PROMOTED.flag) shr Move.PROMOTED.shift)
    @JvmStatic
    fun getCaptureFromMoveAsInt(move: Int): Int = (move and Move.CAPTURE.flag) shr Move.CAPTURE.shift
    @JvmStatic
    fun getCaptureFromMove(move: Int): Boolean = ((move and Move.CAPTURE.flag) shr Move.CAPTURE.shift) != 0
    @JvmStatic
    fun getDoubleFromMoveAsInt(move: Int): Int = (move and Move.DOUBLE.flag) shr Move.DOUBLE.shift
    @JvmStatic
    fun getDoubleFromMove(move: Int): Boolean = ((move and Move.DOUBLE.flag) shr Move.DOUBLE.shift) != 0
    @JvmStatic
    fun getEnPassantFromMoveAsInt(move: Int): Int = (move and Move.EN_PASSANT.flag) shr Move.EN_PASSANT.shift
    @JvmStatic
    fun getEnPassantFromMove(move: Int): Boolean = ((move and Move.EN_PASSANT.flag) shr Move.EN_PASSANT.shift) != 0
    @JvmStatic
    fun getCastlingFromMove(move: Int): Boolean = ((move and Move.CASTLING.flag) shr Move.CASTLING.shift) != 0
    @JvmStatic
    fun getCastlingFromMoveAsInt(move: Int): Int = (move and Move.CASTLING.flag) shr Move.CASTLING.shift

    @JvmStatic
    fun moveUCI(move: Int): String =
        "${getSourceFromMove(move)}${getTargetFromMove(move)}${getPromotedFromMove(move)?.name?.toLowerCase() ?: ""}"

    @JvmStatic
    fun printMoveUCI(move: Int) = println(moveUCI(move))
    @JvmStatic
    fun printMove(move: Int) {
        println(
            "  ${moveUCI(move)}      ${getPieceFromMove(move)}       ${if (getCaptureFromMove(move)) 1 else 0}         ${
                if (getDoubleFromMove(
                        move
                    )
                ) 1 else 0
            }        ${if (getEnPassantFromMove(move)) 1 else 0}           ${if (getCastlingFromMove(move)) 1 else 0}"
        )

    }

    @JvmStatic
   fun printMoveList(list: List<Int>) {
        println("\n  Move      Piece   Capture   Double   Enpassant   Castling\n")
        for (move in list) {
            printMove(move)
        }
        println("\n   Total number of moves: ${list.size}\n")

    }

}