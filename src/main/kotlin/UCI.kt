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

    const val VERSION = "3.0"

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
     * Time flag
     */
    var isTime = false

    /**
     * how much time there is for a move
     */
    var time : ULong = 0UL

    /**
     * how much time to increment
     */
    var increment = 0

    /**
     * for time measuring purposes
     */
    var startTime: ULong = 0UL

    /**
     * for time measuring purposes
     */
    var stopTime: ULong = 0UL

    /**
     * flag to determine if there is time control
     */
    var isTimeSet = false

    /**
     * flag to determine if the engine need to stop (becuase of time or because of interruptions
     */
    var isStopped = false

    val MAX_HASH = 128
    val DEFAULT_HASH = 64
    val MIN_HASH = 4

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
        if (isTimeSet && System.currentTimeMillis().toULong() > stopTime) {
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
        val board: Board
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
                val current = command.substringAfter("fen ","")
                if (current != "") {
//                    try {
//                        //command = command.substring(command.indexOf(" ") + 1)
//                    } catch (e: Exception) {
//                        throw UCIException("Invalid posotion command: '$_command'")
//                    }
                    board = Board(current)

                } else {
                    board = Board(FENDebugConstants.START_POSITION.fen)
                }
            }
            //making additional added moves
            if (command.contains("moves")) {
                try {
                    command = command.substringAfter("moves ","")
                } catch (e: Exception) {
                    throw UCIException("Invalid position command: '$_command'")
                }


                while (command.isNotEmpty()) {
                    val move = parseMove(command, board)
                    if (move == 0) {
                        break
                    } else {

                        // increment repetition index
                        Search.repetitionsIndex++
                        // write hash key into repetition table
                        Search.repetitionsTable[Search.repetitionsIndex] = board.hashKey
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
        resetTimeControl()
        val command = _command
        var depth = -1
        var current: String

        // infinite search
        if (command.contains("infinite")) {
            /* no-op */
        }

        // adding increment to black command
        current = command.substringAfter("binc ", "")
        current = current.substringBefore(" ", current)
        if (current.isNotEmpty() && board.side == Color.BLACK) {
            increment = try {
                current.toInt()
            } catch (e: Exception) {
                0
            }
        }

        // adding increment to white command
        current = command.substringAfter("winc ", "")
        current = current.substringBefore(" ", current)
        if (current.isNotEmpty() && board.side == Color.WHITE) {
            increment = try {
                current.toInt()
            } catch (e: Exception) {
                0
            }
        }

        // time remaining for white
        current = command.substringAfter("wtime ", "")
        current = current.substringBefore(" ", current)
        if (current.isNotEmpty() && board.side == Color.WHITE) {
            isTime = true
            time = try {
                current.toULong()
            } catch (e: Exception) {
                0UL
            }
        }
        // time remaining for black
        current = command.substringAfter("btime ", "")
        current = current.substringBefore(" ", current)
        if (current.isNotEmpty() && board.side == Color.BLACK) {
            isTime = true
            time = try {
                current.toULong()
            } catch (e: Exception) {
                0UL
            }
        }
        // cathing moves to go
        current = command.substringAfter("movestogo ", "")
        current = current.substringBefore(" ", current)
        if (current.isNotEmpty()) {
            movesToGo = try {
                current.toInt()
            } catch (e: Exception) {
                1
            }
        }
        // catching how much time to calculate
        current = command.substringAfter("movetime ", "")
            current = current.substringBefore(" ", current)
        if (current.isNotEmpty()) {
            moveTime = try {
                current.toInt()
            } catch (e: Exception) {
                3
            }
        }
        // catching depth to calculate
        current = command.substringAfter("depth ", "")
            current = current.substringBefore(" ", current)
        if (current.isNotEmpty()) {
            depth = try {
                current.toInt()
            } catch (e: Exception) {
                Search.MAX_NODE_DEPTH
            }
        }



        if (moveTime != -1) {
            // if move time is available
            time = moveTime.toULong()
            movesToGo = 1
        }
        startTime = System.currentTimeMillis().toULong()

        if (isTime) {
            //there is time limit
            isTimeSet = true
            time /= movesToGo.toULong()
            if(time > 1500UL){
                time -= 50UL
            }
            stopTime = startTime + time + increment.toULong()
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

    fun resetTimeControl(){
        isQuit = false
        movesToGo = 30
        moveTime =-1
        isTime = false
        time = 0UL
        increment = 0
        startTime = 0UL
        stopTime = 0UL
        isTimeSet = false
        isStopped = false
    }

    fun printInfo() {
        println("id name Nissim $VERSION\n" +
                "id author yodatk\n" +
                "option name Hash type spin default $DEFAULT_HASH min $MIN_HASH max $MAX_HASH\n"+
                "uciok")
    }

    fun uciLoop() {

        var input: String?
        System.out.flush()
        printInfo()
        while (true) {
            System.out.flush()
            input = readLine()!!
            var temp = input.substringAfter("setoption name Hash value ", "")
            if(temp.isNotEmpty()){
                temp = temp.substringBefore(" ","").trim()
                val mb = if(temp.isNotEmpty()){
                   try {
                        val c = temp.toInt()
                       when {
                           c < MIN_HASH -> {
                               MIN_HASH
                           }
                           c > MAX_HASH -> {
                               MAX_HASH
                           }
                           else -> {
                               c
                           }
                       }

                    } catch (e: Exception) {
                        DEFAULT_HASH
                    }
                }
                else{
                    DEFAULT_HASH
                }
                println("   Set hash table size to $mb")
                ZorbistKeys.initHashTable(mb)
            }
            else{
                when (input.substringBefore(" ", input)) {
                    "\n", "" -> continue
                    "isready" -> {
                        println("readyok")
                        continue
                    }
                    "position" -> {
                        parsePosition(input)
                        ZorbistKeys.clearHashTable()
                    }
                    "ucinewgame" -> {
                        parsePosition("position startpos\n")
                        ZorbistKeys.clearHashTable()
                    }
                    "go" -> parseGoCommand(input.substringAfter("go "))
                    "quit" -> break
                    "uci" -> printInfo()
                    else -> println("NOT VALID COMMAND")
                }

            }

        }
    }
}