package ie.fernandarocha.firstsolitaire

import android.graphics.Color
import android.graphics.drawable.shapes.Shape
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView

@ExperimentalStdlibApi
class MainActivity : AppCompatActivity() {

    lateinit var board : Board
    var selectedCard : Card? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        board = Board()

        for(card in board.cards!!) {
            println(card.toString())
        }

        var stockImage: ImageView = findViewById<ImageView>(R.id.stock_image)

        stockImage.setOnClickListener{ nextStockCard()}
        stockImage.setImageResource(R.drawable.purple_back)

        loadTableauImages()
        setTableauPilesEvents()

        loadFoundationImages()
        setFoundationPilesEvents()

        findViewById<ImageView>(R.id.waste_image).setOnClickListener {
            println("clicou")
            selectCard(board.waste) }

    }

    private fun nextStockCard(){
        board.dealNextStock()
        findViewById<ImageView>(R.id.waste_image).setImageResource(board.getWaste())
    }

    private fun selectCard(pile: Pile){
        if (pile.position<8 && pile.cards.isEmpty() && selectedCard==null)
            return

        var cardImage: ImageView? = null
        when(pile.position){
            0 -> cardImage = findViewById<ImageView>(R.id.pile_0)
            1 -> cardImage = findViewById<ImageView>(R.id.pile_1)
            2 -> cardImage = findViewById<ImageView>(R.id.pile_2)
            3 -> cardImage = findViewById<ImageView>(R.id.pile_3)
            4 -> cardImage = findViewById<ImageView>(R.id.pile_4)
            5 -> cardImage = findViewById<ImageView>(R.id.pile_5)
            7 -> cardImage = findViewById<ImageView>(R.id.waste_image)
            8 -> cardImage = findViewById<ImageView>(R.id.foundation_0)
            9 -> cardImage = findViewById<ImageView>(R.id.foundation_1)
            10 -> cardImage = findViewById<ImageView>(R.id.foundation_2)
            11 -> cardImage = findViewById<ImageView>(R.id.foundation_3)
        }

        println("selectCard")
        if (selectedCard!=null) {
            //verifica se clicou na carta que ja esta selecionada
            if(pile.position<8){
                if(pile.cards.isEmpty()&&selectedCard!!.number==13){
                    moveCardTableau(pile, selectedCard!!)
                }else if (pile.cards.isNotEmpty()) {
                    if (selectedCard!!.equals(pile.cards?.last())) {
                        if (cardImage != null) {
                            cardImage.setBackgroundColor(Color.TRANSPARENT)
                            cardImage.setPadding(0, 0, 0, 0)

                        }
                    }else {
                        if(checkMarriageTableau(pile.cards.last(), selectedCard!!))
                            moveCardTableau(pile, selectedCard!!)
                    }
                }
            }else{
                println("else")
                //Trying to move to foundation
                if(pile.position in 8..11){
                    if(checkMarriageFoundation(pile.cards, selectedCard!!))
                        moveCardToFoundation(pile, selectedCard!!)
                }else if(checkMarriageTableau(pile.cards.last(), selectedCard!!)){
                    println("before move")
                    board.printTableau()
                    moveCardTableau(pile, selectedCard!!)
                    println("after move")
                    board.printTableau()
                }
            }
            selectedCard = null;
        }
        else{
            selectedCard = pile.cards?.last()
            if (cardImage != null) {
                cardImage.setBackgroundColor(Color.RED)
                cardImage.setPadding(1, 1, 1, 1)
            }
        }
        println(pile.position)
    }

    private fun moveCardTableau(to: Pile, from: Card) {
        //if(to.cards.isEmpty()&&from.number==13)
        var toPile: Int = to.position
        var fromPile:Int = from.currentPile
        println("MoveCard")
        println("toPile $toPile")
        println("to $to")
        println("fromPile $fromPile")
        println("from $from")
        from.currentPile = toPile
        println(board.tableau)
        board.tableau.piles[to.position].cards.add(from)
        if (fromPile==7) {
            board.waste.cards.removeLast()
        }else {
            board.tableau.piles[fromPile].cards.removeLast()
        }
        loadTableauImages()

    }

    private fun moveCardToFoundation(to: Pile, from: Card) {
        var toPile: Int = to.position
        var fromPile:Int = from.currentPile
        println("MoveCard")
        println("toPile $toPile")
        println("to $to")
        println("fromPile $fromPile")
        println("from $from")
        from.currentPile = toPile
        //println(board.tableau)
        board.foundation.piles[to.position - 8].cards.add(from)
        if (fromPile==7) {
            board.waste.cards.removeLast()
        }else {
            board.tableau.piles[fromPile].cards.removeLast()
        }
        loadTableauImages()
        loadFoundationImages()

    }

    private fun checkMarriageTableau(top: Card, bottom: Card):Boolean {
        println("Checkmarriage")
        println(top)
        println(bottom)
        if(checkSuit(top.suit, bottom.suit)){
            println("suit ok")
            if(top.number==bottom.number+1){
                println("casou")
                return true
            }
        }
        println("Nao casou")
        return false
    }

    private fun checkMarriageFoundation(topCards: ArrayList<Card>, bottom: Card):Boolean {
        println("CheckmarriageFoundation")
        println(bottom)
        if (topCards.isNotEmpty()){
            if(topCards.last().suit==bottom.suit){
                println("suit ok")
                println(topCards.last().number)
                println(bottom.number + 1)
                if(topCards.last().number+1==bottom.number){
                    println("casou")
                    return true
                }
            }
        }else{
            if(bottom.number==1){
                return true
            }
        }

        println("Nao casou")
        return false
    }

    private fun checkSuit(suitTop: Suit, suitBottom: Suit):Boolean{
        println("checksuit")
        println(suitTop)
        println(suitBottom)
        if(((suitTop==Suit.SPADE||suitTop==Suit.CLUB)&&(suitBottom==Suit.DIAMOND||suitBottom==Suit.HEART))||
            ((suitTop==Suit.DIAMOND||suitTop==Suit.HEART)&&(suitBottom==Suit.SPADE||suitBottom==Suit.CLUB))){
            return true
        }
        return false
    }

    private fun loadTableauImages(){
        findViewById<ImageView>(R.id.pile_0).setImageResource(if (board.tableau.piles[0].cards.isNotEmpty()) board.tableau.piles[0].cards.last().image else R.drawable.blue_back)
        findViewById<ImageView>(R.id.pile_1).setImageResource(if (board.tableau.piles[1].cards.isNotEmpty()) board.tableau.piles[1].cards.last().image else R.drawable.blue_back)
        findViewById<ImageView>(R.id.pile_2).setImageResource(if (board.tableau.piles[2].cards.isNotEmpty()) board.tableau.piles[2].cards.last().image else R.drawable.blue_back)
        findViewById<ImageView>(R.id.pile_3).setImageResource(if (board.tableau.piles[3].cards.isNotEmpty()) board.tableau.piles[3].cards.last().image else R.drawable.blue_back)
        findViewById<ImageView>(R.id.pile_4).setImageResource(if (board.tableau.piles[4].cards.isNotEmpty()) board.tableau.piles[4].cards.last().image else R.drawable.blue_back)
        findViewById<ImageView>(R.id.pile_5).setImageResource(if (board.tableau.piles[5].cards.isNotEmpty()) board.tableau.piles[5].cards.last().image else R.drawable.blue_back)
        findViewById<ImageView>(R.id.pile_6).setImageResource(if (board.tableau.piles[6].cards.isNotEmpty()) board.tableau.piles[6].cards.last().image else R.drawable.blue_back)
        findViewById<ImageView>(R.id.waste_image).setImageResource(board.getWaste())

    }

    private fun setTableauPilesEvents(){
        findViewById<ImageView>(R.id.pile_0).setOnClickListener { selectCard(board.tableau.piles[0]) }
        findViewById<ImageView>(R.id.pile_1).setOnClickListener { selectCard(board.tableau.piles[1]) }
        findViewById<ImageView>(R.id.pile_2).setOnClickListener { selectCard(board.tableau.piles[2]) }
        findViewById<ImageView>(R.id.pile_3).setOnClickListener { selectCard(board.tableau.piles[3]) }
        findViewById<ImageView>(R.id.pile_4).setOnClickListener { selectCard(board.tableau.piles[4]) }
        findViewById<ImageView>(R.id.pile_5).setOnClickListener { selectCard(board.tableau.piles[5]) }
        findViewById<ImageView>(R.id.pile_6).setOnClickListener { selectCard(board.tableau.piles[6]) }
    }

    private fun loadFoundationImages(){
        findViewById<ImageView>(R.id.foundation_0).setImageResource(if (board.foundation.piles[0].cards.isNotEmpty()) board.foundation.piles[0].cards.last().image else R.drawable.blue_back)
        findViewById<ImageView>(R.id.foundation_1).setImageResource(if (board.foundation.piles[1].cards.isNotEmpty()) board.foundation.piles[1].cards.last().image else R.drawable.blue_back)
        findViewById<ImageView>(R.id.foundation_2).setImageResource(if (board.foundation.piles[2].cards.isNotEmpty()) board.foundation.piles[2].cards.last().image else R.drawable.blue_back)
        findViewById<ImageView>(R.id.foundation_3).setImageResource(if (board.foundation.piles[3].cards.isNotEmpty()) board.foundation.piles[3].cards.last().image else R.drawable.blue_back)
    }

    private fun setFoundationPilesEvents(){
        findViewById<ImageView>(R.id.foundation_0).setOnClickListener { selectCard(board.foundation.piles[0]) }
        findViewById<ImageView>(R.id.foundation_1).setOnClickListener { selectCard(board.foundation.piles[1]) }
        findViewById<ImageView>(R.id.foundation_2).setOnClickListener { selectCard(board.foundation.piles[2]) }
        findViewById<ImageView>(R.id.foundation_3).setOnClickListener { selectCard(board.foundation.piles[3]) }
    }
}