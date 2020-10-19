package enums

enum class CastlingRights(val value:Int) {
    /**
     * White King Side Castling
     */
    WK(1),

    /**
     * White Queen Side Castling
     */
    WQ(2),

    /**
     * Black King Side Castling
     */
    BK(4),

    /**
     * Black Queen Side Castling
     */
    BQ(8)
}