package ie.fernandarocha.firstsolitaire

import android.content.pm.ActivityInfo
import android.graphics.Point
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import kotlin.collections.ArrayList

@ExperimentalStdlibApi
class MainActivity : AppCompatActivity() {

    private lateinit var board : Board
    private var selectedCard : Card? = null
    private var manyCard: Card? = null
    private var id : Int = 0
    private lateinit var layout : ConstraintLayout

    override fun onCreate(savedInstanceState : Bundle?) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        board = Board()
        layout = findViewById(R.id.layout)

        for (card in board.cards) {
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

    private fun selectCard(pile: Pile, id: Int = 0, manyC: Card? = null) {
        if (pile.position < 8 && pile.cards.isEmpty() && selectedCard == null)
            return

        println("selectCard")
        //Check if there are any selected card
        if (pile.position==7){
            selectedCard=pile.cards.last()
        }else if (selectedCard != null) {
            println("1")
            println("Selected card $selectedCard")
            //Check if the event came from the foundation
            if (pile.position < 8) {
                println("2")
                if(manyCard!=null) {
                    println("5")
                    println("many first")
                    if (pile.position != selectedCard!!.currentPile) {
                        println("6")
                        println("many")
                        //manyCard = manyC
                        if ((pile.cards.isEmpty()&& manyCard!!.rank == 13)||(checkMarriageTableau(pile.cards.last(), manyCard!!))) {
                            moveManyCards(pile, manyCard!!)
                            manyCard = null
                            selectedCard = null
                        }
                    }
                }
                //If the "to" pile is empty and the selected card is a king
                else if (pile.cards.isEmpty() && selectedCard!!.rank == 13) {
                    println("3")
                    moveCardTableau(pile, selectedCard!!)
                    selectedCard = null
                }
                //If the "to" pile is not empty
                else if (pile.cards.isNotEmpty()) {
                    println("4")
                    if (selectedCard!! != pile.cards.last()) {
                        println("7")
                        if (checkMarriageTableau(pile.cards.last(), selectedCard!!)) {
                            println("8")
                            moveCardTableau(pile, selectedCard!!)
                            selectedCard = null
                        }
                    }
                }
            } else {
                println("9")
                println("else")
                //Trying to move to foundation
                if (pile.position in 8..11) {
                    println("10")
                    if (checkMarriageFoundation(pile.cards, selectedCard!!)) {
                        println("11")
                        moveCardToFoundation(pile, selectedCard!!)
                        selectedCard = null
                    }
                } else if (checkMarriageTableau(pile.cards.last(), selectedCard!!)) {
                    println("12")
                    println("before move")
                    board.printTableau()
                    moveCardTableau(pile, selectedCard!!)
                    selectedCard = null
                    println("after move")
                    board.printTableau()
                }
            }
            selectedCard = null
        } else {
            println("13")
            if (pile.cards.isNotEmpty()) {
                println("14")
                selectedCard = pile.cards.last()
                if (manyC!=null) {
                    println("15")
                    manyCard = manyC
                }
            }
        }
        if (manyC==null)
            manyCard = null
        println("saiu")
    }

    private fun moveCardTableau(to: Pile, from: Card) {
        val toPile: Int = to.position
        val fromPile: Int = from.currentPile
        println("MoveCard")
        println("toPile $toPile")
        if(to.cards.isNotEmpty())
            println("to ${to.cards.last()}")
        println("fromPile $fromPile")
        println("from $from")
        from.currentPile = toPile
        println(board.tableau)
        board.tableau.piles[to.position].cards.add(from)

        //cards from tableau pile
        if(fromPile<7) {
            board.tableau.piles[fromPile].cards.removeLast()
            if(board.tableau.piles[fromPile].cards.isNotEmpty())
                board.tableau.piles[fromPile].cards.last().upFaced = true
        }
        //cards from waste pile
        else if (fromPile == 7) {
            board.waste.cards.removeLast()
        }
        //cards from foundation
        else {
            board.foundation.piles[fromPile-8].cards.removeLast()
        }

        cleanCards()
        generateDynamicTableau()
    }

    private fun moveManyCards(to: Pile, from: Card) {
        val toPile: Int = to.position
        val fromPile: Int = from.currentPile
        println("MoveCard")
        println("toPile $toPile")
        println("to $to")
        println("fromPile $fromPile")
        println("from $from")
        from.currentPile = toPile
        println(board.tableau)

        val startIndex = board.tableau.piles[fromPile].cards.indexOf(from)
        val endIndex = board.tableau.piles[fromPile].cards.size-1
        if(startIndex==endIndex){
            println("SAME INDEX")
            return
        }
        println(startIndex)
        println(endIndex)
        val cardsToMove = ArrayList<Card>()
        println("card that will be moved")
        for (i:Int in startIndex..endIndex){
            println(board.tableau.piles[fromPile].cards[i].rank)
            println(board.tableau.piles[fromPile].cards[i].suit)
            cardsToMove.add(board.tableau.piles[fromPile].cards[i])
        }
        //var cardsToMove:ArrayList<Card> = board.tableau.piles[fromPile].cards.subList(startIndex, endIndex) as ArrayList<Card>


        //cards from tableau pile
        if(fromPile<7) {
            board.tableau.piles[fromPile].cards.removeAll (cardsToMove)
            if(board.tableau.piles[fromPile].cards.isNotEmpty()) {
                board.tableau.piles[fromPile].cards.last().upFaced = true
            }
        }

        for(card: Card in cardsToMove){
            card.currentPile = toPile
        }
        board.tableau.piles[to.position].cards.addAll(cardsToMove)


        cleanCards()
        generateDynamicTableau()

    }

    private fun moveCardToFoundation(to: Pile, from: Card) {
        val toPile: Int = to.position
        val fromPile: Int = from.currentPile
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
            if (board.tableau.piles[fromPile].cards.isNotEmpty())
                board.tableau.piles[fromPile].cards.last().upFaced = true
            println("Mudou o upfaced")
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
            if (top.rank == bottom.rank + 1) {
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
                println(topCards.last().rank)
                println(bottom.rank + 1)
                if (topCards.last().rank + 1 == bottom.rank) {
                    println("casou")
                    return true
                }
            }
        } else {
            if (bottom.rank == 1) {
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
            val currentY = 250F

            if (layout != null) {
                if(pile.cards.isEmpty()){
                    listener = View.OnClickListener{ selectCard(pile, id) }
                    addCardToLayout(pile.position, currentY, 0, R.drawable.gray_back, listener)
                }else {
                    for ((i, card: Card) in pile.cards.withIndex()) {
                        //println("counter for ${i+1}")
                        var img = 0
                        if (card.upFaced){
                            if(i==pile.cards.size-1) {
                                //println("no many ${card.rank} ${card.suit}")
                                listener = View.OnClickListener { selectCard(pile, id) }
                                img = card.image
                            }else{
                                //println("many ${card.rank} ${card.suit}")
                                listener = View.OnClickListener { selectCard(pile, id, card) }
                                img = card.image
                            }
                        }else{
                            listener = View.OnClickListener{ selectCard(pile, id) }
                            img = R.drawable.purple_back
                        }
                        addCardToLayout(pile.position, currentY, i, img, listener)
                    }
                }

            }
        }
    }

    private fun generateDynamicStockWasteFoundations() {
        generateStock()
        generateWaste()

        for (pile: Pile in board.foundation.piles) {
            //println("foundpile ${pile.getColumn()}")
            val currentY = 20F

            if (pile.cards.isEmpty()) {
                val listener = View.OnClickListener { selectCard(pile, id) }
                addCardToLayout(pile.getColumn(), currentY, 0, R.drawable.yellow_back, listener)
            } else {
                for (card: Card in pile.cards) {
                    val listener = View.OnClickListener { selectCard(pile, id) }
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
        layout.addView(imageView)
        println("id $id card $image")
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
        layout.removeAllViews()
        id = 0
    }
}