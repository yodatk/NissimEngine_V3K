import enums.Color
import enums.Piece
import enums.Square
import kotlin.coroutines.suspendCoroutine

@ExperimentalUnsignedTypes
object Search {

    //var ply : Int = 0



    var nodes : ULong = 0UL

    var principalVariationLength : Array<Int> = Array<Int> (64) {0}

    var principalVariationTable :  Array<Array<Int>> = Array<Array<Int>> (64) {Array(64) {0} }

    fun quietSearch(board:Board,_alpha:Int,beta:Int,_ply: Int) : Int{
        var ply = _ply
        nodes++
        var alpha = _alpha
        val eval = Evaluation.evaluate(board)
        if(eval >= beta){
            return beta
        }
        if(eval > alpha){
            alpha = eval
        }
        val moveList = Evaluation.sortedPossibleMoves(board,ply)
        //val moveList = board.generateMoves()
        for(move in moveList){
            val boardCopy = Board(board)
            ply++
            if(!board.makeMove(move,isCapturesOnly = true)){
                ply--
                continue
            }
            val score = -quietSearch(board,-beta,-alpha,ply)
            ply--
            board.copyOtherBoard(boardCopy)
            if(score >= beta){
                return beta
            }
            if(score > alpha){
                alpha = score
            }

        }
        return alpha

    }

    fun negamax(board: Board, _alpha:Int, beta:Int, _depth:Int,_ply:Int) : Int{
        var ply = _ply
        var depth = _depth
        var alpha = _alpha

        principalVariationLength[ply] = ply
        if(depth == 0){
            return quietSearch(board,_alpha,beta,ply)
        }
        nodes++
        var isInCheck =
        if (board.side == Color.WHITE){
            board.isSquareAttacked(Square.fromIntegerToSquare(BitBoard.getLSB(board.pieceBitboards[Piece.K.ordinal]))!!,Color.BLACK)
        }
            else{
            board.isSquareAttacked(Square.fromIntegerToSquare(BitBoard.getLSB(board.pieceBitboards[Piece.k.ordinal]))!!,Color.WHITE)
        }
        if(isInCheck){
            depth++
        }
        var legalMoves = 0
        var oldAlpha = alpha

        val movesList = Evaluation.sortedPossibleMoves(board,ply)
        for(move in movesList){
            //backup board
            val boardCopy = Board(board)
            ply++

            if(!board.makeMove(move)){
                //if move is invalid -> go to different move
                ply--
                continue
            }

            legalMoves++

            // getting current score
            val currentScore : Int = -negamax(board,-beta,-alpha,depth-1,ply)

            //restoring board
            ply--


            board.copyOtherBoard(boardCopy)

            //fail hard betta cutoff
            if(currentScore >= beta){
                if(Moves.getCaptureFromMove(move)){
                    //if quiet move -> store it
                    Evaluation.killerMoves[1][ply] = Evaluation.killerMoves[0][ply]
                    Evaluation.killerMoves[0][ply] = move
                }

                return beta
            }
            //found a better move
            if(currentScore > alpha){
                if(Moves.getCaptureFromMove(move)){
                    //if quiet move -> store in in history
                    Evaluation.historyMoves[Moves.getPieceFromMoveAsInt(move)][Moves.getTargetFromMoveAsInt(move)] += depth

                }

                //principal variation node
                alpha = currentScore

                // write principle variation move
                principalVariationTable[ply][ply] = move

                var next = ply+1
                while(next < principalVariationLength[ply+1]){
                    principalVariationTable[ply][next] = principalVariationTable[ply+1][next]
                    next++
                }
                principalVariationLength[ply] = principalVariationLength[ply+1]
            }
        }
        if(legalMoves == 0){
            // checkmate or stalemate
            return (if(isInCheck){
                //checkmate
                -49000+ply
            } else{
                //stalemate
                0
            })
        }
        return alpha
    }

    fun generatePrincipleVariationString(): String  {
        val builder = StringBuilder("pv ")
        var i =0
        while(i < principalVariationLength[0]){
            builder.append("${Moves.moveUCI(principalVariationTable[0][i])} ")
            i++
        }
        return builder.toString()
    }


    fun searchPosition(board:Board,depth: Int) {
        nodes = 0UL
        val score = negamax(board,-50000,50000,depth,0)

        // bestmove place holder

            println("info score cp $score depth $depth nodes $nodes ${generatePrincipleVariationString()}")
            println("bestmove ${Moves.moveUCI(principalVariationTable[0][0])}\n")

    }

}