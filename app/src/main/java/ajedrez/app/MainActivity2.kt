package ajedrez.app

import Ajedreze.App.R
import Ajedreze.App.R.id
import Ajedreze.App.R.id.toolbar
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.content.Intent

class MainActivity2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val bottomNavigationView = findViewById<BottomNavigationView>(id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                id.navigation_home -> {
                    val intent = Intent(this, MainActivity1::class.java)
                    startActivity(intent)
                    true
                }
                id.navigation_dashboard -> true
                id.navigation_notifications -> true
                else -> false
            }
        }

        val chessView: ChessView = findViewById(id.chessView)
        val chessGame = ChessGame

        chessView.chessDelegate = chessGame

        fun showWinnerDialog(winner: Player) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("¡Felicidades!")
            builder.setMessage("El jugador ${winner.name} ha ganado! 🌟🌟🌟")
            builder.setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            builder.show()
        }
    }
}