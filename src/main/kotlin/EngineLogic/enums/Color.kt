package EngineLogic.enums

enum class Color {
    WHITE, BLACK, BOTH;

    companion object{
        fun switchSides(color: Color): Color {
            return when(color){
                BLACK -> WHITE
                WHITE -> BLACK
                BOTH -> BOTH
            }
        }
    }
}