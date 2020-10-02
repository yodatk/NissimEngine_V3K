import enums.Color
import enums.Piece
import enums.Square

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
    println("Nissim: HALLAWA!\n===================================\n")
    val emptyBoard = "8/8/8/8/8/8/8/8 w - - "
    val startPosition = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1 "
    val trickyPosition = "r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1 "
    val killerPosition = "rnbqkb1r/pp1p1pPp/8/2p1pP2/1P1P4/3P3P/P1P1P3/RNBQKBNR w KQkq e6 0 1"
    val cmkPosition = "r2q1rk1/ppp2ppp/2n1bn2/2b1p3/3pP3/3P1NPP/PPP1NPB1/R1BQ1RK1 b - - 0 9 "

    Attacks.initAll()

    var b =Board()
    println("==========\nemptyBoard\n==========\n")
    b.parseFEN(emptyBoard)
    b.printBoard()
    println("==========\nstartPosition\n==========\n")
    b.parseFEN(startPosition)
    b.printBoard()
    b.occupenciesBitboards[0].printBitboard()
    b.occupenciesBitboards[1].printBitboard()
    b.occupenciesBitboards[2].printBitboard()
    println("==========\ntrickyPosition\n==========\n")
    b.parseFEN(trickyPosition)
    b.printBoard()
    println("==========\nkillerPosition\n==========\n")
    b.parseFEN(killerPosition)
    b.printBoard()
    println("==========\ncmkPosition\n==========\n")
    b.parseFEN(cmkPosition)
    b.printBoard()
}