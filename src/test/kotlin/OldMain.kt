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
@ExperimentalUnsignedTypes
fun checkingPawnsAttacks(args: Array<String>) {
    println("Nissim: HALLAWA!\n===================================\n")
    println("\n\n")
    Attacks.initLeaperAttacks()
    enumValues<Square>().forEach {
        Attacks.pawnAttacks[Color.WHITE.value][it.bit].printBitboard()
    }
//    enumValues<Square>().forEach {
//        Attacks.pawnAttacks[Color.BLACK.value][it.bit]
//    }

}

@ExperimentalUnsignedTypes
fun checkingKnightsAttacks(args: Array<String>) {
    println("Nissim: HALLAWA!\n===================================\n")
    println("\n\n")
    Attacks.initLeaperAttacks()
    enumValues<Square>().forEach {
        Attacks.knightAttacks[it.bit].printBitboard()
    }
}

@ExperimentalUnsignedTypes
fun checkingKingAttacks(args: Array<String>) {
    println("Nissim: HALLAWA!\n===================================\n")
    println("\n\n")
    Attacks.initLeaperAttacks()
    enumValues<Square>().forEach {
        Attacks.kingAttacks[it.bit].printBitboard()
    }
}