package EngineLogic.enums

/**
 * some FEN patterns for debug purposes
 */

enum class FENDebugConstants(val fen : String) {
    EMPTY("8/8/8/8/8/8/8/8 w - - "),
    START_POSITION("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1 "),
    TRICKY_POSITION("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1 "),
    KILLER_POSITION("rnbqkb1r/pp1p1pPp/8/2p1pP2/1P1P4/3P3P/P1P1P3/RNBQKBNR w KQkq e6 0 1"),
    CMK_POSITION("r2q1rk1/ppp2ppp/2n1bn2/2b1p3/3pP3/3P1NPP/PPP1NPB1/R1BQ1RK1 b - - 0 9 "),
    POSITION3("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - -"),
    REPETITIONS("2r3k1/R7/8/1R6/8/8/P4KPP/8 w - - 0 40");

    companion object{

        val EMPTY_BOARD_HASH : ULong = 14599039209436570616UL
    }
}