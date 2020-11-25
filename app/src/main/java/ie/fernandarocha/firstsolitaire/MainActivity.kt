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
    private var id : Int = 12
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
        createBoardBase()
        generateDynamicTableau()
        setWasteImageViews()
        var listener = View.OnLongClickListener { checkAvailableMove(false)}

        layout.setOnLongClickListener(listener)
        println("setou o evento no layout")
    }

    private fun nextStockCard() {
        println("nextStockCard")
        if(board.cards.size==0 && board.waste.cards.isNotEmpty()){
            cleanWasteImages()
        }
        board.dealNextStock()
        updateWasteImage()
    }

    private fun selectCard(pile: Pile, id: Int = 0, manyC: Card? = null) {
        println(pile.position)
        println("Selected card $selectedCard")
        if (pile.position < 8 && pile.cards.isEmpty() && selectedCard == null)
            return

        println("selectCard")
        if(manyC!=null){
            println("manyC is $manyC")
            println("manyC id is ${manyC.id}")
            println("id is $id")
        }
        //Check if there are any selected card
        if (pile.position==7){
            selectedCard=pile.cards.last()
        }else if (selectedCard != null) {
            println("1")
            println("Selected card $selectedCard")
            //Check if the event came from the foundation
            if (pile.position < 8) {
                println("2")
                println("pile.cards.isEmpty() ${pile.cards.isEmpty()}")
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
                    println("pile size ${pile.cards.size}")
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
                    println("manyCard $manyCard")
                }
            }
        }
        if (manyC==null)
            manyCard = null

        if(selectedCard!=null){
            println("selected card $selectedCard")
        }
    }

    private fun moveCardTableau(to: Pile, from: Card) {
        val toPile: Int = to.position
        val fromPile: Int = from.currentPile
        println("moveCardTableau")
        println("toPile $toPile")
        if(to.cards.isNotEmpty())
            println("to ${to.cards.last()}")
        println("fromPile $fromPile")
        println("from $from")
        from.currentPile = toPile
        println(board.tableau)

        var listener = View.OnClickListener {  }

        if (to.cards.isNotEmpty()){
            println("if 1")
            println("upFaced ${to.cards.last().upFaced}")
            if(to.cards.last().upFaced) {
                println("set listener many ${to.cards.last()}")
                val card = to.cards.last()
                val listener1 = View.OnClickListener { selectCard(to, card.id, card) }
                layout.getViewById(card.id).setOnClickListener(listener1)
            }
        }
        board.tableau.piles[to.position].cards.add(from)

        listener = View.OnClickListener { selectCard(to, from.id) }
        println("to.cards.size ${to.cards.size}")
        moveCard(to.position, 250F, from.id, listener, to.cards.size-1)

        //cards from tableau pile
        if(fromPile<7) {
            println("from tableau")
            board.tableau.piles[fromPile].cards.removeLast()
            if(board.tableau.piles[fromPile].cards.isNotEmpty()) {
                board.tableau.piles[fromPile].cards.last().upFaced = true
                turnCardImage(board.tableau.piles[fromPile].cards.last())
            }
        }
        //cards from waste pile
        else if (fromPile == 7) {
            board.waste.cards.removeLast()
        }
        //cards from foundation
        else {
            board.foundation.piles[fromPile-8].cards.removeLast()
        }

        //cleanCards()
        //generateDynamicTableau()
    }

    private fun turnCardImage(card:Card){
        (layout.getViewById(card.id) as ImageView).setImageResource(card.image)
    }

    private fun moveManyCards(to: Pile, from: Card) {
        val toPile: Int = to.position
        val fromPile: Int = from.currentPile
        println("moveManyCards")
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
            println("i $i endIndex $endIndex")
            println(board.tableau.piles[fromPile].cards[i].rank)
            println(board.tableau.piles[fromPile].cards[i].suit)
            cardsToMove.add(board.tableau.piles[fromPile].cards[i])
            var card = board.tableau.piles[fromPile].cards[i]
            val listener = View.OnClickListener{ selectCard(to, card.id, card) }
            if (i < endIndex){
                val listener = View.OnClickListener{ selectCard(to, card.id, card) }
            }else{
                val listener = View.OnClickListener{ selectCard(to, card.id) }
            }
            moveCard(toPile, 250F, card.id, listener,to.cards.size)
            card.currentPile = toPile
            board.tableau.piles[to.position].cards.add(card)
        }
        //var cardsToMove:ArrayList<Card> = board.tableau.piles[fromPile].cards.subList(startIndex, endIndex) as ArrayList<Card>


        //cards from tableau pile
        if(fromPile<7) {
            board.tableau.piles[fromPile].cards.removeAll (cardsToMove)
            if(board.tableau.piles[fromPile].cards.isNotEmpty()) {
                board.tableau.piles[fromPile].cards.last().upFaced = true
                turnCardImage(board.tableau.piles[fromPile].cards.last())
            }
        }
    }

    private fun moveCardToFoundation(to: Pile, from: Card) {
        val toPile: Int = to.position
        val fromPile: Int = from.currentPile
        println("moveCardToFoundation")
        println("toPile $toPile")
        println("to $to")
        println("fromPile $fromPile")
        println("from $from")
        from.currentPile = toPile
        var listener = View.OnClickListener { }
        if (to.cards.isNotEmpty()){
            println("if 1")
            println("upFaced ${to.cards.last().upFaced}")
            if(to.cards.last().upFaced) {
                println("set listener many ${to.cards.last()}")
                listener = View.OnClickListener { selectCard(to, to.cards.last().id, to.cards.last()) }
                layout.getViewById(to.cards.last().id).setOnClickListener(listener)
            }
        }
        board.foundation.piles[to.position - 8].cards.add(from)
        println("Moving card id ${from.id}")
        listener = View.OnClickListener { selectCard(to, from.id) }

        moveCard(to.position-5, 20F,from.id, listener)
        if (fromPile == 7) {
            board.waste.cards.removeLast()
            updateWasteImage()
        } else {
            board.tableau.piles[fromPile].cards.removeLast()
            if (board.tableau.piles[fromPile].cards.isNotEmpty()) {
                board.tableau.piles[fromPile].cards.last().upFaced = true
                turnCardImage(board.tableau.piles[fromPile].cards.last())
                println("Mudou o upfaced")
            }
        }
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
            ((suitTop == Suit.DIAMOND || suitTop == Suit.HEART) && (suitBottom == Suit.SPADE || suitBottom == Suit.CLUB))) {
            return true
        }
        return false
    }

    private fun generateDynamicTableau() {
        var listener: View.OnClickListener

        for (pile: Pile in board.tableau.piles) {
            val currentY = 250F

            if (layout != null) {
                if(pile.cards.isEmpty()){
                    listener = View.OnClickListener{ selectCard(pile, id) }
                    addCardToLayout(pile.position, currentY, 0, R.drawable.gray_back, listener, null, id)
                    id++
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
                        addCardToLayout(pile.position, currentY, i, img, listener, card, card.id)
                    }
                }

            }
        }
    }


    private fun addCardToLayout(column: Int, y:Float, row: Int, image: Int, onClickListener: View.OnClickListener, card: Card?, givenId: Int){
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

        imageView.id = givenId
        imageView.layoutParams = ConstraintLayout.LayoutParams((cardW * 1.5).toInt(), (cardW * 1.5).toInt())
        imageView.setOnClickListener(onClickListener)
        imageView.setImageResource(image)
        imageView.x = x
        imageView.y = currentY
        layout.addView(imageView)
    }

    fun moveCard(column:Int, y: Float, id: Int, listener: View.OnClickListener, row:Int =0){
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val width: Int = size.x
        val cardW = width / 9
        val gapW = cardW / 4
        var currentY = y

        val x = (column*(cardW+gapW)).toFloat()

        println("row $row")
        println("column $column")
        if (row>0){
            currentY += (row*gapW).toFloat()
        }
        println("before x ${layout.getViewById(id).x}")
        println("before y ${layout.getViewById(id).y}")

        println("mov x $x")
        println("mov y $currentY")

        layout.getViewById(id).x = x
        layout.getViewById(id).y = currentY
        layout.getViewById(id).setOnClickListener(listener)
        layout.getViewById(id).bringToFront()
    }


    private fun setWasteImageViews() {
        println("setWasteImageViews")
        for (card: Card in board.cards) {
            val listener = View.OnClickListener { selectCard(board.waste, id) }
            println("setWasteImageViews ${card.id} $card")
            addCardToLayout(1, 20F, 0, card.image, listener, card, card.id)
            println("child ${layout.childCount}")
            layout.getViewById(card.id).visibility = View.GONE
        }
    }

    private fun cleanWasteImages(){
        for (card: Card in board.waste.cards) {
            val listener = View.OnClickListener { selectCard(board.waste, id) }
            layout.getViewById(card.id).visibility = View.GONE
        }
    }


    private fun updateWasteImage(){
        println("updateWasteImage")
        var listener = View.OnClickListener {}
        if (board.waste.cards.isNotEmpty()){
            println("last waste id ${board.waste.cards.last().id} ${board.waste.cards.last()}")
            val listener = View.OnClickListener { selectCard(board.waste, board.waste.cards.last().id) }
            layout.getViewById(board.waste.cards.last().id).setOnClickListener(listener)
            layout.getViewById(board.waste.cards.last().id).visibility=View.VISIBLE
            println(layout.getViewById(board.waste.cards.last().id).x)
            println(layout.getViewById(board.waste.cards.last().id).y)

        }
    }

    private fun createBoardBase() {
        var id:Int = 0
        val currentY = 20F

        var listener = View.OnClickListener {
            nextStockCard()
        }
        addCardToLayout(0, currentY, 0, R.drawable.purple_back, listener, null, 0)

        for (pile: Pile in board.foundation.piles) {
            //println("foundpile ${pile.getColumn()}")

            if (pile.cards.isEmpty()) {
                listener = View.OnClickListener { selectCard(pile, id) }
                addCardToLayout(pile.getColumn(), currentY, 0, R.drawable.yellow_back, listener, null, id)
            }
            id++
        }

        for (pile: Pile in board.tableau.piles) {
            //println("foundpile ${pile.getColumn()}")
            listener = View.OnClickListener { selectCard(pile, id) }
            println("create base T ${pile.position}")
            addCardToLayout(pile.position, 250F, 0, R.drawable.gray_back, listener, null, id)
            id++
        }
    }




    private fun checkAvailableMove(untilFinish: Boolean = false):Boolean{
        println("checkAvailableMove")
        for (pile: Pile in board.tableau.piles) {
            if (pile.cards.isNotEmpty()){
                for(foundation: Pile in board.foundation.piles){
                    if(checkMarriageFoundation(pile.cards.last(),foundation)){
                        moveCardToFoundation(foundation, pile.cards.last())
                        if (!untilFinish)
                            return true
                    }
                }

                for(pileTableau: Pile in board.tableau.piles){
                    if(pile!=pileTableau){
                        if(pileTableau.cards.isNotEmpty()){
                            for(card: Card in pile.cards) {
                                if (card.upFaced && card != pile.cards.last()) {
                                    if ((pileTableau.cards.isEmpty() && card.rank == 13) || (checkMarriageTableau(pileTableau.cards.last(), card))) {
                                        moveManyCards(pileTableau, card)
                                        println("moveMany <3")
                                        return true
                                    }
                                }
                            }
                        }
                    }
                }

                for(pileTableau: Pile in board.tableau.piles){
                    if(pile!=pileTableau){
                        if(pileTableau.cards.isNotEmpty()){
                            if(checkMarriageTableau(pile.cards.last(), pileTableau.cards.last())){
                                moveCardTableau(pile, pileTableau.cards.last())
                                if (!untilFinish)
                                    return true
                            }
                        }else if(pileTableau.cards.isEmpty() && pile.cards.last().rank == 13){
                            moveCardTableau(pileTableau, pile.cards.last())
                            if (!untilFinish)
                                return true
                        }
                    }
                }
            }
        }

        if (board.waste.cards.isNotEmpty()) {
            var card = board.waste.cards.last()
            println("waste card $card")
            for (foundation: Pile in board.foundation.piles) {
                if (checkMarriageFoundation(card, foundation)) {
                    println("movewaste found")
                    moveCardToFoundation(foundation, card)
                    if (!untilFinish)
                        return true
                }
            }

            for (pileTableau: Pile in board.tableau.piles) {
                //if ((pileTableau.cards.isNotEmpty()&& (checkMarriageTableau(pileTableau.cards.last(), card))) ||
                //                (pileTableau.cards.isEmpty()&&card.rank==13)){
                        //moveCardTableau(board.waste, pileTableau.cards.last())
                if (pileTableau.cards.isNotEmpty()&& (checkMarriageTableau(pileTableau.cards.last(), card))){
                    println("movewaste 1")
                    moveCardTableau(pileTableau, card )
                    if (!untilFinish)
                        return true
                }else if (pileTableau.cards.isEmpty()&&card.rank==13){
                    println("movewaste 2")
                    moveCardTableau(pileTableau, card )
                    if (!untilFinish)
                        return true
                }
            }
        }
        return true
    }

    private fun addSendToFoundationEvent(pile: Pile){
        //println("addSendToFoundationEvent currentpile ${card.currentPile}" )
        var listener = View.OnLongClickListener { trySendToFoundation(pile) }
        layout.getViewById(pile.cards.last().id).setOnLongClickListener(listener)
    }
}