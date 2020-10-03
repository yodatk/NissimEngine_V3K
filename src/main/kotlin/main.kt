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
    var b = Board(FENDebugConstants.TRICKY_POSITION.fen)
    val movesList = b.generateMoves()
    b.printBoard()

    Moves.printMoveList(movesList)


    /*

    move   piece   capture   double   enpassant   castling

   d5d6    P       0        0        0            0
   d5e6    P       1        0        0            0
   a2a3    P       0        0        0            0
   a2a4    P       0        1        0            0
   b2b3    P       0        0        0            0
   g2g3    P       0        0        0            0
   g2g4    P       0        1        0            0
   g2h3    P       1        0        0            0
   f5d7    P       1        0        0            0
   f5f7    P       1        0        0            0
   f5c6    P       0        0        0            0
   f5g6    P       1        0        0            0
   f5c4    P       0        0        0            0
   f5g4    P       0        0        0            0
   f5d3    P       0        0        0            0
   d3b5    P       0        0        0            0
   d3a4    P       0        0        0            0
   d3b1    P       0        0        0            0
   d3d1    P       0        0        0            0
   d2h6    P       0        0        0            0
   d2g5    P       0        0        0            0
   d2f4    P       0        0        0            0
   d2e3    P       0        0        0            0
   d2c1    P       0        0        0            0
   g2a6    P       1        0        0            0
   g2b5    P       0        0        0            0
   g2c4    P       0        0        0            0
   g2d3    P       0        0        0            0
   g2d1    P       0        0        0            0
   g2f1    P       0        0        0            0
   d1b1    P       0        0        0            0
   d1c1    P       0        0        0            0
   d1d1    P       0        0        0            0
   h1f1    P       0        0        0            0
   h1g1    P       0        0        0            0
   f3f6    P       1        0        0            0
   f3f5    P       0        0        0            0
   f3h5    P       0        0        0            0
   f3f4    P       0        0        0            0
   f3g4    P       0        0        0            0
   f3d3    P       0        0        0            0
   f3e3    P       0        0        0            0
   f3g3    P       0        0        0            0
   f3h3    P       1        0        0            0
   e1g1    K       0        0        0            1
   e1c1    K       0        0        0            1
   f1d1    P       0        0        0            0
   f1f1    P       0        0        0            0

    */

}