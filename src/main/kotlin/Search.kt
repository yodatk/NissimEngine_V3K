import enums.Color
import enums.Piece
import enums.Square
import kotlin.coroutines.suspendCoroutine

@ExperimentalUnsignedTypes
object Search {

    var ply : Int = 0

    var bestMove :Int = 0

    var nodes : ULong = 0UL

    fun negamax(board: Board, _alpha:Int, beta:Int, depth:Int) : Int{
        var alpha = _alpha
        if(depth == 0){
            return Evaluation.evaluate(board)
        }
        nodes++
        var legalMoves = 0
        val isInCheck = if (board.side == Color.WHITE) board.isSquareAttacked(Square.fromIntegerToSquare(BitBoard.getLSB(board.pieceBitboards[Piece.K.ordinal]))!!,Color.BLACK)
            else board.isSquareAttacked(Square.fromIntegerToSquare(BitBoard.getLSB(board.pieceBitboards[Piece.K.ordinal]))!!,Color.WHITE)
        var bestSoFar = 0
        var oldAlpha = alpha

        val movesList = board.generateMoves()
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
            val currentScore : Int = -negamax(Board(board),-beta,-alpha,depth-1)

            //restoring board
            ply--
            board.copyOtherBoard(boardCopy)

            //fail hard betta cutoff
            if(currentScore >= beta){
                return beta
            }
            //found a better move
            if(currentScore > alpha){
                alpha = currentScore
                if(ply == 0){
                    bestSoFar = move
                }
            }
        }
        if(legalMoves == 0){
            // checkmate or stalemate
            return if(isInCheck){
                //checkmate
                -49000+ply
            } else{
                //stalemate
                0
            }
        }
        if(oldAlpha != alpha){
            //found a better move
                bestMove = bestSoFar
        }
        return alpha
    }


    fun searchPosition(board:Board,depth: Int) {
        ply = 0
        bestMove = 0
        nodes = 0UL
        val score = negamax(board,-50000,50000,depth)
        // bestmove place holder
        if(bestMove!=0){
            println("info score cp $score depth $depth nodes $nodes")
            println("bestmove ${Moves.moveUCI(bestMove)}\n")
        }

    }

}