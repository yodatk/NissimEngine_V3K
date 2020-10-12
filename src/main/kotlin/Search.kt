
import enums.Color
import enums.Piece
import enums.Square
import kotlin.coroutines.suspendCoroutine

@ExperimentalUnsignedTypes
object Search {

    //var ply : Int = 0

    val MAX_PLY = 64


    var nodes: ULong = 0UL

    var ply = 0

    var principalVariationLength: Array<Int> = Array<Int>(64) { 0 }

    var principalVariationTable: Array<Array<Int>> = Array<Array<Int>>(64) { Array(64) { 0 } }




    fun enablePVScoring(moveList: List<Int>) {
        Evaluation.followPrincipleVariation = false
        for (move in moveList) {
            if (principalVariationTable[0][ply] == move) {
                Evaluation.scorePrincipleVariation = true
                Evaluation.followPrincipleVariation = true
            }
        }

    }


    fun quietSearch(board: Board, _alpha: Int, beta: Int): Int {
        nodes++
        var alpha = _alpha
        val eval = Evaluation.evaluate(board)
        if (eval >= beta) {
            return beta
        }
        if (eval > alpha) {
            alpha = eval
        }
        val originalMoveList = board.generateMoves()
        val moveList = Evaluation.sortedPossibleMoves(board, originalMoveList,ply)

        for (move in moveList) {
            val boardCopy = Board(board)
            ply++
            if (!board.makeMove(move, isCapturesOnly = true)) {
                ply--
                continue
            }
            val score = -quietSearch(board, -beta, -alpha)
            ply--
            board.copyOtherBoard(boardCopy)
            if (score >= beta) {
                return beta
            }
            if (score > alpha) {
                alpha = score
            }

        }
        return alpha

    }

    fun negamax(board: Board, _alpha: Int, beta: Int, _depth: Int): Int {
        var depth = _depth
        var alpha = _alpha

        var isPVNodeFound = false

        principalVariationLength[ply] = ply

        if (depth == 0) {
            return quietSearch(board, _alpha, beta)
        }

        if (ply > MAX_PLY - 1) {
            //evaluate poisition
            return Evaluation.evaluate(board)
        }
        nodes++
        var isInCheck =
            if (board.side == Color.WHITE) {
                board.isSquareAttacked(
                    Square.fromIntegerToSquare(BitBoard.getLSB(board.pieceBitboards[Piece.K.ordinal]))!!,
                    Color.BLACK
                )
            } else {
                board.isSquareAttacked(
                    Square.fromIntegerToSquare(BitBoard.getLSB(board.pieceBitboards[Piece.k.ordinal]))!!,
                    Color.WHITE
                )
            }
        if (isInCheck) {
            depth++
        }
        var legalMoves = 0

        val originalMoveList = board.generateMoves()
        if(Evaluation.followPrincipleVariation){
            enablePVScoring(originalMoveList)
        }
        val movesList = Evaluation.sortedPossibleMoves(board,originalMoveList ,ply)
        for (move in movesList) {
            //backup board
            val boardCopy = Board(board)
            ply++

            if (!board.makeMove(move)) {
                //if move is invalid -> go to different move
                ply--
                continue
            }

            legalMoves++
            var currentScore : Int
            if(isPVNodeFound){
                // if we find a principal variation node -> try to minimize range of search:
                currentScore = -negamax(board,(-alpha-1),-alpha,depth-1)

                if(currentScore in (alpha + 1) until beta){
                    currentScore = -negamax(board, -beta, -alpha, depth - 1)
                }
            }
            else{
                currentScore = -negamax(board, -beta, -alpha, depth - 1)
            }


            //restoring board
            ply--


            board.copyOtherBoard(boardCopy)

            //fail hard betta cutoff
            if (currentScore >= beta) {
                if (Moves.getCaptureFromMove(move)) {
                    //if quiet move -> store it
                    Evaluation.killerMoves[1][ply] = Evaluation.killerMoves[0][ply]
                    Evaluation.killerMoves[0][ply] = move
                }

                return beta
            }
            //found a better move
            if (currentScore > alpha) {
                if (Moves.getCaptureFromMove(move)) {
                    //if quiet move -> store in in history
                    Evaluation.historyMoves[Moves.getPieceFromMoveAsInt(move)][Moves.getTargetFromMoveAsInt(move)] += depth

                }

                //principal variation node
                alpha = currentScore

                // turn found flag on to minimize the move search
                isPVNodeFound = true

                // write principle variation move
                principalVariationTable[ply][ply] = move

                var next = ply + 1
                while (next < principalVariationLength[ply + 1]) {
                    principalVariationTable[ply][next] = principalVariationTable[ply + 1][next]
                    next++
                }
                principalVariationLength[ply] = principalVariationLength[ply + 1]
            }
        }
        if (legalMoves == 0) {
            // checkmate or stalemate
            return (if (isInCheck) {
                //checkmate
                -49000 + ply
            } else {
                //stalemate
                0
            })
        }
        return alpha
    }

    fun generatePrincipleVariationString(): String {
        val builder = StringBuilder("pv ")
        var i = 0
        while (i < principalVariationLength[0]) {
            builder.append("${Moves.moveUCI(principalVariationTable[0][i])} ")
            i++
        }
        return builder.toString()
    }

    fun resetDataBeforeSearch() {
        nodes = 0UL

        principalVariationLength = Array<Int>(64) { 0 }

        principalVariationTable = Array<Array<Int>>(64) { Array(64) { 0 } }

        Evaluation.resetHistoryAndKillerMoves()

        Evaluation.followPrincipleVariation = false
        Evaluation.scorePrincipleVariation = false
    }


    fun searchPosition(board: Board, depth: Int) {
        resetDataBeforeSearch()
        for (currentDepth in 1..depth) {
            Evaluation.followPrincipleVariation = true
            val score = negamax(board, -50000, 50000, currentDepth)

            println("info score cp $score depth $currentDepth nodes $nodes ${generatePrincipleVariationString()}")
        }
        println("bestmove ${Moves.moveUCI(principalVariationTable[0][0])}\n")
    }

}
