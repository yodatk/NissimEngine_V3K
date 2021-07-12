package EngineLogic

import EngineLogic.enums.Color
import EngineLogic.enums.FENDebugConstants
import EngineLogic.enums.Piece
import EngineLogic.enums.Square
import java.io.BufferedReader
import java.io.InputStreamReader


/***
 * Object In Charge on the communication: getting request from client and responds with best moves
 */

object UCI {
    /**
     * Custom Exception when UCI input was not valid
     */
    class UCIException(message: String) : Exception(message)

    const val VERSION = "3.0"

    /**
     * flag to determine if to quit searching or not
     */
    @JvmStatic
    var isQuit = false

    /**
     * flag to determine how many moves needs to be done
     */
    @JvmStatic
    var movesToGo = 30

    /**
     * how much time there is for a move
     */
    @JvmStatic
    var moveTime = -1


    /**
     * Time flag
     */
    @JvmStatic
    var isTime = false

    /**
     * how much time there is for a move
     */
    @JvmStatic
    var time: ULong = 0UL

    /**
     * how much time to increment
     */
    @JvmStatic
    var increment = 0

    /**
     * for time measuring purposes
     */
    @JvmStatic
    var startTime: ULong = 0UL

    /**
     * for time measuring purposes
     */
    @JvmStatic
    var stopTime: ULong = 0UL

    /**
     * flag to determine if there is time control
     */
    @JvmStatic
    var isTimeSet = false

    /**
     * flag to determine if the engine need to stop (becuase of time or because of interruptions
     */
    @JvmStatic
    var isStopped = false

    /**
     * Hashing Variables for HashTable size
     */

    const val MAX_HASH = 128
    const val DEFAULT_HASH = 64
    const val MIN_HASH = 4


    /**
     * EngineLogic.Board Field for parsing given Position
     */
    @JvmStatic
    var board: Board = Board()


    /**
     * Checking if there is an input in stdin while searching for a move. if so, act according to input
     */
    @JvmStatic
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


    /**
     * Check for comuunications from UCI client
     */
    @JvmStatic
    fun communicate() {
        if (isTimeSet && System.currentTimeMillis().toULong() > stopTime) {
            isStopped = true
        }
        readInput()
    }


    /**
     * Parse Given move and put it on board
     * @param stringInput: move as string to encode as int
     * @param board: EngineLogic.Board object to perform the move on
     * @return encoded move as integer
     */
    @JvmStatic
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

    /**
     * Parse given position to put on board
     */
    @JvmStatic
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
                val current = command.substringAfter("fen ", "")
                board = if (current != "") {
                    Board(current)

                } else {
                    Board(FENDebugConstants.START_POSITION.fen)
                }
            }
            //making additional added moves
            if (command.contains("moves")) {
                try {
                    command = command.substringAfter("moves ", "")
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
            UCI.board = board
            UCI.board.printBoard()
            return UCI.board
        } else {
            throw UCIException("Invalid position command: '$_command'")
        }
    }

    /**
     * Parse the UCI command
     */
    @JvmStatic
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
            if (time > 1500UL) {
                time -= 50UL
            }
            stopTime = startTime + time + increment.toULong()
        }

        if (depth == -1) {
            depth = Search.MAX_NODE_DEPTH
        }

        println("time:$time start:$startTime stop:$stopTime depth:${depth} timeset:${if (isTimeSet) 1 else 0}")
        Search.searchPosition(board, depth)
    }

    /**
     * reset all variables that's belong to time control before new game
     */
    @JvmStatic
    fun resetTimeControl() {
        isQuit = false
        movesToGo = 30
        moveTime = -1
        isTime = false
        time = 0UL
        increment = 0
        startTime = 0UL
        stopTime = 0UL
        isTimeSet = false
        isStopped = false
    }

    /**
     * Prints Engine Info
     */
    @JvmStatic
    fun printInfo() {
        println(
            "id name Nissim $VERSION\n" +
                    "id author yodatk\n" +
                    "uciok"
        )
    }

    /**
     * Main Loop to communicate with UCI Client
     */
    @JvmStatic
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
                "position" -> {
                    parsePosition(input)
                    // reset hash map only if it's a new game \ from a new position
                    ZorbistKeys.clearHashTable()

                }
                "ucinewgame" -> {
                    parsePosition("position startpos\n")
                    ZorbistKeys.clearHashTable()
                }
                "go" -> parseGoCommand(input.substringAfter("go "))
                "quit" -> break
                "uci" -> printInfo()
                else -> continue //println("NOT VALID COMMAND")
            }

        }

    }
}