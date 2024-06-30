package ajedrez.app


import Ajedreze.App.R
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity




class MainActivity2 : AppCompatActivity() {

    private lateinit var chessView: ChessView
    private lateinit var chessGame: ChessGame

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        chessView = findViewById(R.id.chess_view)
        chessGame = ChessGame

        chessView.chessDelegate = chessGame
    }
    fun showWinnerDialog(winner: Player) {


        val builder = AlertDialog.Builder(this)
        builder.setTitle("¡Felicidades!")
        builder.setMessage("El jugador ${winner.name} ha ganado! 🌟🌟🌟")
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

}
interface GameListener{
    fun onGameEnd(winer: Player)

    companion object {
        fun onGameEnd() {

        }
    }
}