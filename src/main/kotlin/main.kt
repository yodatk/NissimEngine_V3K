/**
================================================
NISSIM ENGINE (HALLAWA)
author: Tomer Gonen
================================================
 */


/**
================================================
Bit Manipulation
================================================
 */




/**
================================================
Main
================================================
 */

//importing setBitOn, SetBitOff, GetBit, printBitBoard from BitBoardManipulation


@ExperimentalUnsignedTypes
fun main(args: Array<String>) {
    println("Nissim: HALLAWA!\n===================================\n")
    var temp: ULong = 0UL
    temp = setBitOn(temp, Square.e4.bit)
    printBitboard(temp);
    temp = setBitOn(temp, Square.e3.bit)
    printBitboard(temp);
    temp = setBitOff(temp, Square.e4.bit)
    printBitboard(temp);

}