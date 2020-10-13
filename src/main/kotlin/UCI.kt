import enums.Color
import enums.FENDebugConstants
import enums.Piece
import enums.Square
import java.io.BufferedReader
import java.io.InputStreamReader


/***
 * Object In Charge on the communication: getting request from client and responds with best moves
 */
@ExperimentalUnsignedTypes
object UCI {
    /**
     * Custom Exception when UCI input was not valid
     */
    class UCIException(message: String) : Exception(message)

    /**
     * flag to determine if to quit searching or not
     */
    var isQuit = false

    /**
     * flag to determine how many moves needs to be done
     */
    var movesToGo = 30

    /**
     * how much time there is for a move
     */
    var moveTime = -1

    /**
     * how much time there is for a move
     */
    var time = -1;

    /**
     * how much time to increment
     */
    var increment = 0;

    /**
     * for time measuring purposes
     */
    var startTime: Long = 0

    /**
     * for time measuring purposes
     */
    var stopTime: Long = 0

    /**
     * flag to determine if there is time control
     */
    var isTimeSet = false

    /**
     * flag to determine if the engine need to stop (becuase of time or because of interruptions
     */
    var isStopped = false

//
//    fun inputWaiting() : Boolean{
//        var input : String
//        val scanner = Scanner(System.`in`)
//        return scanner.hasNext()
//    }


    fun readInput() {
        val inputScanner = BufferedReader(InputStreamReader(System.`in`))
        if (inputScanner.ready()) {
            isStopped = true
            val input = inputScanner.readLine()
            if (input.isNotEmpty()) {
                if (input == "quit" || input == "stop") {
                    isQuit = true
                }
            }
        }
    }


    fun communicate() {
        if (isTimeSet && System.currentTimeMillis() > stopTime) {
            isStopped = true
        }
        readInput()
    }


    var board: Board = Board()

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

    fun parsePosition(_command: String): Board {
        var command = _command.trim()
        var board: Board
        if (command.substring(0, 8).equals("position")) {
            try {
                command = command.substring(command.indexOf(" ") + 1)
            } catch (e: Exception) {
                throw UCIException("Invalid posotion command: '$_command'")
            }

            if (command.substring(0, 8).equals("startpos")) {
                board = Board(FENDebugConstants.START_POSITION.fen)
                try {
                    command = command.substring(command.indexOf(" ") + 1)
                } catch (e: Exception) {
                    throw UCIException("Invalid posotion command: '$_command'")
                }
            } else {

                if (command.substring(0, 3).equals("fen")) {
                    try {
                        command = command.substring(command.indexOf(" ") + 1)
                    } catch (e: Exception) {
                        throw UCIException("Invalid posotion command: '$_command'")
                    }
                    board = Board(command)

                } else {
                    board = Board(FENDebugConstants.START_POSITION.fen)
                }
            }
            //making additional added moves
            if (command.contains("moves")) {
                try {
                    command = command.substring(command.indexOf(" ") + 1)
                } catch (e: Exception) {
                    throw UCIException("Invalid position command: '$_command'")
                }


                while (!command.isEmpty()) {
                    val move = parseMove(command, board)
                    if (move == 0) {
                        break
                    } else {
                        val check = board.makeMove(move)
                        if (!check) {
                            break
                        }
                        val index = command.indexOf(' ')
                        if (index < 0) {
                            break
                        }
                        command = command.substring(index + 1)
                    }
                }
            }
            this.board = board
            this.board.printBoard()
            return this.board
        } else {
            throw UCIException("Invalid position command: '$_command'")
        }
    }

    fun parseGoCommand(_command: String) {
        var command = _command
        var depth = -1
        var current: String

        // infinite search
        if (command.contains("infinite")) {
            // nothing to be done
        }

        // adding increment to black command
        current = command.substringAfter("binc ", "")
        if (current.isNotEmpty() && board.side == Color.BLACK) {
            increment = try {
                current.toInt()
            } catch (e: Exception) {
                0
            }
        }

        // adding increment to white command
        current = command.substringAfter("winc ", "")
        if (current.isNotEmpty() && board.side == Color.WHITE) {
            increment = try {
                current.toInt()
            } catch (e: Exception) {
                0
            }
        }

        // time remaining for white
        current = command.substringAfter("wtime ", "")
        if (current.isNotEmpty() && board.side == Color.WHITE) {
            time = try {
                current.toInt()
            } catch (e: Exception) {
                0
            }
        }
        // time remaining for black
        current = command.substringAfter("btime ", "")
        if (current.isNotEmpty() && board.side == Color.BLACK) {
            time = try {
                current.toInt()
            } catch (e: Exception) {
                0
            }
        }
        // cathing moves to go
        current = command.substringAfter("movestogo ", "")
        if (current.isNotEmpty()) {
            movesToGo = try {
                current.toInt()
            } catch (e: Exception) {
                1
            }
        }
        // catching how much time to calculate
        current = command.substringAfter("movetime ", "")
        if (current.isNotEmpty()) {
            moveTime = try {
                current.toInt()
            } catch (e: Exception) {
                3
            }
        }
        // catching depth to calculate
        current = command.substringAfter("depth ", "")
        if (current.isNotEmpty()) {
            depth = try {
                current.toInt()
            } catch (e: Exception) {
                Search.MAX_NODE_DEPTH
            }
        }


        if (moveTime != -1) {
            // if move time is available
            time = moveTime
            movesToGo = 1
        }
        startTime = System.currentTimeMillis()

        if (time != -1) {
            //there is time limit
            isTimeSet = true
            time /= movesToGo
            time -= 50
            stopTime = startTime + time + increment
        }

        if (depth == -1) {
            depth = Search.MAX_NODE_DEPTH
        }

        println("time:${time} start:${startTime} stop:${stopTime} depth:${depth} timeset:${if (isTimeSet) 1 else 0}")
        Search.searchPosition(board, depth)


//        val command = _command.substringAfter("depth ", "${Search.MAX_NODE_DEPTH}")
//        val depth = try{
//            command.toInt()
//        }catch (e: Exception){
//            Search.MAX_NODE_DEPTH
//        }
//
//        //!!!!!!!!!!!!!!!different time controll place holder!!!!!!!!!!!!
//        Search.searchPosition(board, depth)

    }

    fun printInfo() {
        println("id name Nissim\nid name yodatk\nuciok")
    }

    fun uciLoop() {
        var input: String?
        System.out.flush()
        printInfo()
        while (true) {
            System.out.flush()
            input = readLine()!!
            when (input.substringBefore(" ", input)) {
                "\n", "" -> continue
                "isready" -> {
                    println("readyok")
                    continue
                }
                "position" -> parsePosition(input)
                "ucinewgame" -> parsePosition("position startpos\n")
                "go" -> parseGoCommand(input.substringAfter("go "))
                "quit" -> break
                "uci" -> printInfo()
                else -> println("NOT VALID COMMAND")
            }

        }
    }
}