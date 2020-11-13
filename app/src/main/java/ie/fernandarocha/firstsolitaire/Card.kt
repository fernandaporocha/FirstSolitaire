package ie.fernandarocha.firstsolitaire

class Card(val number: Int, val suit: Suit, val unicodeValue: String){

    override fun toString(): String {
        return this.unicodeValue
    }
}