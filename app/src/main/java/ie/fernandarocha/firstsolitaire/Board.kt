package ie.fernandarocha.firstsolitaire

import kotlin.random.Random

class Board {
    var cards = ArrayList<Card>()
    var waste = ArrayList<Card>()
    var tableau = Tableau(ArrayList<Pile>())
    var foundations = ArrayList<Foundation>()

    init {
        createAllCards()
        cards.shuffle()
        createTableauPiles()
        printTableau()
        createFoundations()

    }

    private fun createAllCards() {
        println("createAllCards")
        createDiamondCards()
        createSpadeCards()
        createHeartCards()
        createClubCards()
    }

    private fun createDiamondCards(){
        cards.add(Card(1, Suit.DIAMOND, "\uD83C\uDCC1"))
        cards.add(Card(2, Suit.DIAMOND, "\uD83C\uDCC2"))
        cards.add(Card(3, Suit.DIAMOND, "\uD83C\uDCC3"))
        cards.add(Card(4, Suit.DIAMOND, "\uD83C\uDCC4"))
        cards.add(Card(5, Suit.DIAMOND, "\uD83C\uDCC5"))
        cards.add(Card(6, Suit.DIAMOND, "\uD83C\uDCC6"))
        cards.add(Card(7, Suit.DIAMOND, "\uD83C\uDCC7"))
        cards.add(Card(8, Suit.DIAMOND, "\uD83C\uDCC8"))
        cards.add(Card(9, Suit.DIAMOND, "\uD83C\uDCC9"))
        cards.add(Card(10, Suit.DIAMOND, "\uD83C\uDCCA"))
        cards.add(Card(11, Suit.DIAMOND, "\uD83C\uDCCB"))
        cards.add(Card(12, Suit.DIAMOND, "\uD83C\uDCCC"))
        cards.add(Card(13, Suit.DIAMOND, "\uD83C\uDCCD"))
    }


    private fun createSpadeCards(){
        cards.add(Card(1, Suit.SPADE, "\uD83C\uDCA1"))
        cards.add(Card(2, Suit.SPADE, "\uD83C\uDCA2"))
        cards.add(Card(3, Suit.SPADE, "\uD83C\uDCA3"))
        cards.add(Card(4, Suit.SPADE, "\uD83C\uDCA4"))
        cards.add(Card(5, Suit.SPADE, "\uD83C\uDCA5"))
        cards.add(Card(6, Suit.SPADE, "\uD83C\uDCA6"))
        cards.add(Card(7, Suit.SPADE, "\uD83C\uDCA7"))
        cards.add(Card(8, Suit.SPADE, "\uD83C\uDCA8"))
        cards.add(Card(9, Suit.SPADE, "\uD83C\uDCA9"))
        cards.add(Card(10, Suit.SPADE, "\uD83C\uDCAA"))
        cards.add(Card(11, Suit.SPADE, "\uD83C\uDCAB"))
        cards.add(Card(12, Suit.SPADE, "\uD83C\uDCAC"))
        cards.add(Card(13, Suit.SPADE, "\uD83C\uDCAD"))
    }

    private fun createHeartCards(){
        cards.add(Card(1, Suit.HEART, "\uD83C\uDCB1"))
        cards.add(Card(2, Suit.HEART, "\uD83C\uDCB2"))
        cards.add(Card(3, Suit.HEART, "\uD83C\uDCB3"))
        cards.add(Card(4, Suit.HEART, "\uD83C\uDCB4"))
        cards.add(Card(5, Suit.HEART, "\uD83C\uDCB5"))
        cards.add(Card(6, Suit.HEART, "\uD83C\uDCB6"))
        cards.add(Card(7, Suit.HEART, "\uD83C\uDCB7"))
        cards.add(Card(8, Suit.HEART, "\uD83C\uDCB8"))
        cards.add(Card(9, Suit.HEART, "\uD83C\uDCB9"))
        cards.add(Card(10, Suit.HEART, "\uD83C\uDCBA"))
        cards.add(Card(11, Suit.HEART, "\uD83C\uDCBB"))
        cards.add(Card(12, Suit.HEART, "\uD83C\uDCBC"))
        cards.add(Card(13, Suit.HEART, "\uD83C\uDCBD"))
    }

    private fun createClubCards(){
        cards.add(Card(1, Suit.CLUB, "🃑"))
        cards.add(Card(2, Suit.CLUB, "\uD83C\uDCD2"))
        cards.add(Card(3, Suit.CLUB, "\uD83C\uDCD3"))
        cards.add(Card(4, Suit.CLUB, "\uD83C\uDCD4"))
        cards.add(Card(5, Suit.CLUB, "\uD83C\uDCD5"))
        cards.add(Card(6, Suit.CLUB, "\uD83C\uDCD6"))
        cards.add(Card(7, Suit.CLUB, "\uD83C\uDCD7"))
        cards.add(Card(8, Suit.CLUB, "\uD83C\uDCD8"))
        cards.add(Card(9, Suit.CLUB, "\uD83C\uDCD9"))
        cards.add(Card(10, Suit.CLUB, "\uD83C\uDCDA"))
        cards.add(Card(11, Suit.CLUB, "\uD83C\uDCDB"))
        cards.add(Card(12, Suit.CLUB, "\uD83C\uDCDC"))
        cards.add(Card(13, Suit.CLUB, "\uD83C\uDCDD"))
    }

    private fun createTableauPiles(){
        for(x in 0..6){
            var pileCards = ArrayList<Card>()
            for (y in 0..x) {
                var random = Random.nextInt(0, cards.size)
                pileCards.add(cards.get(random))
                cards.removeAt(random)
            }

            var pile = Pile(x, x, pileCards)
            tableau.piles.add(pile)
        }
    }

    private fun printTableau(){
        for (pile in tableau.piles){
            println("tableau pile " + pile.position)
            for (card in pile.cards){
                print(card)
            }
            println()
            println("----------------------------------------")
        }
    }

    private fun createFoundations() {
        for(x in 0..3){
            foundations.add(Foundation(x, null, ArrayList<Card>()))
        }
    }

    fun dealNextStock(){
        if (cards.size==0 && waste.size>0) {
            cards = waste.clone() as ArrayList<Card>
            waste.clear()
        }else if (cards.size>0){
            waste.add(cards[0])
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
        for(card in waste){
            print(card)
        }

    }
}