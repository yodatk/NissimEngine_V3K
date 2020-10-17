import enums.Color
import enums.GamePhase
import enums.Piece
import enums.Square


@ExperimentalUnsignedTypes
object Evaluation {
    enum class PieceType {
        PAWN, KNIGHT, BISHOP, ROOK, QUEEN, KING
    }

    data class MoveWithScore(val move: Int, val score: Int) : Comparable<MoveWithScore> {
        override fun compareTo(other: MoveWithScore): Int {
            return this.score.compareTo(other.score)
        }
    }

    const val OPENING_PHASE_SCORE = 6192

    const val ENDGAME_PHASE_SCORE = 518

    // positional scores array: [ game pahse ] [ piece ] [ square ]
    @JvmStatic
    val positionalScore: Array<Array<Array<Int>>> = arrayOf(

        // ================= OPENING POSITIONAL SCORES ================= //
        arrayOf(
            //***************PAWN***************
            arrayOf(
                0, 0, 0, 0, 0, 0, 0, 0,
                98, 134, 61, 95, 68, 126, 34, -11,
                -6, 7, 26, 31, 65, 56, 25, -20,
                -14, 13, 6, 21, 23, 12, 17, -23,
                -27, -2, -5, 12, 17, 6, 10, -25,
                -26, -4, -4, -10, 3, 3, 33, -12,
                -35, -1, -20, -23, -15, 24, 38, -22,
                0, 0, 0, 0, 0, 0, 0, 0,
            ),

            //***************KNIGHT***************
            arrayOf(
                -167, -89, -34, -49, 61, -97, -15, -107,
                -73, -41, 72, 36, 23, 62, 7, -17,
                -47, 60, 37, 65, 84, 129, 73, 44,
                -9, 17, 19, 53, 37, 69, 18, 22,
                -13, 4, 16, 13, 28, 19, 21, -8,
                -23, -9, 12, 10, 19, 17, 25, -16,
                -29, -53, -12, -3, -1, 18, -14, -19,
                -105, -21, -58, -33, -17, -28, -19, -23,
            ),
            //***************BISHOP***************
            arrayOf(
                -29, 4, -82, -37, -25, -42, 7, -8,
                -26, 16, -18, -13, 30, 59, 18, -47,
                -16, 37, 43, 40, 35, 50, 37, -2,
                -4, 5, 19, 50, 37, 37, 7, -2,
                -6, 13, 13, 26, 34, 12, 10, 4,
                0, 15, 15, 15, 14, 27, 18, 10,
                4, 15, 16, 0, 7, 21, 33, 1,
                -33, -3, -14, -21, -13, -12, -39, -21,
            ),
            //***************ROOK***************
            arrayOf(
                32, 42, 32, 51, 63, 9, 31, 43,
                27, 32, 58, 62, 80, 67, 26, 44,
                -5, 19, 26, 36, 17, 45, 61, 16,
                -24, -11, 7, 26, 24, 35, -8, -20,
                -36, -26, -12, -1, 9, -7, 6, -23,
                -45, -25, -16, -17, 3, 0, -5, -33,
                -44, -16, -20, -9, -1, 11, -6, -71,
                -19, -13, 1, 17, 16, 7, -37, -26,

            ),
            //***************QUEEN***************
            arrayOf(
                -28, 0, 29, 12, 59, 44, 43, 45,
                -24, -39, -5, 1, -16, 57, 28, 54,
                -13, -17, 7, 8, 29, 56, 47, 57,
                -27, -27, -16, -16, -1, 17, -2, 1,
                -9, -26, -9, -10, -2, -4, 3, -3,
                -14, 2, -11, -2, -5, 2, 14, 5,
                -35, -8, 11, 2, 8, 15, -3, 1,
                -1, -18, -9, 10, -15, -25, -31, -50,
            ),
            //***************KING***************
            arrayOf(
                -65, 23, 16, -15, -56, -34, 2, 13,
                29, -1, -20, -7, -8, -4, -38, -29,
                -9, 24, 2, -16, -20, 6, 22, -22,
                -17, -20, -12, -27, -30, -25, -14, -36,
                -49, -1, -27, -39, -46, -44, -33, -51,
                -14, -14, -22, -46, -44, -30, -15, -27,
                1, 7, -8, -64, -43, -16, 9, 8,
                -15, 36, 12, -54, 8, -28, 24, 14,

            ),

            ),


        // ================= ENDGAME POSITIONAL SCORES ================= //

        arrayOf(
            //***************PAWN***************
            arrayOf(
                0, 0, 0, 0, 0, 0, 0, 0,
                178, 173, 158, 134, 147, 132, 165, 187,
                94, 100, 85, 67, 56, 53, 82, 84,
                32, 24, 13, 5, -2, 4, 17, 17,
                13, 9, -3, -7, -7, -8, 3, -1,
                4, 7, -6, 1, 0, -5, -1, -8,
                13, 8, 8, 10, 13, 0, 2, -7,
                0, 0, 0, 0, 0, 0, 0, 0,

            ),

            //***************KNIGHT***************
            arrayOf(
                -58, -38, -13, -28, -31, -27, -63, -99,
                -25, -8, -25, -2, -9, -25, -24, -52,
                -24, -20, 10, 9, -1, -9, -19, -41,
                -17, 3, 22, 22, 22, 11, 8, -18,
                -18, -6, 16, 25, 16, 17, 4, -18,
                -23, -3, -1, 15, 10, -3, -20, -22,
                -42, -20, -10, -5, -2, -20, -23, -44,
                -29, -51, -23, -15, -22, -18, -50, -64,
            ),

            //***************BISHOP***************
            arrayOf(
                -14, -21, -11, -8, -7, -9, -17, -24,
                -8, -4, 7, -12, -3, -13, -4, -14,
                2, -8, 0, -1, -2, 6, 0, 4,
                -3, 9, 12, 9, 14, 10, 3, 2,
                -6, 3, 13, 19, 7, 10, -3, -9,
                -12, -3, 8, 10, 13, 3, -7, -15,
                -14, -18, -7, -1, 4, -9, -15, -27,
                -23, -9, -23, -5, -9, -16, -5, -17,
            ),

            //***************ROOK***************
            arrayOf(
                13, 10, 18, 15, 12, 12, 8, 5,
                11, 13, 13, 11, -3, 3, 8, 3,
                7, 7, 7, 5, 4, -3, -5, -3,
                4, 3, 13, 1, 2, 1, -1, 2,
                3, 5, 8, 4, -5, -6, -8, -11,
                -4, 0, -5, -1, -7, -12, -8, -16,
                -6, -6, 0, 2, -9, -9, -11, -3,
                -9, 2, 3, -1, -5, -13, 4, -20,
            ),

            //***************QUEEN***************
            arrayOf(
                -9, 22, 22, 27, 27, 19, 10, 20,
                -17, 20, 32, 41, 58, 25, 30, 0,
                -20, 6, 9, 49, 47, 35, 19, 9,
                3, 22, 24, 45, 57, 40, 57, 36,
                -18, 28, 19, 47, 31, 34, 39, 23,
                -16, -27, 15, 6, 9, 17, 10, 5,
                -22, -23, -30, -16, -16, -23, -36, -32,
                -33, -28, -22, -43, -5, -32, -20, -41,
            ),

            //***************KING***************
            arrayOf(
                -74, -35, -18, -18, -11, 15, 4, -17,
                -12, 17, 14, 17, 17, 38, 23, 11,
                10, 17, 23, 15, 20, 45, 44, 13,
                -8, 22, 24, 27, 26, 33, 26, 3,
                -18, -4, 21, 24, 27, 23, 9, -11,
                -19, -3, 11, 21, 23, 16, 7, -9,
                -27, -11, 4, 13, 14, 4, -5, -17,
                -53, -34, -21, -11, -28, -14, -24, -43
            ),

            )
    )

    @JvmStatic
    val mirrorScores = arrayOf(

        Square.a1.ordinal,
        Square.b1.ordinal,
        Square.c1.ordinal,
        Square.d1.ordinal,
        Square.e1.ordinal,
        Square.f1.ordinal,
        Square.g1.ordinal,
        Square.h1.ordinal,


        Square.a2.ordinal,
        Square.b2.ordinal,
        Square.c2.ordinal,
        Square.d2.ordinal,
        Square.e2.ordinal,
        Square.f2.ordinal,
        Square.g2.ordinal,
        Square.h2.ordinal,


        Square.a3.ordinal,
        Square.b3.ordinal,
        Square.c3.ordinal,
        Square.d3.ordinal,
        Square.e3.ordinal,
        Square.f3.ordinal,
        Square.g3.ordinal,
        Square.h3.ordinal,


        Square.a4.ordinal,
        Square.b4.ordinal,
        Square.c4.ordinal,
        Square.d4.ordinal,
        Square.e4.ordinal,
        Square.f4.ordinal,
        Square.g4.ordinal,
        Square.h4.ordinal,


        Square.a5.ordinal,
        Square.b5.ordinal,
        Square.c5.ordinal,
        Square.d5.ordinal,
        Square.e5.ordinal,
        Square.f5.ordinal,
        Square.g5.ordinal,
        Square.h5.ordinal,


        Square.a6.ordinal,
        Square.b6.ordinal,
        Square.c6.ordinal,
        Square.d6.ordinal,
        Square.e6.ordinal,
        Square.f6.ordinal,
        Square.g6.ordinal,
        Square.h6.ordinal,


        Square.a7.ordinal,
        Square.b7.ordinal,
        Square.c7.ordinal,
        Square.d7.ordinal,
        Square.e7.ordinal,
        Square.f7.ordinal,
        Square.g7.ordinal,
        Square.h7.ordinal,


        Square.a8.ordinal,
        Square.b8.ordinal,
        Square.c8.ordinal,
        Square.d8.ordinal,
        Square.e8.ordinal,
        Square.f8.ordinal,
        Square.g8.ordinal,
        Square.h8.ordinal,

        )

    //most valuable piece -> least valuable attacker
    @JvmStatic
    val MVV_LVA: Array<Array<Int>> = arrayOf(
        //white pPawn (from pawns to king)
        arrayOf(105, 205, 305, 405, 505, 605, 105, 205, 305, 405, 505, 605),

        //White Knight (from pawns to king)
        arrayOf(104, 204, 304, 404, 504, 604, 104, 204, 304, 404, 504, 604),

        //White Bishop (from pawns to king)
        arrayOf(103, 203, 303, 403, 503, 603, 103, 203, 303, 403, 503, 603),


        //White Rook (from pawns to king)
        arrayOf(102, 202, 302, 402, 502, 602, 102, 202, 302, 402, 502, 602),

        //White Queen (from pawns to king)
        arrayOf(101, 201, 301, 401, 501, 601, 101, 201, 301, 401, 501, 601),

        //White King (from pawns to king)
        arrayOf(100, 200, 300, 400, 500, 600, 100, 200, 300, 400, 500, 600),


        //Black Pawn (from pawns to king)
        arrayOf(105, 205, 305, 405, 505, 605, 105, 205, 305, 405, 505, 605),
        //Black Knight (from pawns to king)
        arrayOf(104, 204, 304, 404, 504, 604, 104, 204, 304, 404, 504, 604),
        //Black Bishop (from pawns to king)
        arrayOf(103, 203, 303, 403, 503, 603, 103, 203, 303, 403, 503, 603),
        //Black Rook (from pawns to king)
        arrayOf(102, 202, 302, 402, 502, 602, 102, 202, 302, 402, 502, 602),
        //Black Queen (from pawns to king)
        arrayOf(101, 201, 301, 401, 501, 601, 101, 201, 301, 401, 501, 601),
        //Black King (from pawns to king)
        arrayOf(100, 200, 300, 400, 500, 600, 100, 200, 300, 400, 500, 600)

    )

    // extract rank from square
    @JvmStatic
    val GET_RANK_FROM_SQUARE = arrayOf(
        7, 7, 7, 7, 7, 7, 7, 7,
        6, 6, 6, 6, 6, 6, 6, 6,
        5, 5, 5, 5, 5, 5, 5, 5,
        4, 4, 4, 4, 4, 4, 4, 4,
        3, 3, 3, 3, 3, 3, 3, 3,
        2, 2, 2, 2, 2, 2, 2, 2,
        1, 1, 1, 1, 1, 1, 1, 1,
        0, 0, 0, 0, 0, 0, 0, 0
    )

    const val DOUBLE_PAWN_PENALTY_OPENING = -5

    const val DOUBLE_PAWN_PENALTY_ENDGAME = -10

    const val ISOLATED_PAWN_PENALTY_OPENING = -5

    const val ISOLATED_PAWN_PENALTY_ENDGAME = -10

    const val KING_SHIELD_BONUS = 5

    @JvmStatic
    val PASSED_PAWN_BONUS = arrayOf(
        0, 10, 30, 50, 75, 100, 150, 200
    )


    const val SEMI_OPEN_FILE_BONUS = 10

    const val OPEN_FILE_SCORE = 15

    // mobility units
    const val BISHOP_UNIT = 4
    const val QUEEN_UNIT = 9

    // mobility bonuses
    const val BISHOP_MOBILITY_UNIT_OPENING = 5
    const val BISHOP_MOBILITY_UNIT_ENDGAME = 5
    const val QUEEN_MOBILITY_UNIT_OPENING = 1
    const val QUEEN_MOBILITY_UNIT_ENDGAME = 2

    /**
     * File masks [ square ]
     */
    @JvmStatic
    val fileMasks: Array<ULong> = Array(64) { 0UL }


    /**
     * rank masks [ square ]
     */
    @JvmStatic
    val rankMasks: Array<ULong> = Array(64) { 0UL }

    /**
     * isolated pawns masks [ square ]
     */
    @JvmStatic
    val isolatedPawnsMasks: Array<ULong> = Array(64) { 0UL }

    /**
     * white passed pawns masks [ square ]
     */
    @JvmStatic
    val whitePassedPawnsMasks: Array<ULong> = Array(64) { 0UL }

    /**
     * black passed pawns masks[ square ]
     */
    @JvmStatic
    val blackPassedPawnsMasks: Array<ULong> = Array(64) { 0UL }

    @JvmStatic
    var killerMoves = Array(2) { Array(64) { 0 } }

    @JvmStatic
    var historyMoves = Array(12) { Array(64) { 0 } }

    @JvmStatic
    var followPrincipleVariation = false
    @JvmStatic
    var scorePrincipleVariation = false

    @JvmStatic
    fun setFileRankMask(fileNumber: Int, rankNumber: Int): ULong {
        //define file or rank
        var mask = 0UL

        for (rank in 0..7) {
            //loop over files
            for (file in 0..7) {
                val square = rank * 8 + file
                if (fileNumber != -1) {
                    if (file == fileNumber) {
                        mask = mask or BitBoard.setBitOn(mask, square)
                    }

                } else if (rankNumber != -1) {
                    if (rank == rankNumber) {
                        mask = mask or BitBoard.setBitOn(mask, square)
                    }

                }

            }
        }

        return mask

    }
    @JvmStatic
    fun initEvaluationMasks() {
        for (rank in 0..7) {
            for (file in 0..7) {
                val square = rank * 8 + file
                // init file mask for current square
                fileMasks[square] = fileMasks[square] or setFileRankMask(file, -1)
                // init rank mask for current square
                rankMasks[square] = rankMasks[square] or setFileRankMask(-1, rank)
                //init isolated pawns masks
                isolatedPawnsMasks[square] = isolatedPawnsMasks[square] or setFileRankMask(file - 1, -1)
                isolatedPawnsMasks[square] = isolatedPawnsMasks[square] or setFileRankMask(file + 1, -1)

                // init white passed pawns
                whitePassedPawnsMasks[square] = whitePassedPawnsMasks[square] or setFileRankMask(file - 1, -1)
                whitePassedPawnsMasks[square] = whitePassedPawnsMasks[square] or setFileRankMask(file, -1)
                whitePassedPawnsMasks[square] = whitePassedPawnsMasks[square] or setFileRankMask(file + 1, -1)

                var cancel = 0UL
                var i = 0
                while (i < rank) {
                    cancel = cancel or rankMasks[i * 8 + file]
                    i++
                }
                whitePassedPawnsMasks[square] = whitePassedPawnsMasks[square] and cancel

                // init black passed pawns
                blackPassedPawnsMasks[square] = blackPassedPawnsMasks[square] or setFileRankMask(file - 1, -1)
                blackPassedPawnsMasks[square] = blackPassedPawnsMasks[square] or setFileRankMask(file, -1)
                blackPassedPawnsMasks[square] = blackPassedPawnsMasks[square] or setFileRankMask(file + 1, -1)
                cancel = cancel.inv()
                cancel = cancel and (rankMasks[i * 8 + file].inv())
                blackPassedPawnsMasks[square] = blackPassedPawnsMasks[square] and cancel
            }
        }
    }
    @JvmStatic
    fun resetHistoryAndKillerMoves() {
        killerMoves = Array(2) { Array(64) { 0 } }

        historyMoves = Array(12) { Array(64) { 0 } }
    }
    @JvmStatic
    fun sortedPossibleMoves(movesList: List<Int>, ply: Int): List<Int> {



         // BEST SO FAR
        val withScoreList = movesList.map { MoveWithScore(it, evaluateMoveScore(it, ply)) }.toMutableList()

        //insertion sort
        var i = 0
        while (i < movesList.size) {
            var j = i + 1
            while (j < movesList.size) {
                if (withScoreList[i].score < withScoreList[j].score) {
                    val tempWScore = withScoreList[i]
                    withScoreList[i] = withScoreList[j]
                    withScoreList[j] = tempWScore
                }
                j++
            }
            i++
        }
        return withScoreList.map { it.move }
//
//        //quick sort - not implemented yet for debugging
//        return withScoreList.sortedDescending().map{it.move}
    }
    @JvmStatic
    fun printMovesScores(ply: Int) {
        val movesList = sortedPossibleMoves(Board.generateMoves(), ply)
        println("   Moves Scores:\n")
        for (move in movesList) {
            println("     move: ${Moves.moveUCI(move)} score: ${evaluateMoveScore(move, ply)}")
        }
    }

    @JvmStatic
    fun evaluateMoveScore( move: Int, ply: Int): Int {

        if (scorePrincipleVariation) {
            if (Search.principalVariationTable[0][Search.ply] == move) {
                scorePrincipleVariation = false
                return 20000
            }
        }

        if (Moves.getCaptureFromMove(move)) {
            // score capture move
            var targetPiece = Piece.P.ordinal
            val arr = if (Board.side == Color.WHITE) Piece.blackPieces else Piece.whitePieces
            for (piece in arr) {
                if (BitBoard.getBit(Board.pieceBitboards[piece.ordinal], Moves.getTargetFromMove(move)) != 0UL) {
                    targetPiece = piece.ordinal
                    break
                }
            }
            return MVV_LVA[Moves.getPieceFromMoveAsInt(move)][targetPiece] + 10000
        } else {
            // score quiet move
            return when (move) {
                killerMoves[0][ply] -> {
                    9000
                }
                killerMoves[1][ply] -> {
                    8000
                }
                else -> {
                    historyMoves[Moves.getPieceFromMoveAsInt(move)][Moves.getTargetFromMoveAsInt(move)]
                }
            }
        }
    }

    /**
     *  Calculate the values of the "officers" pieces to define if the game is in the opening, middle game, or endgame
     *  @param board: Board to check
     *  @return int value represents the score of the current "game phase"
     */
    @JvmStatic
    fun getGamePhaseScore(): Int {
        var whitePieces = 0
        var blackPieces = 0

        for (piece in Piece.whitePieces) {
            if (piece != Piece.P && piece != Piece.K) {
                whitePieces += BitBoard.countBits(Board.pieceBitboards[piece.ordinal]) * piece.openingValue

            }

        }
        for (piece in Piece.blackPieces) {
            if (piece != Piece.p && piece != Piece.k) {
                blackPieces += BitBoard.countBits(Board.pieceBitboards[piece.ordinal]) * (-piece.openingValue)
            }
        }
        return whitePieces + blackPieces
    }

    @JvmStatic
    fun evaluate(): Int {
        val gamePhaseScore = getGamePhaseScore()


        val gamePhase: GamePhase =

            // determining game phase
            when {
                gamePhaseScore > OPENING_PHASE_SCORE -> {
                    GamePhase.OPENING
                }
                gamePhaseScore < ENDGAME_PHASE_SCORE -> {
                    GamePhase.END_GAME
                }
                else -> {
                    GamePhase.MIDDLE_GAME
                }
            }


        var openingScore = 0
        var endGameScore = 0
        var currBitboard: ULong
        var square: Int
        for (p in Piece.allPieces) {
            currBitboard = Board.pieceBitboards[p.ordinal]
            while (currBitboard != 0UL) {
                square = BitBoard.getLSB(currBitboard)

                // score opening and endgame material score
                openingScore += p.openingValue
                endGameScore += p.endGameValue

                // score += p.value
                when (p) {
                    //WHITE
                    Piece.P -> {

                        // positional
                        openingScore += positionalScore[GamePhase.OPENING.ordinal][PieceType.PAWN.ordinal][square]
                        endGameScore += positionalScore[GamePhase.END_GAME.ordinal][PieceType.PAWN.ordinal][square]

                        // double pawns
                        val doublePawns =
                            BitBoard.countBits(Board.pieceBitboards[Piece.P.ordinal] and fileMasks[square])
                        if (doublePawns > 1) {
                            openingScore += (doublePawns - 1) * DOUBLE_PAWN_PENALTY_OPENING
                            endGameScore += (doublePawns - 1) * DOUBLE_PAWN_PENALTY_ENDGAME
                        }
                        // isolated pawns
                        if (Board.pieceBitboards[Piece.P.ordinal] and isolatedPawnsMasks[square] == 0UL) {
                            //give an isolated pawn panelty
                            openingScore += ISOLATED_PAWN_PENALTY_OPENING
                            endGameScore += ISOLATED_PAWN_PENALTY_ENDGAME
                        }
                        // passed pawns
                        if (whitePassedPawnsMasks[square] and Board.pieceBitboards[Piece.p.ordinal] == 0UL) {
                            openingScore += PASSED_PAWN_BONUS[GET_RANK_FROM_SQUARE[square]]
                            endGameScore += PASSED_PAWN_BONUS[GET_RANK_FROM_SQUARE[square]]
                        }

                    }
                    Piece.N -> {
                        // positional
                        openingScore += positionalScore[GamePhase.OPENING.ordinal][PieceType.KNIGHT.ordinal][square]
                        endGameScore += positionalScore[GamePhase.END_GAME.ordinal][PieceType.KNIGHT.ordinal][square]
                    }
                    Piece.B -> {
                        // positional
                        openingScore += positionalScore[GamePhase.OPENING.ordinal][PieceType.BISHOP.ordinal][square]
                        endGameScore += positionalScore[GamePhase.END_GAME.ordinal][PieceType.BISHOP.ordinal][square]

//                        // mobility
                        val mobilityCount = (BitBoard.countBits(
                            Attacks.getBishopAttacks(
                                Square.fromIntegerToSquare(square)!!,
                                Board.occupanciesBitboards[Color.BOTH.ordinal]
                            )
                        ) - BISHOP_UNIT)
                        openingScore += mobilityCount * BISHOP_MOBILITY_UNIT_OPENING
                        endGameScore += mobilityCount * BISHOP_MOBILITY_UNIT_ENDGAME
                    }
                    Piece.R -> {
                        // positional
                        openingScore += positionalScore[GamePhase.OPENING.ordinal][PieceType.ROOK.ordinal][square]
                        endGameScore += positionalScore[GamePhase.END_GAME.ordinal][PieceType.ROOK.ordinal][square]

                        // semi-open considiration
                         if (Board.pieceBitboards[Piece.P.ordinal] and fileMasks[square] == 0UL) {
                             openingScore += SEMI_OPEN_FILE_BONUS
                             endGameScore += SEMI_OPEN_FILE_BONUS
                         }
                        // open file considiration
                         if ((Board.pieceBitboards[Piece.P.ordinal] or Board.pieceBitboards[Piece.p.ordinal]) and fileMasks[square] == 0UL) {
                             openingScore += OPEN_FILE_SCORE
                             endGameScore += OPEN_FILE_SCORE
                         }
                    }

                    Piece.Q -> {
                        // positional
                        openingScore += positionalScore[GamePhase.OPENING.ordinal][PieceType.QUEEN.ordinal][square]
                        endGameScore += positionalScore[GamePhase.END_GAME.ordinal][PieceType.QUEEN.ordinal][square]

                        // mobility
                         val mobilityCount = (BitBoard.countBits(
                             Attacks.getQueenAttacks(
                                 Square.fromIntegerToSquare(square)!!,
                                 Board.occupanciesBitboards[Color.BOTH.ordinal]
                             )
                         ) - QUEEN_UNIT)
                        openingScore += mobilityCount * QUEEN_MOBILITY_UNIT_OPENING
                        endGameScore += mobilityCount * QUEEN_MOBILITY_UNIT_ENDGAME
                    }

                    Piece.K -> {

                        // positional
                        openingScore += positionalScore[GamePhase.OPENING.ordinal][PieceType.KING.ordinal][square]
                        endGameScore += positionalScore[GamePhase.END_GAME.ordinal][PieceType.KING.ordinal][square]

                         // king semi-open file calculations
                         if (Board.pieceBitboards[Piece.P.ordinal] and fileMasks[square] == 0UL) {
                             openingScore -= SEMI_OPEN_FILE_BONUS
                             endGameScore -= SEMI_OPEN_FILE_BONUS
                         }
                        // king open file calculations
                         if ((Board.pieceBitboards[Piece.P.ordinal] or Board.pieceBitboards[Piece.p.ordinal]) and fileMasks[square] == 0UL) {
                             openingScore -= OPEN_FILE_SCORE
                             endGameScore -= OPEN_FILE_SCORE
                         }
                         // king shield bonus
                         openingScore += BitBoard.countBits(Attacks.kingAttacks[square] and Board.occupanciesBitboards[Color.WHITE.ordinal]) * KING_SHIELD_BONUS
                        endGameScore += BitBoard.countBits(Attacks.kingAttacks[square] and Board.occupanciesBitboards[Color.WHITE.ordinal]) * KING_SHIELD_BONUS
                    }

                    //BLACK
                    Piece.p -> {
                        // positional
                        openingScore -= positionalScore[GamePhase.OPENING.ordinal][PieceType.PAWN.ordinal][mirrorScores[square]]
                        endGameScore -= positionalScore[GamePhase.END_GAME.ordinal][PieceType.PAWN.ordinal][mirrorScores[square]]
                        // double pawns
                        val doublePawns =
                            BitBoard.countBits(Board.pieceBitboards[Piece.p.ordinal] and fileMasks[square])
                        if (doublePawns > 1) {
                            openingScore -= (doublePawns - 1) * DOUBLE_PAWN_PENALTY_OPENING
                            endGameScore -= (doublePawns - 1) * DOUBLE_PAWN_PENALTY_ENDGAME
                        }
                        // isolated pawns
                        if (Board.pieceBitboards[Piece.p.ordinal] and isolatedPawnsMasks[square] == 0UL) {
                            //give an isolated pawn panelty
                            openingScore -= ISOLATED_PAWN_PENALTY_OPENING
                            endGameScore -= ISOLATED_PAWN_PENALTY_ENDGAME
                        }
                        // passed pawns
                        if (blackPassedPawnsMasks[square] and Board.pieceBitboards[Piece.P.ordinal] == 0UL) {
                            openingScore -= PASSED_PAWN_BONUS[GET_RANK_FROM_SQUARE[square]]
                            endGameScore -= PASSED_PAWN_BONUS[GET_RANK_FROM_SQUARE[square]]
                        }


                    }
                    Piece.n -> {
                        // positional
                        openingScore -= positionalScore[GamePhase.OPENING.ordinal][PieceType.KNIGHT.ordinal][mirrorScores[square]]
                        endGameScore -= positionalScore[GamePhase.END_GAME.ordinal][PieceType.KNIGHT.ordinal][mirrorScores[square]]

                    }
                    Piece.b -> {

                        // positional
                        openingScore -= positionalScore[GamePhase.OPENING.ordinal][PieceType.BISHOP.ordinal][mirrorScores[square]]
                        endGameScore -= positionalScore[GamePhase.END_GAME.ordinal][PieceType.BISHOP.ordinal][mirrorScores[square]]

                        // mobility
                        val mobilityCount = (BitBoard.countBits(
                            Attacks.getBishopAttacks(
                                Square.fromIntegerToSquare(square)!!,
                                Board.occupanciesBitboards[Color.BOTH.ordinal]
                            )
                        ) - BISHOP_UNIT)
                        openingScore -= mobilityCount * BISHOP_MOBILITY_UNIT_OPENING
                        endGameScore -= mobilityCount * BISHOP_MOBILITY_UNIT_ENDGAME
                    }
                    Piece.r -> {
                        // positional
                        openingScore -= positionalScore[GamePhase.OPENING.ordinal][PieceType.ROOK.ordinal][mirrorScores[square]]
                        endGameScore -= positionalScore[GamePhase.END_GAME.ordinal][PieceType.ROOK.ordinal][mirrorScores[square]]


                        // semi-open considiration
                        if (Board.pieceBitboards[Piece.p.ordinal] and fileMasks[square] == 0UL) {
                            openingScore -= SEMI_OPEN_FILE_BONUS
                            endGameScore -= SEMI_OPEN_FILE_BONUS
                        }
                        // open file considiration
                        if ((Board.pieceBitboards[Piece.P.ordinal] or Board.pieceBitboards[Piece.p.ordinal]) and fileMasks[square] == 0UL) {
                            openingScore -= OPEN_FILE_SCORE
                            endGameScore -= OPEN_FILE_SCORE
                        }
                    }

                    Piece.q -> {
                        // positional
                        openingScore -= positionalScore[GamePhase.OPENING.ordinal][PieceType.QUEEN.ordinal][mirrorScores[square]]
                        endGameScore -= positionalScore[GamePhase.END_GAME.ordinal][PieceType.QUEEN.ordinal][mirrorScores[square]]

                        // mobility
                        val mobilityCount = (BitBoard.countBits(
                            Attacks.getQueenAttacks(
                                Square.fromIntegerToSquare(square)!!,
                                Board.occupanciesBitboards[Color.BOTH.ordinal]
                            )
                        ) - QUEEN_UNIT)
                        openingScore -= mobilityCount * QUEEN_MOBILITY_UNIT_OPENING
                        endGameScore -= mobilityCount * QUEEN_MOBILITY_UNIT_ENDGAME
                    }

                    Piece.k -> {
                        // positional
                        openingScore -= positionalScore[GamePhase.OPENING.ordinal][PieceType.KING.ordinal][mirrorScores[square]]
                        endGameScore -= positionalScore[GamePhase.END_GAME.ordinal][PieceType.KING.ordinal][mirrorScores[square]]

                        // king semi-open file calculations
                        if (Board.pieceBitboards[Piece.p.ordinal] and fileMasks[square] == 0UL) {
                            openingScore += SEMI_OPEN_FILE_BONUS
                            endGameScore += SEMI_OPEN_FILE_BONUS
                        }
                        // king open file calculations
                        if ((Board.pieceBitboards[Piece.P.ordinal] or Board.pieceBitboards[Piece.p.ordinal]) and fileMasks[square] == 0UL) {
                            openingScore += OPEN_FILE_SCORE
                            endGameScore += OPEN_FILE_SCORE
                        }
                        // king shield bonus
                        openingScore -= BitBoard.countBits(Attacks.kingAttacks[square] and Board.occupanciesBitboards[Color.BLACK.ordinal]) * KING_SHIELD_BONUS
                        endGameScore -= BitBoard.countBits(Attacks.kingAttacks[square] and Board.occupanciesBitboards[Color.BLACK.ordinal]) * KING_SHIELD_BONUS
                    }
                }
                currBitboard = BitBoard.setBitOff(currBitboard, Square.fromIntegerToSquare(square)!!)
            }
        }



        val score = when (gamePhase) {
            GamePhase.MIDDLE_GAME -> {
                // interpulation for middlegame!
                (openingScore * gamePhaseScore + endGameScore * (OPENING_PHASE_SCORE - gamePhaseScore)) / OPENING_PHASE_SCORE
            }
            GamePhase.END_GAME -> {
                endGameScore
            }
            GamePhase.OPENING -> {

                openingScore

            }
        }
        return if (Board.side == Color.WHITE) score else -score
    }
}