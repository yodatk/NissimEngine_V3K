@ExperimentalUnsignedTypes
fun bitManipulationsCheck(args: Array<String>) {
    println("Nissim: HALLAWA!\n===================================\n")
    var temp = BitBoard(0UL)
    temp.setBitOn(Square.e4)
    temp.printBitboard()
    temp.setBitOn(Square.e3)
    temp.printBitboard()
    temp.setBitOff(Square.e4)
    temp.printBitboard()

}