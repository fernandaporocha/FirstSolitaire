package ie.fernandarocha.firstsolitaire

class Card(val rank: Int, val suit: Suit, val image: Int, var currentPile: Int = 7, var upFaced:Boolean = true){

    override fun toString(): String {
        return this.rank.toString() + this.suit;
    }

}