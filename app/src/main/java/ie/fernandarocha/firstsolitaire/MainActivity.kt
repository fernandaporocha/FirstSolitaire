package ie.fernandarocha.firstsolitaire

import android.graphics.Point
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import kotlin.collections.ArrayList

@ExperimentalStdlibApi
class MainActivity : AppCompatActivity() {

    lateinit var board : Board
    var selectedCard : Card? = null
    var id : Int = 0
    private lateinit var layout : ConstraintLayout

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        board = Board()
        layout = findViewById(R.id.layout)


        for (card in board.cards!!) {
            println(card.toString())
        }

        generateDynamicTableau()

    }

    private fun nextStockCard() {
        println("nextStockCard")
        board.dealNextStock()
        cleanCards()
        generateDynamicTableau()
    }

    private fun selectCard(pile: Pile, id: Int = 0) {
        if (pile.position < 8 && pile.cards.isEmpty() && selectedCard == null)
            return

        println("selectCard")
        if (selectedCard != null) {
            //verifica se clicou na carta que ja esta selecionada
            if (pile.position < 8) {
                if (pile.cards.isEmpty() && selectedCard!!.number == 13) {
                    moveCardTableau(pile, selectedCard!!)
                } else if (pile.cards.isNotEmpty()) {
                    if (!selectedCard!!.equals(pile.cards?.last())) {
                        if (checkMarriageTableau(pile.cards.last(), selectedCard!!))
                            moveCardTableau(pile, selectedCard!!)
                    }
                }
            } else {
                println("else")
                //Trying to move to foundation
                if (pile.position in 8..11) {
                    if (checkMarriageFoundation(pile.cards, selectedCard!!))
                        moveCardToFoundation(pile, selectedCard!!)
                } else if (checkMarriageTableau(pile.cards.last(), selectedCard!!)) {
                    println("before move")
                    board.printTableau()
                    moveCardTableau(pile, selectedCard!!)
                    println("after move")
                    board.printTableau()
                }
            }
            selectedCard = null;
        } else {
            if (pile.cards != null && pile.cards.size > 0)
                selectedCard = pile.cards?.last()
        }
        cleanCards()
        generateDynamicTableau()
        println("saiu")
    }

    private fun moveCardTableau(to: Pile, from: Card) {
        var toPile: Int = to.position
        var fromPile: Int = from.currentPile
        println("MoveCard")
        println("toPile $toPile")
        println("to $to")
        println("fromPile $fromPile")
        println("from $from")
        from.currentPile = toPile
        println(board.tableau)
        board.tableau.piles[to.position].cards.add(from)

        //cards from tableau pile
        if(fromPile<7) {
            board.tableau.piles[fromPile].cards.removeLast()
        }
        //cards from waste pile
        else if (fromPile == 7) {
            board.waste.cards.removeLast()
        }
        //cards from foundation
        else {
            board.foundation.piles[fromPile-8].cards.removeLast()
        }


    }

    private fun moveCardToFoundation(to: Pile, from: Card) {
        var toPile: Int = to.position
        var fromPile: Int = from.currentPile
        println("MoveCard")
        println("toPile $toPile")
        println("to $to")
        println("fromPile $fromPile")
        println("from $from")
        from.currentPile = toPile
        //println(board.tableau)
        board.foundation.piles[to.position - 8].cards.add(from)
        if (fromPile == 7) {
            board.waste.cards.removeLast()
        } else {
            board.tableau.piles[fromPile].cards.removeLast()
        }
        cleanCards()
        generateDynamicTableau()
    }

    private fun checkMarriageTableau(top: Card, bottom: Card): Boolean {
        println("Checkmarriage")
        println(top)
        println(bottom)
        if (checkSuit(top.suit, bottom.suit)) {
            println("suit ok")
            if (top.number == bottom.number + 1) {
                println("casou")
                return true
            }
        }
        println("Nao casou")
        return false
    }

    private fun checkMarriageFoundation(topCards: ArrayList<Card>, bottom: Card): Boolean {
        println("CheckmarriageFoundation")
        println(bottom)
        if (topCards.isNotEmpty()) {
            if (topCards.last().suit == bottom.suit) {
                println("suit ok")
                println(topCards.last().number)
                println(bottom.number + 1)
                if (topCards.last().number + 1 == bottom.number) {
                    println("casou")
                    return true
                }
            }
        } else {
            if (bottom.number == 1) {
                return true
            }
        }

        println("Nao casou")
        return false
    }

    private fun checkSuit(suitTop: Suit, suitBottom: Suit): Boolean {
        println("checksuit")
        println(suitTop)
        println(suitBottom)
        if (((suitTop == Suit.SPADE || suitTop == Suit.CLUB) && (suitBottom == Suit.DIAMOND || suitBottom == Suit.HEART)) ||
            ((suitTop == Suit.DIAMOND || suitTop == Suit.HEART) && (suitBottom == Suit.SPADE || suitBottom == Suit.CLUB))
        ) {
            return true
        }
        return false
    }

    private fun generateDynamicTableau() {
        generateDynamicStockWasteFoundations()

        var listener = View.OnClickListener {  }

        for (pile: Pile in board.tableau.piles) {
            var currentY = 250F

            if (layout != null) {
                if(pile.cards.isEmpty()){
                    listener = View.OnClickListener{ selectCard(pile, id) }
                    addCardToLayout(pile.position, currentY, 0, R.drawable.gray_back, listener)
                }else {
                    for ((i, card: Card) in pile.cards.withIndex()) {
                        println("counter for ${i+1}")
                        listener = View.OnClickListener{ selectCard(pile, id) }
                        addCardToLayout(pile.position, currentY, i, card.image, listener)
                    }
                }

            }
        }
    }

    private fun generateDynamicStockWasteFoundations() {
        generateStock()
        generateWaste()

        for (pile: Pile in board.foundation.piles) {
            println("foundpile ${pile.getColumn()}")
            var currentY = 20F

            if (pile.cards.isEmpty()) {
                var listener = View.OnClickListener { selectCard(pile, id) }
                addCardToLayout(pile.getColumn(), currentY, 0, R.drawable.yellow_back, listener)
            } else {
                for (card: Card in pile.cards) {
                    var listener = View.OnClickListener { selectCard(pile, id) }
                    addCardToLayout(pile.getColumn(), currentY, 0, card.image, listener)
                }
            }
        }
    }

    private fun generateStock(){
        println("generateStock")
        val listener = View.OnClickListener {
            nextStockCard()
        }
        addCardToLayout(0, 20F, 0, R.drawable.purple_back, listener)
    }

    private fun addCardToLayout(column: Int, y:Float, row: Int, image: Int, onClickListener: View.OnClickListener){
        println("addCardToLayout")
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val width: Int = size.x
        val cardW = width / 9
        val gapW = cardW / 4
        val imageView: ImageView = ImageView(this)
        val x = (column*(cardW+gapW)).toFloat()

        var currentY = y
        if (row>0){
            currentY += (row*gapW).toFloat()
        }

        println("currentY $currentY")

        imageView.id = id
        imageView.layoutParams = ConstraintLayout.LayoutParams((cardW * 1.5).toInt(), (cardW * 1.5).toInt())
        imageView.setOnClickListener(onClickListener)
        imageView.setImageResource(image)
        imageView.x = x
        imageView.y = currentY
        layout?.addView(imageView);
        id++

    }

    private fun generateWaste(){
        println("generateWaste")
        var listener = View.OnClickListener{}

        if(board.waste.cards.isNotEmpty()) {
            listener = View.OnClickListener { selectCard(board.waste, id) }
            addCardToLayout(1, 20F, 0, board.waste.cards.last().image, listener)
        }else{
            println("waste else")
            addCardToLayout(1, 20F, 0, R.drawable.yellow_back, listener)
        }
    }

    private fun cleanCards(){
        layout?.removeAllViews()

    }
}