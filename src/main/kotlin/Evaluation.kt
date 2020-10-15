import enums.Color
import enums.Piece
import enums.Square


@ExperimentalUnsignedTypes
object Evaluation {

    data class MoveWithScore(val move: Int, val score: Int) : Comparable<MoveWithScore> {
        override fun compareTo(other: MoveWithScore): Int {
            return this.score.compareTo(other.score)
        }
    }

    val pawnPositionalScore = arrayOf(
        90, 90, 90, 90, 90, 90, 90, 90,
        30, 30, 30, 40, 40, 30, 30, 30,
        20, 20, 20, 30, 30, 30, 20, 20,
        10, 10, 10, 20, 20, 10, 10, 10,
        5, 5, 10, 20, 20, 5, 5, 5,
        0, 0, 0, 5, 5, 0, 0, 0,
        0, 0, 0, -10, -10, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0
    )

    val knightPositionalScore = arrayOf(
        -5, 0, 0, 0, 0, 0, 0, -5,
        -5, 0, 0, 10, 10, 0, 0, -5,
        -5, 5, 20, 20, 20, 20, 5, -5,
        -5, 10, 20, 30, 30, 20, 10, -5,
        -5, 10, 20, 30, 30, 20, 10, -5,
        -5, 5, 20, 10, 10, 20, 5, -5,
        -5, 0, 0, 0, 0, 0, 0, -5,
        -5, -10, 0, 0, 0, 0, -10, -5
    )

    val bishopPositionalScore = arrayOf(
        0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 10, 10, 0, 0, 0,
        0, 0, 10, 20, 20, 10, 0, 0,
        0, 0, 10, 20, 20, 10, 0, 0,
        0, 10, 0, 0, 0, 0, 10, 0,
        0, 30, 0, 0, 0, 0, 30, 0,
        0, 0, -10, 0, 0, -10, 0, 0
    )

    val rookPositionalScore = arrayOf(
        50, 50, 50, 50, 50, 50, 50, 50,
        50, 50, 50, 50, 50, 50, 50, 50,
        0, 0, 10, 20, 20, 10, 0, 0,
        0, 0, 10, 20, 20, 10, 0, 0,
        0, 0, 10, 20, 20, 10, 0, 0,
        0, 0, 10, 20, 20, 10, 0, 0,
        0, 0, 10, 20, 20, 10, 0, 0,
        0, 0, 0, 20, 20, 0, 0, 0
    )

    val kingPositionalScore = arrayOf(
        0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 5, 5, 5, 5, 0, 0,
        0, 5, 5, 10, 10, 5, 5, 0,
        0, 5, 10, 20, 20, 10, 5, 0,
        0, 5, 10, 20, 20, 10, 5, 0,
        0, 0, 5, 10, 10, 5, 0, 0,
        0, 5, 5, -5, -5, 0, 5, 0,
        0, 0, 5, 0, -15, 0, 10, 0
    )

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

    /**
     * File masks [ square ]
     */
    val fileMasks: Array<ULong> = Array(64) { 0UL }


    /**
     * rank masks [ square ]
     */
    val rankMasks: Array<ULong> = Array(64) { 0UL }

    /**
     * isolated pawns masks [ square ]
     */
    val isolatedPawnsMasks: Array<ULong> = Array(64) { 0UL }

    /**
     * white passed pawns masks [ square ]
     */
    val whitePassedPawnsMasks: Array<ULong> = Array(64) { 0UL }

    /**
     * black passed pawns masks[ square ]
     */
    val blackPassedPawnsMasks: Array<ULong> = Array(64) { 0UL }

    var killerMoves = Array(2) { Array(64) { 0 } }

    var historyMoves = Array(12) { Array(64) { 0 } }

    var followPrincipleVariation = false
    var scorePrincipleVariation = false

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
                while (i<rank){
                    cancel = cancel or rankMasks[i*8 + file]
                    i++
                }
                whitePassedPawnsMasks[square] = whitePassedPawnsMasks[square] and cancel

                // init black passed pawns
                blackPassedPawnsMasks[square] = blackPassedPawnsMasks[square] or setFileRankMask(file - 1, -1)
                blackPassedPawnsMasks[square] = blackPassedPawnsMasks[square] or setFileRankMask(file, -1)
                blackPassedPawnsMasks[square] = blackPassedPawnsMasks[square] or setFileRankMask(file + 1, -1)
                cancel = cancel.inv()
                cancel = cancel and (rankMasks[i*8 + file].inv())
                blackPassedPawnsMasks[square] = blackPassedPawnsMasks[square] and cancel


                                println(Square.fromIntegerToSquare(square))
                BitBoard.printBitboard( blackPassedPawnsMasks[square])


            }
        }
    }

//    fun checkSort(b:Board,ply:Int){
//        val moves = b.generateMoves()
//        val withScoreList = b.generateMoves().map {  MoveWithScore(it, evaluateMoveScore(b,it,ply)) }
//        var flag = false
//        for (i in moves.indices){
//            println("move a: ${Moves.moveUCI(moves[i])} move b: ${Moves.moveUCI(withScoreList[i].move)}")
//            if(moves[i] != withScoreList[i].move){
//                flag = true
//                break
//            }
//        }
//        if(flag){
//            println("NOT SAME")
//        }
//        else{
//            println("SAME")
//        }
//    }


    fun resetHistoryAndKillerMoves() {
        killerMoves = Array(2) { Array(64) { 0 } }

        historyMoves = Array(12) { Array(64) { 0 } }
    }

    fun sortedPossibleMoves(b: Board, movesList: List<Int>, ply: Int): List<Int> {

        val withScoreList = movesList.map { MoveWithScore(it, evaluateMoveScore(b, it, ply)) }.toMutableList()

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
////
//        //quick sort - not implemented yet for debugging
//        return withScoreList.sortedDescending().map{it.move}
    }

    fun printMovesScores(b: Board, ply: Int) {
        val movesList = sortedPossibleMoves(b, b.generateMoves(), ply)
        println("   Moves Scores:\n")
        for (move in movesList) {
            println("     move: ${Moves.moveUCI(move)} score: ${evaluateMoveScore(b, move, ply)}")
        }
    }


    fun evaluateMoveScore(board: Board, move: Int, ply: Int): Int {

        if (scorePrincipleVariation) {
            if (Search.principalVariationTable[0][Search.ply] == move) {
                scorePrincipleVariation = false
                return 20000
            }
        }

        if (Moves.getCaptureFromMove(move)) {
            // score capture move
            var targetPiece = Piece.P.ordinal
            val arr = if (board.side == Color.WHITE) Piece.blackPieces else Piece.whitePieces
            for (piece in arr) {
                if (BitBoard.getBit(board.pieceBitboards[piece.ordinal], Moves.getTargetFromMove(move)) != 0UL) {
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

        return 0
    }

    fun evaluate(board: Board): Int {
        var score = 0
        var currBitboard: ULong
        var square: Int
        for (p in Piece.allPieces) {
            currBitboard = board.pieceBitboards[p.ordinal]
            while (currBitboard != 0UL) {
                square = BitBoard.getLSB(currBitboard)
                score += p.value
                when (p) {
                    //WHITE
                    Piece.P -> score += pawnPositionalScore[square]
                    Piece.N -> score += knightPositionalScore[square]
                    Piece.B -> score += bishopPositionalScore[square]
                    Piece.R -> score += rookPositionalScore[square]
                    Piece.K -> score += kingPositionalScore[square]

                    //BLACK
                    Piece.p -> score -= pawnPositionalScore[mirrorScores[square]]
                    Piece.n -> score -= knightPositionalScore[mirrorScores[square]]
                    Piece.b -> score -= bishopPositionalScore[mirrorScores[square]]
                    Piece.r -> score -= rookPositionalScore[mirrorScores[square]]
                    Piece.k -> score -= kingPositionalScore[mirrorScores[square]]

                    else -> break
                }
                currBitboard = BitBoard.setBitOff(currBitboard, Square.fromIntegerToSquare(square)!!)
            }
        }
        return if (board.side == Color.WHITE) score else -score
    }
}