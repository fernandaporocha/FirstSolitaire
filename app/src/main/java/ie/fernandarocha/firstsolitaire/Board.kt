package ie.fernandarocha.firstsolitaire

import kotlin.random.Random

class Board {
    var cards = ArrayList<Card>()
    var waste = Pile(7, ArrayList())
    var tableau = Tableau(ArrayList())
    var foundation = Foundation(ArrayList())
    var id = 13

    init {
        createAllCards()
        cards.shuffle()
        createTableauPiles()
        printTableau()
        createFoundationPiles()
    }

    private fun createAllCards() {
        createDiamondCards()
        createSpadeCards()
        createHeartCards()
        createClubCards()
    }

    private fun createDiamondCards(){
        cards.add(Card(1, Suit.DIAMOND, R.drawable.d1, 7, true, id++))
        cards.add(Card(2, Suit.DIAMOND, R.drawable.d2, 7, true, id++))
        cards.add(Card(3, Suit.DIAMOND, R.drawable.d3, 7, true, id++))
        cards.add(Card(4, Suit.DIAMOND, R.drawable.d4, 7, true, id++))
        cards.add(Card(5, Suit.DIAMOND, R.drawable.d5, 7, true, id++))
        cards.add(Card(6, Suit.DIAMOND, R.drawable.d6, 7, true, id++))
        cards.add(Card(7, Suit.DIAMOND, R.drawable.d7, 7, true, id++))
        cards.add(Card(8, Suit.DIAMOND, R.drawable.d8, 7, true, id++))
        cards.add(Card(9, Suit.DIAMOND, R.drawable.d9, 7, true, id++))
        cards.add(Card(10, Suit.DIAMOND, R.drawable.d10, 7, true, id++))
        cards.add(Card(11, Suit.DIAMOND, R.drawable.dj, 7, true, id++))
        cards.add(Card(12, Suit.DIAMOND, R.drawable.dq, 7, true, id++))
        cards.add(Card(13, Suit.DIAMOND, R.drawable.dk, 7, true, id++))
    }

    private fun createSpadeCards(){
        cards.add(Card(1, Suit.SPADE, R.drawable.s1, 7, true, id++))
        cards.add(Card(2, Suit.SPADE, R.drawable.s2, 7, true, id++))
        cards.add(Card(3, Suit.SPADE, R.drawable.s3, 7, true, id++))
        cards.add(Card(4, Suit.SPADE, R.drawable.s4, 7, true, id++))
        cards.add(Card(5, Suit.SPADE, R.drawable.s5, 7, true, id++))
        cards.add(Card(6, Suit.SPADE, R.drawable.s6, 7, true, id++))
        cards.add(Card(7, Suit.SPADE, R.drawable.s7, 7, true, id++))
        cards.add(Card(8, Suit.SPADE, R.drawable.s8, 7, true, id++))
        cards.add(Card(9, Suit.SPADE, R.drawable.s9, 7, true, id++))
        cards.add(Card(10, Suit.SPADE, R.drawable.s10, 7, true, id++))
        cards.add(Card(11, Suit.SPADE, R.drawable.sj, 7, true, id++))
        cards.add(Card(12, Suit.SPADE, R.drawable.sq, 7, true, id++))
        cards.add(Card(13, Suit.SPADE, R.drawable.sk, 7, true, id++))
    }

    private fun createHeartCards(){
        cards.add(Card(1, Suit.HEART, R.drawable.h1, 7, true, id++))
        cards.add(Card(2, Suit.HEART, R.drawable.h2, 7, true, id++))
        cards.add(Card(3, Suit.HEART, R.drawable.h3, 7, true, id++))
        cards.add(Card(4, Suit.HEART, R.drawable.h4, 7, true, id++))
        cards.add(Card(5, Suit.HEART, R.drawable.h5, 7, true, id++))
        cards.add(Card(6, Suit.HEART, R.drawable.h6, 7, true, id++))
        cards.add(Card(7, Suit.HEART, R.drawable.h7, 7, true, id++))
        cards.add(Card(8, Suit.HEART, R.drawable.h8, 7, true, id++))
        cards.add(Card(9, Suit.HEART, R.drawable.h9, 7, true, id++))
        cards.add(Card(10, Suit.HEART, R.drawable.h10, 7, true, id++))
        cards.add(Card(11, Suit.HEART, R.drawable.hj, 7, true, id++))
        cards.add(Card(12, Suit.HEART, R.drawable.hq, 7, true, id++))
        cards.add(Card(13, Suit.HEART, R.drawable.hk, 7, true, id++))
    }

    private fun createClubCards(){
        cards.add(Card(1, Suit.CLUB, R.drawable.c1, 7, true, id++))
        cards.add(Card(2, Suit.CLUB, R.drawable.c2, 7, true, id++))
        cards.add(Card(3, Suit.CLUB, R.drawable.c3, 7, true, id++))
        cards.add(Card(4, Suit.CLUB, R.drawable.c4, 7, true, id++))
        cards.add(Card(5, Suit.CLUB, R.drawable.c5, 7, true, id++))
        cards.add(Card(6, Suit.CLUB, R.drawable.c6, 7, true, id++))
        cards.add(Card(7, Suit.CLUB, R.drawable.c7, 7, true, id++))
        cards.add(Card(8, Suit.CLUB, R.drawable.c8, 7, true, id++))
        cards.add(Card(9, Suit.CLUB, R.drawable.c9, 7, true, id++))
        cards.add(Card(10, Suit.CLUB, R.drawable.c10, 7, true, id++))
        cards.add(Card(11, Suit.CLUB, R.drawable.cj, 7, true, id++))
        cards.add(Card(12, Suit.CLUB, R.drawable.cq, 7, true, id++))
        cards.add(Card(13, Suit.CLUB, R.drawable.ck, 7, true, id++))
    }

    private fun createTableauPiles(){
        println("createTableauPiles")
        for(x in 0..6){
            val pileCards = ArrayList<Card>()
            for (y in 0..x) {
                val random = Random.nextInt(0, cards.size)
                cards[random].currentPile = x
                if (y < x){
                    cards[random].upFaced = false
                }
                pileCards.add(cards[random])
                cards.removeAt(random)
            }

            val pile = Pile(x, pileCards)
            tableau.piles.add(pile)
        }
    }

    fun printTableau(){
        for (pile in tableau.piles){
            println(" tableau pile " + pile.position)
            for (card in pile.cards){
                print(" $card")
            }
            println()
            println("----------------------------------------")
        }
    }

    private fun createFoundationPiles() {
        for(x in 8..11){
            foundation.piles.add(Pile(x,  ArrayList()))
        }
    }

    fun dealNextStock(){
        if (cards.size==0 && waste.cards.size >0) {
            cards = waste.cards.clone() as ArrayList<Card>
            waste.cards.clear()
        }else if (cards.size>0){
            waste.cards.add(cards[0])
            cards.removeAt(0)
        }
    }
}