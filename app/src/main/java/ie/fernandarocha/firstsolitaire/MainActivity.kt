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

        var listener = View.OnTouchListener(function = { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_MOVE) {
                view.y = motionEvent.rawY - view.height/2
                view.x = motionEvent.rawX - view.width/2
            }
            true
        })

        //findViewById<ImageView>(R.id.waste_image).setOnTouchListener(listener)
        findViewById<ImageView>(R.id.waste_image).setOnClickListener {
            println("clicou")
            selectCard(board.waste) }
    }

    private fun nextStockCard(){
        board.dealNextStock()
        findViewById<ImageView>(R.id.waste_image).setImageResource(board.getWaste())
    }

    private fun selectCard(pile:Pile){
        if (pile.cards.isEmpty())
            return

        var cardImage: ImageView? = null
        when(pile.position){
            0 ->  cardImage = findViewById<ImageView>(R.id.pile_0)
            1 ->  cardImage = findViewById<ImageView>(R.id.pile_1)
            2 ->  cardImage = findViewById<ImageView>(R.id.pile_2)
            3 ->  cardImage = findViewById<ImageView>(R.id.pile_3)
            4 ->  cardImage = findViewById<ImageView>(R.id.pile_4)
            5 ->  cardImage = findViewById<ImageView>(R.id.pile_5)
            6 ->  cardImage = findViewById<ImageView>(R.id.pile_6)
            7 ->  cardImage = findViewById<ImageView>(R.id.waste_image)
        }

        println("selectCard")
        if (selectedCard!=null) {
            //verifica se clicou na carta que ja esta selecionada
            if(selectedCard!!.equals(pile.cards?.last())){
                if (cardImage != null) {
                    cardImage.setBackgroundColor(Color.TRANSPARENT)
                    cardImage.setPadding(0, 0, 0, 0)
                }
            }else{
                println("else")
                if(checkMarriageTableau(pile.cards.last(), selectedCard!!)){
                    println("before move")
                    board.printTableau()
                    moveCardTableau(pile.cards.last(), selectedCard!!)
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

    private fun moveCardTableau(to: Card, from: Card) {
        var toPile: Int = to.currentPile
        var fromPile:Int = from.currentPile
        println("MoveCard")
        println("toPile $toPile")
        println("to $to")
        println("fromPile $fromPile")
        println("from $from")
        from.currentPile = toPile
        println(board.tableau)
        board.tableau.piles[to.currentPile].cards.add(from)
        if (fromPile==7) {
            board.waste.cards.removeLast()
        }else {
            board.tableau.piles[fromPile].cards.removeLast()
        }
        loadTableauImages()

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

    private fun checkSuit(suitTop: Suit, suitBottom:Suit):Boolean{
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

}