import enums.Color
import enums.Piece
import enums.Square

@ExperimentalUnsignedTypes
object Search {


    /**
     * Score bounds for the range of the mating scores
     *
     *                                           Score Layout
     *
     *          [ -INFINITY,-MATE_VALUE, .... -MATE_SCORE ...... score ...... MATE_SCORE ... MATE_VALUE ... INFINITY ]
     */
    val INFINITY = 50000
    val MATE_VALUE = 49000
    val MATE_SCORE = 48000

    val MAX_NODE_DEPTH = 64

    val MAX_PLY = 64

    val FULL_DEPTH_SEARCH = 4
    val REDUCTION_LIMIT = 3


    val WINDOW_INCREMENTOR = 50


    var nodes: ULong = 0UL

    var ply: Int = 0

    var principalVariationLength: Array<Int> = Array<Int>(66) { 0 }

    var principalVariationTable: Array<Array<Int>> = Array<Array<Int>>(66) { Array(66) { 0 } }

    //positions repetiotions table
    var repetitionsTable: Array<ULong> = Array(1000) { 0UL } // number of plys in entire game (500 games in entire game)

    var repetitionsIndex: Int = 0


    fun enablePVScoring(moveList: List<Int>) {
        Evaluation.followPrincipleVariation = false
        for (move in moveList) {
            if (principalVariationTable[0][ply] == move) {
                Evaluation.scorePrincipleVariation = true
                Evaluation.followPrincipleVariation = true
            }
        }

    }

    // position repetition detections
    fun isRepetition(hashKey:ULong): Boolean{
        // loop over repetition range
        var i = 0
        while (i < repetitionsIndex){
            // if we found same hash key ...
            if(repetitionsTable[i] == hashKey){
                // found a repetition
                return true
            }
            i++
        }

        // if no repetition is found
        return false
    }

    fun quietSearch(board: Board, _alpha: Int, beta: Int): Int {
        if ((nodes and 2047UL) == 0UL) {
            UCI.communicate()
        }
        nodes++



        if (ply > MAX_PLY - 1) {
            //evaluate poisition
            return Evaluation.evaluate(Board(board))
        }

        var alpha = _alpha
        val eval = Evaluation.evaluate(Board(board))
        if (eval >= beta) {
            return beta
        }
        if (eval > alpha) {
            alpha = eval
        }
        val originalMoveList = board.generateMoves()
        val moveList = Evaluation.sortedPossibleMoves(Board(board), originalMoveList, ply)



        for (move in moveList) {


            val boardCopy = Board(board)
            ply++
            //updating in repetitionsTable
            repetitionsIndex++
            repetitionsTable[repetitionsIndex] = board.hashKey


            if (!board.makeMove(move, isCapturesOnly = true)) {
                ply--
                //updating in repetitionsTable
                repetitionsIndex--
                continue
            }

            val score = -quietSearch(Board(board), -beta, -alpha)

            ply--
            //updating in repetitionsTable
            repetitionsIndex--
            board.copyOtherBoard(boardCopy)

            if (UCI.isStopped) {
                return 0
            }


            if (score > alpha) {
                alpha = score

                if (score >= beta) {
                    return beta
                }
            }

        }
        return alpha
    }

    fun negamax(board: Board, _alpha: Int, beta: Int, _depth: Int): Int {


        var depth = _depth
        var alpha = _alpha

        var currentScore: Int


        var hashFlag = ZorbistKeys.HASH_FLAG_ALPHA

        // if position repetition occurs
        if(ply != 0 && isRepetition(board.hashKey)){
            // return draw score
            return 0
        }

        // a hack to figure out wether if the current node is PV or not
        val isPvNode = (beta - alpha) > 1

        //read hash entry if not a root ply, and not a pv_node, and hash is available
        currentScore = ZorbistKeys.readHashData(board.hashKey, alpha, beta, depth, ply)
        if (ply != 0 && currentScore != ZorbistKeys.UNKOWN_VALUE && !isPvNode) {
            //if the move has alreadey been searched
            // return the score for this move without searching it
            return currentScore
        }

        if ((nodes and 2047UL) == 0UL) {
            UCI.communicate()
        }


        principalVariationLength[ply] = ply


        if (depth == 0) {
            return quietSearch(board, _alpha, beta)
        }


        if (ply > MAX_PLY - 1) {
            //evaluate poisition
            return Evaluation.evaluate(board)
        }


        nodes++
        val isInCheck =
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

            ply++
            //updating in repetitionsTable
            repetitionsIndex++
            repetitionsTable[repetitionsIndex] = board.hashKey

            // update hash key with enpassant
            if (board.enpassant != Square.NO_SQUARE) {
                board.hashKey = board.hashKey xor ZorbistKeys.enpassantKeys[board.enpassant.ordinal]
            }

            board.enpassant = Square.NO_SQUARE

            board.side = Color.switchSides(board.side)

            // hash the side
            board.hashKey = board.hashKey xor ZorbistKeys.sideKey

            currentScore = -negamax(board, -beta, -beta + 1, depth - 1 - 2)

            ply--
            //updating in repetitionsTable
            repetitionsIndex--
            board.copyOtherBoard(backup)


            if (UCI.isStopped) {
                return 0
            }

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
            //updating in repetitionsTable
            repetitionsIndex++
            repetitionsTable[repetitionsIndex] = board.hashKey

            if (!board.makeMove(move)) {
                //if move is invalid -> go to different move
                ply--
                //updating in repetitionsTable
                repetitionsIndex--
                continue
            }

            legalMoves++

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
            //updating in repetitionsTable
            repetitionsIndex--
            board.copyOtherBoard(boardCopy)

            if (UCI.isStopped) {
                return 0
            }

            movesSearched++


            //found a better move
            if (currentScore > alpha) {

                //switch hash flag from storing score for fail low node, to PV node
                hashFlag = ZorbistKeys.HASH_FLAG_EXACT


                if (!Moves.getCaptureFromMove(move)) {
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
                //fail hard betta cutoff
                if (currentScore >= beta) {


                    //store hash entry with the score equal to beta
                    ZorbistKeys.writeEntry(board.hashKey, beta, depth, ZorbistKeys.HASH_FLAG_BETA, ply)

                    if (!Moves.getCaptureFromMove(move)) {
                        //if quiet move -> store it
                        Evaluation.killerMoves[1][ply] = Evaluation.killerMoves[0][ply]
                        Evaluation.killerMoves[0][ply] = move
                    }

                    return beta
                }
            }
        }
        if (legalMoves == 0) {
            // checkmate or stalemate
            return (if (isInCheck) {
                //checkmate
                -MATE_VALUE + ply
            } else {
                //stalemate
                0
            })
        }

        //store hash entry with the score equal to alpha
        ZorbistKeys.writeEntry(board.hashKey, alpha, depth, hashFlag, ply)

        return alpha
    }

    fun generatePrincipleVariationString(): String {
        val builder = StringBuilder("pv ")
        var i = 0
        while (principalVariationTable[0][i] != 0) {
            builder.append("${Moves.moveUCI(principalVariationTable[0][i])} ")
            i++
        }
        return builder.toString()
    }

    fun resetDataBeforeSearch() {
        nodes = 0UL//
        UCI.isStopped = false//
        Evaluation.followPrincipleVariation = false//
        Evaluation.scorePrincipleVariation = false//
        Evaluation.resetHistoryAndKillerMoves()//
        principalVariationLength = Array<Int>(66) { 0 }

        principalVariationTable = Array<Array<Int>>(66) { Array(66) { 0 } }

        //ZorbistKeys.clearHashTable()
    }


    fun searchPosition(board: Board, depth: Int) {
        resetDataBeforeSearch()
        var score: Int
        var alpha = -INFINITY
        var beta = INFINITY
        for (currentDepth in 1..depth) {
            if (UCI.isStopped) {
                break
            }
            Evaluation.followPrincipleVariation = true
            score = negamax(board, alpha, beta, currentDepth)
            // fell out of bounds-> try again with initial values
            if (score <= alpha || score >= beta) {
                alpha = -INFINITY
                beta = INFINITY
                continue
            }
            alpha = score - WINDOW_INCREMENTOR
            beta = score + WINDOW_INCREMENTOR

            if(principalVariationLength[0] > 0){
                if (score > -MATE_VALUE && score < -MATE_SCORE) {
                    println(
                        "info score mate ${(-(score + MATE_VALUE) / 2) - 1} depth $currentDepth nodes $nodes time ${
                            System.currentTimeMillis().toULong() - UCI.startTime
                        } ${generatePrincipleVariationString()}"
                    )

                } else if (score > MATE_SCORE && score < MATE_VALUE) {
                    println(
                        "info score mate ${((MATE_VALUE - score) / 2) + 1} depth $currentDepth nodes $nodes time ${
                            System.currentTimeMillis().toULong() - UCI.startTime
                        } ${generatePrincipleVariationString()}"
                    )
                } else {
                    println(
                        "info score cp $score depth $currentDepth nodes $nodes time ${
                            System.currentTimeMillis().toULong() - UCI.startTime
                        } ${generatePrincipleVariationString()}"
                    )
                }
            }

        }
        println("bestmove ${Moves.moveUCI(principalVariationTable[0][0])}\n")
    }

}