package ie.fernandarocha.firstsolitaire

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView

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

        var listener = View.OnTouchListener(function = { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_MOVE) {
                view.y = motionEvent.rawY - view.height/2
                view.x = motionEvent.rawX - view.width/2
            }
            true
        })

        //findViewById<ImageView>(R.id.waste_image).setOnTouchListener(listener)
         }
    }

    private fun nextStockCard(){
        board.dealNextStock()
        findViewById<ImageView>(R.id.waste_image).setImageResource(board.getWaste())
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

}