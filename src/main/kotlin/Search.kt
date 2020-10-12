import enums.Color
import enums.Piece
import enums.Square
import kotlin.coroutines.suspendCoroutine

@ExperimentalUnsignedTypes
object Search {

    val MAX_PLY = 64

    val FULL_DEPTH_SEARCH = 4
    val REDUCTION_LIMIT = 3

    val INITIAL_BANDWITH_VALUE = 50000

    val WINDOW_INCREMENTOR = 50


    var nodes: ULong = 0UL

    var ply: Int = 0

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
        val moveList = Evaluation.sortedPossibleMoves(board, originalMoveList, ply)



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

        // null move pruning
        if (depth >= 3 && !isInCheck && ply != 0) {
            val backup = Board(board)
            board.side = Color.switchSides(board.side)
            board.enpassant = Square.NO_SQUARE
            val currentScore = -negamax(board, -beta, -beta + 1, depth - 1 - 2)
            board.copyOtherBoard(backup)
            if (currentScore >= beta) {
                return beta
            }
        }

        val originalMoveList = board.generateMoves()

        if (Evaluation.followPrincipleVariation) {
            enablePVScoring(originalMoveList)
        }

        val movesList = Evaluation.sortedPossibleMoves(board, originalMoveList, ply)

        // for LMR purposes
        var movesSearched = 0

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
            var currentScore: Int
            if (movesSearched == 0) { // LMR condition
                // no moves searched -> full depth search
                currentScore = -negamax(board, -beta, -alpha, depth - 1)
            } else {
                // LMR
                // Late Move Reduction
                if (movesSearched >= FULL_DEPTH_SEARCH && depth >= REDUCTION_LIMIT && !isInCheck && !Moves.getCaptureFromMove(
                        move
                    ) && Moves.getPromotedFromMove(move) == null
                ) {
                    // if this move is answring all condition for reduction -> reduct move
                    currentScore = -negamax(board, -alpha - 1, -alpha, depth - 2)
                } else {
                    currentScore = alpha + 1 // trick to ensure full search
                }

                if (currentScore > alpha) {
                    //LMR failed. try deep the search, with the same alpha-beta width
                    currentScore = -negamax(board, -alpha - 1, -alpha, depth - 1)
                    if (currentScore in (alpha + 1) until beta) {
                        // LMR failed completely. do a full search
                        currentScore = -negamax(board, -beta, -alpha, depth - 1)
                    }
                }
            }


            //restoring board
            ply--
            board.copyOtherBoard(boardCopy)

            movesSearched++

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
        var alpha = -INITIAL_BANDWITH_VALUE
        var beta = INITIAL_BANDWITH_VALUE
        for (currentDepth in 1..depth) {
            Evaluation.followPrincipleVariation = true
            val score = negamax(board, alpha, beta, currentDepth)
            if (score <= alpha || score >= beta) {
                alpha = -INITIAL_BANDWITH_VALUE
                beta = INITIAL_BANDWITH_VALUE
                continue
            }
            alpha = score - WINDOW_INCREMENTOR
            beta = score + WINDOW_INCREMENTOR

            println("info score cp $score depth $currentDepth nodes $nodes ${generatePrincipleVariationString()}")
        }
        println("bestmove ${Moves.moveUCI(principalVariationTable[0][0])}\n")
    }

}