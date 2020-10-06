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
            try {
                command = command.substring(command.indexOf(" ")+1)
            }catch (e : Exception){
                throw UCIException("Invalid posotion command: '$_command'")
            }

            if(command.substring(0,8).equals("startpos")){
                board = Board(FENDebugConstants.START_POSITION.fen)
                try {
                    command = command.substring(command.indexOf(" ")+1)
                }catch (e : Exception){
                    throw UCIException("Invalid posotion command: '$_command'")
                }
            }
            else{

                if(command.substring(0,3).equals("fen")){
                    try {
                        command = command.substring(command.indexOf(" ")+1)
                    }catch (e : Exception){
                        throw UCIException("Invalid posotion command: '$_command'")
                    }
                    board = Board(command)

                }
                else{
                    board=Board(FENDebugConstants.START_POSITION.fen)
                }
            }
            //making additional added moves
            if(command.contains("moves")){
                try {
                    command = command.substring(command.indexOf(" ")+1)
                }catch (e : Exception){
                    throw UCIException("Invalid position command: '$_command'")
                }


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
            throw UCIException("Invalid position command: '$_command'")
        }
    }

    fun parseGoCommand(_command: String) {
        var depth = -1
        var command = _command.trim()
        if(command.contains("depth")){
            command = command.substring(command.indexOf(" ")+1)
            depth = command.toInt()
        }
        else{
            depth = 6
        }
        //!!!!!!!!!!!!!!!different time controll place holder!!!!!!!!!!!!

    }
}