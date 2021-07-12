package EngineLogic

import EngineLogic.RandomNumbers.getRandomULongNumber
import EngineLogic.enums.Square

object MagicNumbers {

    private const val RANDOM_BIG_NUMBER: Int = 99999999

    private const val FF00000000000000: ULong = 71776119061217280UL

    @JvmStatic
    val rookMagicNumbers: Array<ULong> = arrayOf(
        9979994641325359136UL,
        90072129987412032UL,
        180170925814149121UL,
        72066458867205152UL,
        144117387368072224UL,
        216203568472981512UL,
        9547631759814820096UL,
        2341881152152807680UL,
        140740040605696UL,
        2316046545841029184UL,
        72198468973629440UL,
        81205565149155328UL,
        146508277415412736UL,
        703833479054336UL,
        2450098939073003648UL,
        576742228899270912UL,
        36033470048378880UL,
        72198881818984448UL,
        1301692025185255936UL,
        90217678106527746UL,
        324684134750365696UL,
        9265030608319430912UL,
        4616194016369772546UL,
        2199165886724UL,
        72127964931719168UL,
        2323857549994496000UL,
        9323886521876609UL,
        9024793588793472UL,
        562992905192464UL,
        2201179128832UL,
        36038160048718082UL,
        36029097666947201UL,
        4629700967774814240UL,
        306244980821723137UL,
        1161084564161792UL,
        110340390163316992UL,
        5770254227613696UL,
        2341876206435041792UL,
        82199497949581313UL,
        144120019947619460UL,
        324329544062894112UL,
        1152994210081882112UL,
        13545987550281792UL,
        17592739758089UL,
        2306414759556218884UL,
        144678687852232706UL,
        9009398345171200UL,
        2326183975409811457UL,
        72339215047754240UL,
        18155273440989312UL,
        4613959945983951104UL,
        145812974690501120UL,
        281543763820800UL,
        147495088967385216UL,
        2969386217113789440UL,
        19215066297569792UL,
        180144054896435457UL,
        2377928092116066437UL,
        9277424307650174977UL,
        4621827982418248737UL,
        563158798583922UL,
        5066618438763522UL,
        144221860300195844UL,
        281752018887682UL
    )

    @JvmStatic
    val bishopMagicNumbers: Array<ULong> = arrayOf(
        18018832060792964UL,
        9011737055478280UL,
        4531088509108738UL,
        74316026439016464UL,
        396616115700105744UL,
        2382975967281807376UL,
        1189093273034424848UL,
        270357282336932352UL,
        1131414716417028UL,
        2267763835016UL,
        2652629010991292674UL,
        283717117543424UL,
        4411067728898UL,
        1127068172552192UL,
        288591295206670341UL,
        576743344005317120UL,
        18016669532684544UL,
        289358613125825024UL,
        580966009790284034UL,
        1126071732805635UL,
        37440604846162944UL,
        9295714164029260800UL,
        4098996805584896UL,
        9223937205167456514UL,
        153157607757513217UL,
        2310364244010471938UL,
        95143507244753921UL,
        9015995381846288UL,
        4611967562677239808UL,
        9223442680644702210UL,
        64176571732267010UL,
        7881574242656384UL,
        9224533161443066400UL,
        9521190163130089986UL,
        2305913523989908488UL,
        9675423050623352960UL,
        9223945990515460104UL,
        2310346920227311616UL,
        7075155703941370880UL,
        4755955152091910658UL,
        146675410564812800UL,
        4612821438196357120UL,
        4789475436135424UL,
        1747403296580175872UL,
        40541197101432897UL,
        144397831292092673UL,
        1883076424731259008UL,
        9228440811230794258UL,
        360435373754810368UL,
        108227545293391872UL,
        4611688277597225028UL,
        3458764677302190090UL,
        577063357723574274UL,
        9165942875553793UL,
        6522483364660839184UL,
        1127033795058692UL,
        2815853729948160UL,
        317861208064UL,
        5765171576804257832UL,
        9241386607448426752UL,
        11258999336993284UL,
        432345702206341696UL,
        9878791228517523968UL,
        4616190786973859872UL
    )

    @JvmStatic
    fun generateMagicNumber(): ULong {
        return (getRandomULongNumber() and getRandomULongNumber() and getRandomULongNumber())
    }


    @JvmStatic
    fun findMagicNumber(square: Int, relevantBits: Int, isBishop: Boolean): ULong {
        // init occupencies
        val occupancies = Array(4096) { 0UL }

        // init attacks table
        val attacks = Array(4096) { 0UL }

        // init used attacks table
        var usedAttacks = Array(4096) { 0UL }

        // init mask for current piece
        val attackMask = if (isBishop) Attacks.maskBishopAttacks(Square.fromIntegerToSquare(square)!!)
        else Attacks.maskRookAttacks(Square.fromIntegerToSquare(square)!!)
        // init occupency indicies
        val occupancyIndices: Int = (1 shl relevantBits)

        // loop over occupancy indicies
        for (index in 0 until occupancyIndices) {
            //init occupancies
            occupancies[index] = Attacks.setOccupancy(index, relevantBits, attackMask)

            //init attacks
            attacks[index] = if (isBishop) Attacks.bishopAttacksOnTheFly(
                Square.fromIntegerToSquare(square)!!,
                (occupancies[index])
            )
            else Attacks.rookAttacksOnTheFly(Square.fromIntegerToSquare(square)!!, (occupancies[index]))
        }

        //test magic numbers loop
        for (count in 0..RANDOM_BIG_NUMBER) {
            //generate current magic number
            val currentMagicNumber: ULong = generateMagicNumber()

            if (BitBoard.countBits((attackMask * currentMagicNumber) and 0xFF00000000000000U) < 6) {
                // skip invalid values
                continue
            }
            var index = 0
            var fail = false
            // init used attacks table
            usedAttacks = Array(4096) { 0UL }

            while (!fail && index < occupancyIndices) {
                val magicIndex = ((occupancies[index] * currentMagicNumber) shr (64 - relevantBits)).toInt()

                if (usedAttacks[magicIndex] == 0UL) {
                    //if magic index works --> init used attacks
                    usedAttacks[magicIndex] = attacks[index]

                } else if (usedAttacks[magicIndex] != attacks[index]) {
                    fail = true
                }
                index++
            }
            if (!fail) {
                return currentMagicNumber
            }
        }

        println("MAGIC NUMBER FAILED!")
        return 0UL
    }

    /**
     * generate magic numbers for rook and bishop movements calculations, and prints them to screen in format to paste
     * as an ULong array
     */
    @JvmStatic
    fun initMagicNumbers() {
        var temp: ULong
        println("ROOK MAGIC NUMBERS\n========")
        for (square in 0..63) {
            temp = findMagicNumber(square, Attacks.rookRelevantBits[square], false)
            println("${temp}UL,")
            //rookMagicNumbers[square] = temp
        }
        println("\n========\nBISHOP MAGIC NUMBERS\n========")
        for (square in 0..63) {
            temp = findMagicNumber(square, Attacks.bishopRelevantBits[square], true)
            println("${temp}UL,")
            //bishopMagicNumbers[square] = temp
        }

    }

    @JvmStatic
    fun main(args: Array<String>) {
        initMagicNumbers()
    }


}