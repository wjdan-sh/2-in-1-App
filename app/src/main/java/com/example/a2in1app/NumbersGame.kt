package com.example.a2in1app

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.random.Random

class NumbersGame : AppCompatActivity() {

    private lateinit var RC: RecyclerView
    private lateinit var ed: EditText
    private lateinit var btn: Button
    private lateinit var guess: ArrayList<String>
    private lateinit var tv: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_numbers_game)

        RC = findViewById(R.id.rv)
        guess = ArrayList()

        tv = findViewById(R.id.tv)
        ed = findViewById(R.id.ed)
        btn = findViewById(R.id.btn)

        RC.adapter = RVAdapter(guess)
        RC.layoutManager = LinearLayoutManager(this)

        var count = 3
        val num = Random.nextInt(0, 10)
        btn.setOnClickListener {
            val Uinput = ed.text.toString()
            if (Uinput.isNotEmpty()) {
                if (count > 0) {
                    if (Uinput.toInt() == num) {

                        guess.add("You win!")
                        customAlert()
                    } else {

                        count--
                        guess.add("You guessed $Uinput")
                        guess.add("You have $count guesses left")


                    }
                    if (count == 0) {

                        guess.add("You lose - The correct answer was $num")
                        guess.add("Game Over")
                        customAlert()
                    }
                }
                ed.text.clear()
                ed.clearFocus()
                RC.adapter?.notifyDataSetChanged()


            }
        }
    }

    private fun customAlert(){

        val dialogBuilder = AlertDialog.Builder(this)

        dialogBuilder.setMessage("You want to play again?")

            .setPositiveButton("yes", DialogInterface.OnClickListener {
                    dialog, id -> this.recreate()
            })

            .setNegativeButton("no", DialogInterface.OnClickListener {
                    dialog, id -> dialog.cancel()
            })

        val alert = dialogBuilder.create()

        alert.setTitle("game over")


        alert.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.mi_new_game -> {

                val intent = Intent(this, this::class.java)
                startActivity(intent)
                return true
            }
            R.id.mi_other_game -> {

                val intent = Intent(this, GuessThePhrase::class.java)
                startActivity(intent)
                return true
            }
            R.id.mi_back -> {

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}