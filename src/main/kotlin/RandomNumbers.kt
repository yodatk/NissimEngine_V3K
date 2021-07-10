object RandomNumbers {
    /**
     * Initial Uint number for random 32 bit numbers
     */
    @JvmStatic
    var uIntRandomState: UInt = 1804289383U

    @JvmStatic
    fun getRandomUIntNumber(): UInt {
        var current: UInt = uIntRandomState
        current = current xor (current shl 13)
        current = current xor (current shr 17)
        current = current xor (current shl 5)
        uIntRandomState = current
        return uIntRandomState

    }

    @JvmStatic
    fun getRandomULongNumber(): ULong {
        val n1: ULong = (getRandomUIntNumber().toULong()) and 65535U
        val n2: ULong = (getRandomUIntNumber().toULong()) and 65535U
        val n3: ULong = (getRandomUIntNumber().toULong()) and 65535U
        val n4: ULong = (getRandomUIntNumber().toULong()) and 65535U

        val output : ULong= n1 or (n2 shl 16) or (n3 shl 32) or (n4 shl 48)
        return output
    }
}