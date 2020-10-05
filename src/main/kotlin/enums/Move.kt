package enums

/**
 * Moves are encoded as int in the following manner
binary move bits                                   hexidecimal constants

0000 0000 0000 0000 0011 1111    source square       0x3f
0000 0000 0000 1111 1100 0000    target square       0xfc0
0000 0000 1111 0000 0000 0000    piece               0xf000
0000 1111 0000 0000 0000 0000    promoted piece      0xf0000
0001 0000 0000 0000 0000 0000    capture flag        0x100000
0010 0000 0000 0000 0000 0000    double push flag    0x200000
0100 0000 0000 0000 0000 0000    enpassant flag      0x400000
1000 0000 0000 0000 0000 0000    castling flag       0x800000

 */
enum class Move(val flag: Int, val shift: Int) {
    SOURCE(flag = 0x3f, 0),
    TARGET(flag = 0xfc0, shift = 6),
    PIECE(flag = 0xf000, shift = 12),
    PROMOTED(flag = 0xf0000, shift = 16),
    CAPTURE(flag = 0x100000, shift = 20),
    DOUBLE(flag = 0x200000, shift = 21),
    EN_PASSANT(flag = 0x400000, shift = 22),
    CASTLING(flag = 0x800000, shift = 23);

}