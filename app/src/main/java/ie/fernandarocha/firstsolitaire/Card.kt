package ie.fernandarocha.firstsolitaire

import android.widget.ImageView

class Card(val rank: Int, val suit: Suit, val image: Int, var currentPile: Int = 7, var upFaced:Boolean = true, var id: Int){

    override fun toString(): String {
        return this.rank.toString() + this.suit
    }

}