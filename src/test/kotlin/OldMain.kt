import enums.Color
import enums.Square

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
        if (it != Square.NO_SQUARE) {

            Attacks.pawnAttacks[Color.WHITE.value][it.ordinal].printBitboard()
        }
    }

}

@ExperimentalUnsignedTypes
fun checkingKnightsAttacks(args: Array<String>) {
    println("Nissim: HALLAWA!\n===================================\n")
    println("\n\n")
    Attacks.initLeaperAttacks()
    enumValues<Square>().forEach {
        if (it != Square.NO_SQUARE) {

            Attacks.knightAttacks[it.ordinal].printBitboard()
        }
    }
}

@ExperimentalUnsignedTypes
fun checkingKingAttacks(args: Array<String>) {
    println("Nissim: HALLAWA!\n===================================\n")
    println("\n\n")
    Attacks.initLeaperAttacks()
    enumValues<Square>().forEach {
        if (it != Square.NO_SQUARE) {

            Attacks.kingAttacks[it.ordinal].printBitboard()
        }
    }
}


@ExperimentalUnsignedTypes
fun checkBishopMovements(args: Array<String>) {
    println("Nissim: HALLAWA!\n===================================\n")
    println("\n\n")
    Attacks.initLeaperAttacks()
    enumValues<Square>().forEach {
        if (it != Square.NO_SQUARE) {

            Attacks.maskBishopAttacks(it).printBitboard()
        }
    }
}


@ExperimentalUnsignedTypes
fun checkRookMovement(args: Array<String>) {
    println("Nissim: HALLAWA!\n===================================\n")
    println("\n\n")
    Attacks.initLeaperAttacks()
    enumValues<Square>().forEach {
        if (it!=Square.NO_SQUARE) {

            Attacks.maskRookAttacks(it).printBitboard()
        }
    }
}

@ExperimentalUnsignedTypes
fun checkingBishopAndRookOnTheFly(args: Array<String>) {
    println("Nissim: HALLAWA!\n===================================\n")
    println("\n\n")
    //Attacks.initLeaperAttacks()
    var block = BitBoard(0UL)
    block.setBitOn(Square.b7)
    block.setBitOn(Square.c2)
    block.setBitOn(Square.g2)
    block.setBitOn(Square.g6)
    Attacks.bishopAttacksOnTheFly(Square.e4, block).printBitboard()
    block = BitBoard(0UL)
    block.setBitOn(Square.e7)
    block.setBitOn(Square.e2)
    block.setBitOn(Square.b4)
    block.setBitOn(Square.g4)
    Attacks.rookAttacksOnTheFly(Square.e4, block).printBitboard()
}

@ExperimentalUnsignedTypes
fun checkingBitCount(args: Array<String>) {
    println("Nissim: HALLAWA!\n===================================\n")
    println("\n\n")
    //Attacks.initLeaperAttacks()
    var block = BitBoard(0UL)
    block.setBitOn(Square.b7)
    block.setBitOn(Square.c2)
    block.setBitOn(Square.g2)
    block.setBitOn(Square.g6)
    println(block.countBits())
}

@ExperimentalUnsignedTypes
fun checkingGetLSBMethod(args: Array<String>) {
    println("Nissim: HALLAWA!\n===================================\n")
    println("\n\n")
    //Attacks.initLeaperAttacks()
    var block = BitBoard(0UL)
    block.setBitOn(Square.d7)
    block.setBitOn(Square.d2)
    block.setBitOn(Square.d1)
    block.setBitOn(Square.b4)
    block.setBitOn(Square.g4)
    var curr = block.getLSB();
    println("index: $curr square to coordinates ${Square.fromIntegerToSquare(curr)!!.name}")
    val test = BitBoard(0UL);
    test.setBitOn(Square.d7);
    curr = test.getLSB();
    println("index: $curr square to coordinates ${Square.fromIntegerToSquare(curr)!!.name}")

}

@ExperimentalUnsignedTypes
fun checkSetOccupency(args: Array<String>) {
    println("Nissim: HALLAWA!\n===================================\n")
    Attacks.initLeaperAttacks()
    //val attackMask = Attacks.maskBishopAttacks(enums.Square.d4)
    val attackMask = Attacks.maskRookAttacks(Square.d4)

    for (i in 0..100) {
        val occupancy = Attacks.setOccupancy(i, attackMask.countBits(), attackMask);
        occupancy.printBitboard()
        readLine()
    }

}


@ExperimentalUnsignedTypes
fun testingRookAndBishopMovement(args: Array<String>) {
    println("Nissim: HALLAWA!\n===================================\n")
    Attacks.initAll()
    val occupancy = BitBoard()
    occupancy.setBitOn(Square.c5)
    occupancy.setBitOn(Square.f2)
    occupancy.setBitOn(Square.g7)
    occupancy.setBitOn(Square.g5)
    occupancy.setBitOn(Square.e2)
    occupancy.setBitOn(Square.e7)
    occupancy.setBitOn(Square.b2)
    occupancy.setBitOn(Square.h8)
    occupancy.printBitboard()
    Attacks.getBishopAttacks(Square.d4, occupancy).printBitboard()
    Attacks.getRookAttacks(Square.e5, occupancy).printBitboard()
}