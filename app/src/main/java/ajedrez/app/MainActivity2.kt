package ajedrez.app

import Ajedreze.App.R
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        chessView.chessDelegate = chessGame
        fun showWinnerDialog(winner: Player) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("¡Felicidades!")
            builder.setMessage("El jugador ${winner.name} ha ganado! 🌟🌟🌟")
            builder.show()
        }
    }
}