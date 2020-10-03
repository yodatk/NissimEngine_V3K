import enums.*

/**
 * ================================================
 *
 * NISSIM ENGINE (HALLAWA)
 * @author: Tomer Gonen
 *
 * ================================================
 */







/**
================================================
Main
================================================
 */

@ExperimentalUnsignedTypes
fun main() {
    Attacks.initAll()
    var b = Board("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1 ")

    val move:Int = Move.encodeMove(Square.d1, Square.d8,Piece.P,null,
        capture = false,
        double = false,
        enpassant = false,
        castling = false
    )

    println("source: ${Move.getSourceFromMove(move)}")
    println("target: ${Move.getTargetFromMove(move)}")
    println("Piece: ${Move.getPieceFromMove(move)}")
    println("Promoted: ${Move.getPromotedFromMove(move)?: "-"}")
    println("Capture: ${Move.getCaptureFromMove(move)}")
    println("double: ${Move.getDoubleFromMove(move)}")
    println("enPassant: ${Move.getEnPassantFromMove(move)}")
    println("castling: ${Move.getCastlingFromMove(move)}")
}