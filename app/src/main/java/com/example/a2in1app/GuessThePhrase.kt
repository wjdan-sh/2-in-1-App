package com.example.a2in1app

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class GuessThePhrase : AppCompatActivity() {
    private lateinit var clRoot: ConstraintLayout
    private lateinit var rvMessages: RecyclerView
    private lateinit var guessField: EditText
    private lateinit var guessButton: Button
    private lateinit var messages: ArrayList<String>
    private lateinit var tvPhrase: TextView
    private lateinit var tvLetters: TextView


    private val answer = "this is the secret phrase"
    private val myAnswerDictionary = mutableMapOf<Int, Char>()
    private var myAnswer = ""
    private var guessedLetters = ""
    private var count = 0
    private var guessPhrase = true

    private lateinit var myHighScore: TextView
    private lateinit var sharedPreferences: SharedPreferences

    private var score = 0
    private var highScore = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guess_the_phrase)

        sharedPreferences = this.getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        highScore = sharedPreferences.getInt("HighScore", 0)

        myHighScore = findViewById(R.id.tvHS)
        myHighScore.text = "High Score: $highScore"

        for(i in answer.indices){
            if(answer[i] == ' '){
                myAnswerDictionary[i] = ' '
                myAnswer += ' '
            }else{
                myAnswerDictionary[i] = '*'
                myAnswer += '*'
            }
        }
        clRoot = findViewById(R.id.clRoot)
        rvMessages = findViewById(R.id.rvMessages)
        messages = ArrayList()

        rvMessages.adapter = RVAdapter( messages)
        rvMessages.layoutManager = LinearLayoutManager(this)

        guessField = findViewById(R.id.etGuessField)
        guessButton = findViewById(R.id.btGuessButton)
        guessButton.setOnClickListener { addMessage() }

        tvPhrase = findViewById(R.id.tvPhrase)
        tvLetters = findViewById(R.id.tvLetters)

        updateText()
    }

    private fun addMessage(){
        val msg = guessField.text.toString()

        if(guessPhrase){
            if(msg == answer){
                disableEntry()
                updateScore()
                showAlertDialog("You win!\n\nPlay again? ")
            }else{
                messages.add("Wrong guess: $msg")
                guessPhrase = false
                updateText()
            }
        }else{
            if(msg.isNotEmpty() && msg.length==1){
                myAnswer = ""
                guessPhrase = true
                checkLetters(msg[0])
            }else{
                Snackbar.make(clRoot, "Please enter one letter only", Snackbar.LENGTH_LONG).show()
            }
        }

        guessField.text.clear()
        guessField.clearFocus()
        rvMessages.adapter?.notifyDataSetChanged()
    }

    private fun disableEntry(){
        guessButton.isEnabled = false
        guessButton.isClickable = false
        guessField.isEnabled = false
        guessField.isClickable = false
    }

    private fun updateText(){
        tvPhrase.text = "Phrase:  " + myAnswer.toUpperCase()
        tvLetters.text = "Guessed Letters:  " + guessedLetters
        if(guessPhrase){
            guessField.hint = "Guess the full phrase"
        }else{
            guessField.hint = "Guess a letter"
        }
    }

    private fun checkLetters(guessedLetter: Char){
        var found = 0
        for(i in answer.indices){
            if(answer[i] == guessedLetter){
                myAnswerDictionary[i] = guessedLetter
                found++
            }
        }
        for(i in myAnswerDictionary){myAnswer += myAnswerDictionary[i.key]}
        if(myAnswer==answer){
            disableEntry()
            updateScore()
            showAlertDialog("You win!\n\nPlay again?")
        }
        if(guessedLetters.isEmpty()){guessedLetters+=guessedLetter}else{guessedLetters+=" , "+guessedLetter}
        if(found>0){
            messages.add("Found $found ${guessedLetter.toUpperCase()}(s)")
        }else{
            messages.add("No ${guessedLetter.toUpperCase()}s found")
        }
        count++
        val guessesLeft = 10 - count
        if(count<10){messages.add("$guessesLeft guesses remaining")}
        updateText()
        rvMessages.scrollToPosition(messages.size - 1)
    }

    private fun showAlertDialog(title: String) {

        val dialogBuilder = AlertDialog.Builder(this)

        dialogBuilder.setMessage(title)
            .setCancelable(false)

            .setPositiveButton("Yes", DialogInterface.OnClickListener {
                    dialog, id -> this.recreate()
            })

            .setNegativeButton("No", DialogInterface.OnClickListener {
                    dialog, id -> dialog.cancel()
            })


        val alert = dialogBuilder.create()

        alert.setTitle("Game Over")
        alert.show()
    }

    private fun updateScore(){
        score = 10 - count
        if(score >= highScore){
            highScore = score
            with(sharedPreferences.edit()) {
                putInt("HighScore", highScore)
                apply()
            }
            Snackbar.make(clRoot, "NEW HIGH SCORE!", Snackbar.LENGTH_LONG).show()
        }
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

                val intent = Intent(this, NumbersGame::class.java)
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