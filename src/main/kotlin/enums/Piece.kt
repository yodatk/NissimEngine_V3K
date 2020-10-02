package enums

/**
 * Enum for the pieces. white pieces are going to be capital letters, black letters, original letters
 */
enum class Piece {

    P,
    N,
    B,
    R,
    Q,
    K,
    p,
    n,
    b,
    r,
    q,
    k;

    companion object {

        val whitePieces = arrayOf(P,N,B,R,Q,K)
        val blackPieces = arrayOf(p,n,b,r,q,k)
        val allPieces = arrayOf(P,N,B,R,Q,K,p,n,b,r,q,k)
        fun convertIndexToPiece(i:Int) : Piece?{
            return when(i){
                0 -> P
                1 -> N
                2 -> B
                3 -> R
                4 -> Q
                5 -> K
                6 -> p
                7 -> n
                8 -> b
                9 -> r
                10 -> q
                11 -> k
                else -> null
            }
        }

        fun convertCharToPiece(i:Char) : Piece?{
            return when(i){
                'P' -> P
                'N' -> N
                'B' -> B
                'R' -> R
                'Q' -> Q
                'K' -> K
                'p' -> p
                'n' -> n
                'b' -> b
                'r' -> r
                'q' -> q
                'k' -> k
                else -> null
            }
        }
    }

}