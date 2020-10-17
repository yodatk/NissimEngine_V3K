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
    const val INFINITY = 50000
    const val MATE_VALUE = 49000
    const val MATE_SCORE = 48000

    const val MAX_NODE_DEPTH = 64

    const val MAX_PLY = 64

    const val FULL_DEPTH_SEARCH = 4
    const val REDUCTION_LIMIT = 3


    const val WINDOW_INCREMENTOR = 50

    @JvmStatic
    var nodes: ULong = 0UL

    @JvmStatic
    var ply: Int = 0

    @JvmStatic
    var principalVariationLength: Array<Int> = Array<Int>(66) { 0 }

    @JvmStatic
    var principalVariationTable: Array<Array<Int>> = Array<Array<Int>>(66) { Array(66) { 0 } }

    @JvmStatic
    //positions repetiotions table
    var repetitionsTable: Array<ULong> = Array(1000) { 0UL } // number of plys in entire game (500 games in entire game)

    @JvmStatic
    var repetitionsIndex: Int = 0

    @JvmStatic
    fun enablePVScoring(moveList: List<Int>) {
        Evaluation.followPrincipleVariation = false
        for (move in moveList) {
            if (principalVariationTable[0][ply] == move) {
                Evaluation.scorePrincipleVariation = true
                Evaluation.followPrincipleVariation = true
            }
        }

    }

    @JvmStatic
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
    @JvmStatic
    fun quietSearch(_alpha: Int, beta: Int): Int {
        if ((nodes and 2047UL) == 0UL) {
            UCI.communicate()
        }
        nodes++



        if (ply > MAX_PLY - 1) {
            //evaluate poisition
            return Evaluation.evaluate()
        }

        var alpha = _alpha
        val eval = Evaluation.evaluate()
        if (eval >= beta) {
            return beta
        }
        if (eval > alpha) {
            alpha = eval
        }
        val originalMoveList = Board.generateMoves()
        val moveList = Evaluation.sortedPossibleMoves(originalMoveList, ply)



        for (move in moveList) {


//            val boardCopy = Board(board)
            val tempCastle = Board.castle
            val tempSide = Board.side
            val tempEnpassant = Board.enpassant
            val tempPieceBitboards = arrayOf(
                Board.pieceBitboards[0],
                Board.pieceBitboards[1], Board.pieceBitboards[2],
                Board.pieceBitboards[3],
                Board.pieceBitboards[4],
                Board.pieceBitboards[5],
                Board.pieceBitboards[6],
                Board.pieceBitboards[7],
                Board.pieceBitboards[8],
                Board.pieceBitboards[9],
                Board.pieceBitboards[10],
                Board.pieceBitboards[11]
            )

            val tempOccupanciesBitboards = arrayOf(
                Board.occupanciesBitboards[0],
                Board.occupanciesBitboards[1],
                Board.occupanciesBitboards[2],
            )
            val tempHashKey = Board.hashKey
            ply++
            //updating in repetitionsTable
            repetitionsIndex++
            repetitionsTable[repetitionsIndex] = Board.hashKey


            if (!Board.makeMove(move, isCapturesOnly = true)) {
                ply--
                //updating in repetitionsTable
                repetitionsIndex--
                continue
            }

            val score = -quietSearch(-beta, -alpha)

            ply--
            //updating in repetitionsTable
            repetitionsIndex--
            Board.copyOtherBoard(tempCastle,tempSide,tempEnpassant,tempPieceBitboards,tempOccupanciesBitboards,tempHashKey)

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

    @JvmStatic
    fun negamax(_alpha: Int, beta: Int, _depth: Int): Int {

        principalVariationLength[ply] = ply

        var depth = _depth
        var alpha = _alpha

        var currentScore: Int


        var hashFlag = ZorbistKeys.HASH_FLAG_ALPHA

        // if position repetition occurs
        if(ply != 0 && isRepetition(Board.hashKey)){
            // return draw score
            return 0
        }

        // a hack to figure out wether if the current node is PV or not
        val isPvNode = (beta - alpha) > 1

        //read hash entry if not a root ply, and not a pv_node, and hash is available
        currentScore = ZorbistKeys.readHashData(Board.hashKey, alpha, beta, depth, ply)

        if (ply != 0 && currentScore != ZorbistKeys.UNKNOWN_VALUE && !isPvNode) {
            //if the move has alreadey been searched
            // return the score for this move without searching it
            return currentScore
        }

        if ((nodes and 2047UL) == 0UL) {
            UCI.communicate()
        }





        if (depth == 0) {
            return quietSearch(_alpha, beta)
        }


        if (ply > MAX_PLY - 1) {
            //evaluate poisition
            return Evaluation.evaluate()
        }


        nodes++
        val isInCheck =
            if (Board.side == Color.WHITE) {
                Board.isSquareAttacked(
                    Square.fromIntegerToSquare(BitBoard.getLSB(Board.pieceBitboards[Piece.K.ordinal]))!!,
                    Color.BLACK
                )
            } else {
                Board.isSquareAttacked(
                    Square.fromIntegerToSquare(BitBoard.getLSB(Board.pieceBitboards[Piece.k.ordinal]))!!,
                    Color.WHITE
                )
            }
        if (isInCheck) {
            depth++
        }
        var legalMoves = 0

        // null move pruning
        if (depth >= 3 && !isInCheck && ply != 0) {
            val tempCastle = Board.castle
            val tempSide = Board.side
            val tempEnpassant = Board.enpassant
            val tempPieceBitboards = arrayOf(
                Board.pieceBitboards[0],
                Board.pieceBitboards[1], Board.pieceBitboards[2],
                Board.pieceBitboards[3],
                Board.pieceBitboards[4],
                Board.pieceBitboards[5],
                Board.pieceBitboards[6],
                Board.pieceBitboards[7],
                Board.pieceBitboards[8],
                Board.pieceBitboards[9],
                Board.pieceBitboards[10],
                Board.pieceBitboards[11]
            )

            val tempOccupanciesBitboards = arrayOf(
                Board.occupanciesBitboards[0],
                Board.occupanciesBitboards[1],
                Board.occupanciesBitboards[2],
            )
            val tempHashKey = Board.hashKey

            ply++
            //updating in repetitionsTable
            repetitionsIndex++
            repetitionsTable[repetitionsIndex] = Board.hashKey

            // update hash key with enpassant
            if (Board.enpassant != Square.NO_SQUARE) {
                Board.hashKey = Board.hashKey xor ZorbistKeys.enpassantKeys[Board.enpassant.ordinal]
            }

            Board.enpassant = Square.NO_SQUARE

            Board.side = Color.switchSides(Board.side)

            // hash the side
            Board.hashKey = Board.hashKey xor ZorbistKeys.sideKey

            currentScore = -negamax(-beta, -beta + 1, depth - 1 - 2)

            ply--
            //updating in repetitionsTable
            repetitionsIndex--
            Board.copyOtherBoard(tempCastle,tempSide,tempEnpassant,tempPieceBitboards,tempOccupanciesBitboards,tempHashKey)


            if (UCI.isStopped) {
                return 0
            }

            if (currentScore >= beta) {
                return beta
            }
        }

        val originalMoveList = Board.generateMoves()

        if (Evaluation.followPrincipleVariation) {
            enablePVScoring(originalMoveList)
        }

        val movesList = Evaluation.sortedPossibleMoves(originalMoveList, ply)

        // for LMR purposes
        var movesSearched = 0

        for (move in movesList) {
            //backup board
            val tempCastle = Board.castle
            val tempSide = Board.side
            val tempEnpassant = Board.enpassant
            val tempPieceBitboards = arrayOf(
                Board.pieceBitboards[0],
                Board.pieceBitboards[1], Board.pieceBitboards[2],
                Board.pieceBitboards[3],
                Board.pieceBitboards[4],
                Board.pieceBitboards[5],
                Board.pieceBitboards[6],
                Board.pieceBitboards[7],
                Board.pieceBitboards[8],
                Board.pieceBitboards[9],
                Board.pieceBitboards[10],
                Board.pieceBitboards[11]
            )

            val tempOccupanciesBitboards = arrayOf(
                Board.occupanciesBitboards[0],
                Board.occupanciesBitboards[1],
                Board.occupanciesBitboards[2],
            )
            val tempHashKey = Board.hashKey
            ply++
            //updating in repetitionsTable
            repetitionsIndex++
            repetitionsTable[repetitionsIndex] = Board.hashKey

            if (!Board.makeMove(move)) {
                //if move is invalid -> go to different move
                ply--
                //updating in repetitionsTable
                repetitionsIndex--
                continue
            }

            legalMoves++

            if (movesSearched == 0) { // LMR condition
                // no moves searched -> full depth search
                currentScore = -negamax( -beta, -alpha, depth - 1)
            } else {
                // LMR
                // Late Move Reduction
                if (movesSearched >= FULL_DEPTH_SEARCH && depth >= REDUCTION_LIMIT && !isInCheck && !Moves.getCaptureFromMove(
                        move
                    ) && Moves.getPromotedFromMove(move) == null
                ) {
                    // if this move is answring all condition for reduction -> reduct move
                    currentScore = -negamax( -alpha - 1, -alpha, depth - 2)
                } else {
                    currentScore = alpha + 1 // trick to ensure full search
                }

                if (currentScore > alpha) {
                    //LMR failed. try deep the search, with the same alpha-beta width
                    currentScore = -negamax( -alpha - 1, -alpha, depth - 1)
                    if (currentScore in (alpha + 1) until beta) {
                        // LMR failed completely. do a full search
                        currentScore = -negamax( -beta, -alpha, depth - 1)
                    }
                }
            }


            //restoring board
            ply--
            //updating in repetitionsTable
            repetitionsIndex--
            Board.copyOtherBoard(tempCastle,tempSide,tempEnpassant,tempPieceBitboards,tempOccupanciesBitboards,tempHashKey)

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
                    ZorbistKeys.writeEntry(Board.hashKey, beta, depth, ZorbistKeys.HASH_FLAG_BETA, ply)

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
        ZorbistKeys.writeEntry(Board.hashKey, alpha, depth, hashFlag, ply)

        return alpha
    }
    @JvmStatic
    fun generatePrincipleVariationString(): String {
        val builder = StringBuilder("pv ")
        var i = 0
        while (principalVariationTable[0][i] != 0) {
            builder.append("${Moves.moveUCI(principalVariationTable[0][i])} ")
            i++
        }
        return builder.toString()
    }
    @JvmStatic
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

    @JvmStatic
    fun searchPosition(depth: Int) {
        resetDataBeforeSearch()
        var score: Int
        var alpha = -INFINITY
        var beta = INFINITY
        for (currentDepth in 1..depth) {
            if (UCI.isStopped) {
                break
            }
            Evaluation.followPrincipleVariation = true

            score = negamax(alpha, beta, currentDepth)

            // fell out of bounds-> try again with initial values
            if (score <= alpha || score >= beta) {
                alpha = -INFINITY
                beta = INFINITY
                continue
            }
            alpha = score - WINDOW_INCREMENTOR
            beta = score + WINDOW_INCREMENTOR

            if(principalVariationLength[0] != 0){
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