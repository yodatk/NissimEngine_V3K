import enums.FENDebugConstants

@ExperimentalUnsignedTypes
var nodes = 0UL
@ExperimentalUnsignedTypes
fun perftDriver(board:Board,depth:Int){
    if(depth == 0){
        nodes++
        return
    }
    var moveList = board.generateMoves()
    for (move in moveList){
        val copy = Board(board)
        if(!board.makeMove(move)){
            continue
        }
        perftDriver(board,depth-1)
        board.copyOtherBoard(copy)
    }

}



@ExperimentalUnsignedTypes
fun runPerftTestOnGivenFEN(fen:String,initialDepth:Int): ULong {
    nodes = 0UL
    Attacks.initAll()
    val b = Board(fen)
    b.printBoard()
    //val movesList: List<Int> = b.generateMoves()
    val start = System.currentTimeMillis()
    perftDriver(b,initialDepth)

    println("time that took for the test (ms): ${System.currentTimeMillis() - start}")
    println("Num of Nodes: $nodes")
    return nodes

}
@ExperimentalUnsignedTypes
fun main() {
    runPerftTestOnGivenFEN(FENDebugConstants.START_POSITION.fen,6)
}