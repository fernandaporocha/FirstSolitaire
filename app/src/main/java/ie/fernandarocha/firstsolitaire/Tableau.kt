package ie.fernandarocha.firstsolitaire

class Tableau(val piles: ArrayList<Pile>) {
    fun getSize():Int{
        var i:Int =0;
        for (pile:Pile in this.piles){
            if (pile.cards!=null)
                i+=pile.cards.size
        }
        return i
    }
}