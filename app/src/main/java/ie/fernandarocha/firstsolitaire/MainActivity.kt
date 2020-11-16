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

        var listener = View.OnTouchListener(function = { view, motionEvent ->

            if (motionEvent.action == MotionEvent.ACTION_MOVE) {

                view.y = motionEvent.rawY - view.height/2
                view.x = motionEvent.rawX - view.width/2
            }

            true

        })

        findViewById<ImageView>(R.id.waste_image).setOnTouchListener(listener)
    }

    private fun nextStockCard(){
        board.dealNextStock()
        findViewById<ImageView>(R.id.waste_image).setImageResource(board.getWaste())
    }

}