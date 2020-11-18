package ie.fernandarocha.firstsolitaire

import kotlin.random.Random

class Board {
    var cards = ArrayList<Card>()
    var waste = Pile(7, ArrayList<Card>())
    var tableau = Tableau(ArrayList<Pile>())
    var foundation = Foundation(ArrayList<Pile>())

    init {
        createAllCards()
        cards.shuffle()
        createTableauPiles()
        printTableau()
        createFoundationPiles()

    }

    private fun createAllCards() {
        println("createAllCards")
        createDiamondCards()
        createSpadeCards()
        createHeartCards()
        createClubCards()
    }

    private fun createDiamondCards(){
        cards.add(Card(1, Suit.DIAMOND, R.drawable.d1))
        cards.add(Card(2, Suit.DIAMOND, R.drawable.d2))
        cards.add(Card(3, Suit.DIAMOND, R.drawable.d3))
        cards.add(Card(4, Suit.DIAMOND, R.drawable.d4))
        cards.add(Card(5, Suit.DIAMOND, R.drawable.d5))
        cards.add(Card(6, Suit.DIAMOND, R.drawable.d6))
        cards.add(Card(7, Suit.DIAMOND, R.drawable.d7))
        cards.add(Card(8, Suit.DIAMOND, R.drawable.d8))
        cards.add(Card(9, Suit.DIAMOND, R.drawable.d9))
        cards.add(Card(10, Suit.DIAMOND, R.drawable.d10))
        cards.add(Card(11, Suit.DIAMOND, R.drawable.dj))
        cards.add(Card(12, Suit.DIAMOND, R.drawable.dq))
        cards.add(Card(13, Suit.DIAMOND, R.drawable.dk))
    }

    private fun createSpadeCards(){
        cards.add(Card(1, Suit.SPADE, R.drawable.s1))
        cards.add(Card(2, Suit.SPADE, R.drawable.s2))
        cards.add(Card(3, Suit.SPADE, R.drawable.s3))
        cards.add(Card(4, Suit.SPADE, R.drawable.s4))
        cards.add(Card(5, Suit.SPADE, R.drawable.s5))
        cards.add(Card(6, Suit.SPADE, R.drawable.s6))
        cards.add(Card(7, Suit.SPADE, R.drawable.s7))
        cards.add(Card(8, Suit.SPADE, R.drawable.s8))
        cards.add(Card(9, Suit.SPADE, R.drawable.s9))
        cards.add(Card(10, Suit.SPADE, R.drawable.s10))
        cards.add(Card(11, Suit.SPADE, R.drawable.sj))
        cards.add(Card(12, Suit.SPADE, R.drawable.sq))
        cards.add(Card(13, Suit.SPADE, R.drawable.sk))
    }

    private fun createHeartCards(){
        cards.add(Card(1, Suit.HEART, R.drawable.h1))
        cards.add(Card(2, Suit.HEART, R.drawable.h2))
        cards.add(Card(3, Suit.HEART, R.drawable.h3))
        cards.add(Card(4, Suit.HEART, R.drawable.h4))
        cards.add(Card(5, Suit.HEART, R.drawable.h5))
        cards.add(Card(6, Suit.HEART, R.drawable.h6))
        cards.add(Card(7, Suit.HEART, R.drawable.h7))
        cards.add(Card(8, Suit.HEART, R.drawable.h8))
        cards.add(Card(9, Suit.HEART, R.drawable.h9))
        cards.add(Card(10, Suit.HEART, R.drawable.h10))
        cards.add(Card(11, Suit.HEART, R.drawable.hj))
        cards.add(Card(12, Suit.HEART, R.drawable.hq))
        cards.add(Card(13, Suit.HEART, R.drawable.hk))
    }

    private fun createClubCards(){
        cards.add(Card(1, Suit.CLUB, R.drawable.c1))
        cards.add(Card(2, Suit.CLUB, R.drawable.c2))
        cards.add(Card(3, Suit.CLUB, R.drawable.c3))
        cards.add(Card(4, Suit.CLUB, R.drawable.c4))
        cards.add(Card(5, Suit.CLUB, R.drawable.c5))
        cards.add(Card(6, Suit.CLUB, R.drawable.c6))
        cards.add(Card(7, Suit.CLUB, R.drawable.c7))
        cards.add(Card(8, Suit.CLUB, R.drawable.c8))
        cards.add(Card(9, Suit.CLUB, R.drawable.c9))
        cards.add(Card(10, Suit.CLUB, R.drawable.c10))
        cards.add(Card(11, Suit.CLUB, R.drawable.cj))
        cards.add(Card(12, Suit.CLUB, R.drawable.cq))
        cards.add(Card(13, Suit.CLUB, R.drawable.ck))
    }

    private fun createTableauPiles(){
        println("createTableauPiles")
        for(x in 0..6){
            var pileCards = ArrayList<Card>()
            for (y in 0..x) {
                var random = Random.nextInt(0, cards.size)
                cards.get(random).currentPile = x
                if (y < x){
                    cards.get(random).upFaced = false
                }
                pileCards.add(cards.get(random))
                println(cards.get(random).upFaced)
                cards.removeAt(random)
            }

            var pile = Pile(x, pileCards)
            tableau.piles.add(pile)
        }
    }

    fun printTableau(){
        for (pile in tableau.piles){
            println(" tableau pile " + pile.position)
            for (card in pile.cards!!){
                print(" $card");
            }
            println()
            println("----------------------------------------")
        }
    }

    private fun createFoundationPiles() {
        for(x in 8..11){
            foundation.piles.add(Pile(x,  ArrayList<Card>()))
        }
    }

    fun dealNextStock(){
        if (cards.size==0 && waste.cards?.size!! >0) {
            cards = waste.cards?.clone() as ArrayList<Card>
            waste.cards?.clear()
        }else if (cards.size>0){
            waste.cards?.add(cards[0])
            cards.removeAt(0)
        }
    }

    fun printStock(){
        println("Cards on stock \n")
        for(card in cards){
            print(card)
        }
    }

    fun printWaste(){
        println("Cards on waste \n")
        for(card in waste.cards!!){
            print(card)
        }

    }

    fun getWaste(): Int {
        println("getWaste")
        println(waste)
        if (waste.cards?.isEmpty()!!) {
            return 0
        }else {
            return waste.cards!!.last().image
        }
    }

}