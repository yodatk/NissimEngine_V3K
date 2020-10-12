import enums.Color
import enums.Piece
import enums.Square
import kotlin.coroutines.suspendCoroutine

@ExperimentalUnsignedTypes
object Search {

    //var ply : Int = 0

    var bestMove :Int = 0

    var nodes : ULong = 0UL


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
        var bestSoFar = movesList[0]
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
                Evaluation.killerMoves[1][ply] = Evaluation.killerMoves[0][ply]
                Evaluation.killerMoves[0][ply] = move
                return beta
            }
            //found a better move
            if(currentScore > alpha){
                Evaluation.historyMoves[Moves.getPieceFromMoveAsInt(move)][Moves.getTargetFromMoveAsInt(move)] += depth
                alpha = currentScore
                if(ply == 0){
                    bestSoFar = move
                }
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
        if(oldAlpha != alpha){
            //found a better move
                bestMove = bestSoFar
        }
        return alpha
    }


    fun searchPosition(board:Board,depth: Int) {
        bestMove = 0
        nodes = 0UL
        val score = negamax(board,-50000,50000,depth,0)

        // bestmove place holder
        if(bestMove!=0){
            println("info score cp $score depth $depth nodes $nodes")
            println("bestmove ${Moves.moveUCI(bestMove)}\n")
        }

    }

}