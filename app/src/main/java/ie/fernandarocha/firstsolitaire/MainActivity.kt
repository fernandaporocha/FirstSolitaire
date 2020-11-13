package ie.fernandarocha.firstsolitaire

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var board = Board()

        for(card in board.cards!!){
            println(card.toString())
        }
    }
}