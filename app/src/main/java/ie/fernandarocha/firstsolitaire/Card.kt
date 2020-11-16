package ie.fernandarocha.firstsolitaire

class Card(val number: Int, val suit: Suit, val image: Int, var currentPile: Int = 7){

    override fun toString(): String {
        return this.number.toString() + this.suit;
    }

}