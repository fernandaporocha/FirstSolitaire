package ie.fernandarocha.firstsolitaire

class Pile(val position: Int, val cards: ArrayList<Card>) {
    fun getColumn():Int{
        return this.position-5
    }
}