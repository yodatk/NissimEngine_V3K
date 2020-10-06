import enums.FENDebugConstants
import enums.Piece
import enums.Square

@ExperimentalUnsignedTypes
object UCI {
    class UCIException(message: String) : Exception(message)

    fun parseMove(stringInput: String, board: Board): Int {
        val listOfMoves = board.generateMoves()
        val inputSource: Square = Square.fromIntegerToSquare((stringInput[0] - 'a') + (8 - (stringInput[1] - '0')) * 8)
            ?: throw UCIException("invalid source square: ${stringInput.subSequence(0, 2)}")
        val inputTarget: Square = Square.fromIntegerToSquare((stringInput[2] - 'a') + (8 - (stringInput[3] - '0')) * 8)
            ?: throw UCIException("invalid source square: ${stringInput.subSequence(2, 5)}")
        for (move in listOfMoves) {
            if (inputSource == Moves.getSourceFromMove(move) && inputTarget == Moves.getTargetFromMove(move)) {
                val promoted = Moves.getPromotedFromMove(move)
                if (promoted != null) {
                    if (stringInput.length < 5) {
                        return 0 // invalid promotion move
                    }

                    if (
                        (stringInput[4] == 'q' && (promoted == Piece.Q || promoted == Piece.q)) ||
                        (stringInput[4] == 'r' && (promoted == Piece.R || promoted == Piece.r)) ||
                        (stringInput[4] == 'b' && (promoted == Piece.B || promoted == Piece.b)) ||
                        (stringInput[4] == 'n' && (promoted == Piece.N || promoted == Piece.n))
                    ) {
                        // valid promotion -> return move
                        return move
                    } else {
                        //not a valid promotion continue to the next move
                        continue
                    }
                }
                //if not a promotion - the move was found
                return move
            }
        }
        //move was not found-> return 0
        return 0
    }
    fun parsePosition(_command:String) : Board{
        var command = _command.trim()
        var board : Board
        if(command.substring(0,8).equals("position")){
            command = command.substring(9)
            if(command.substring(0,8).equals("startpos")){
                board = Board(FENDebugConstants.START_POSITION.fen)
                command = command.substring(8)
            }
            else{

                if(command.substring(0,3).equals("fen")){
                    command = command.substring(4)
                    board = Board(command)

                }
                else{
                    board=Board(FENDebugConstants.START_POSITION.fen)
                }
            }
            //making additional added moves
            if(command.contains("moves")){
                command = command.substring(7)


                while(!command.isEmpty()){
                    val move = parseMove(command,board)
                    if(move == 0){
                        break
                    }
                    else{
                        val check = board.makeMove(move)
                        if(!check){
                            break
                        }
                        val index = command.indexOf(' ')
                        if (index < 0){
                            break
                        }
                        command = command.substring(index+1)
                    }
                }
            }
            return board
        }
        else{
            println("Nissim: HALLWA INVALID COMMAND HALLLWA HALLWA")
            return Board()
        }
    }
}