object RandomNumbers {
    /**equal to 0xFFFF*/
    const val FFFF = 65535

    /**
     * Initial Uint number for random 32 bit numbers
     */
    @ExperimentalUnsignedTypes
    var uIntRandomState: UInt = 1804289383.toUInt()

    @ExperimentalUnsignedTypes
    fun getRandomUIntNumber(): UInt {
        var current: UInt = uIntRandomState
        current = current xor (current shl 13)
        current = current xor (current shr 17)
        current = current xor (current shl 5)
        uIntRandomState = current
        return uIntRandomState

    }

    @ExperimentalUnsignedTypes
    fun getRandomULongNumber(): ULong {
        val n1 = (getRandomUIntNumber() and FFFF.toUInt()).toULong()
        val n2 = (getRandomUIntNumber() and FFFF.toUInt()).toULong()
        val n3 = (getRandomUIntNumber() and FFFF.toUInt()).toULong()
        val n4 = (getRandomUIntNumber() and FFFF.toUInt()).toULong()

        return (n1 or (n2 shl 16) or (n3 shl 32) or (n4 shl 48))
    }

    @ExperimentalUnsignedTypes
    fun generateMagicNumber(): ULong {
        return (getRandomULongNumber() and getRandomULongNumber() and getRandomULongNumber())
    }


}