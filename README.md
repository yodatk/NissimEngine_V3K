# NissimEngine_V3K
3rd version of Nissim engine working with UCI protocol and Bitboards. written in Kotlin

## Features:
 * Pre-Calculated Moves and Attacks, using MagicBitboards
 * Alpha Beta pruning based search, with:
    - Principal Variation Search 
    - Move ordering by Princiopal variation Nodem, killer moves, and history moves
    - Late Move Reduction
    - Null Move Pruning
 * Smart evaluation Including :
    - Material Evaluation
    - Positional Evaluation
    - Pawn structre and King Saftey
    - Open and semi files considiration
    - Opening \ Middle Game \ End Game Evaluation
 * Transposition Table


## Thanks A Lot to these Wonderful guides: 
- Chess Programming : https://www.youtube.com/watch?v=QUNP-UjujBM&list=PLmN0neTso3Jxh8ZIylk74JpwfiWNI76Cs&ab_channel=ChessProgramming
- Logic Crazy Chess: https://www.youtube.com/watch?v=a5IGltn95Bk&ab_channel=LogicCrazyChess
