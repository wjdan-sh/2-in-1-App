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

class MainActivity : AppCompatActivity() {

    private lateinit var ngBtn: Button
    private lateinit var gphBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ngBtn = findViewById(R.id.btNG)
        gphBtn = findViewById(R.id.btGPh)

        ngBtn.setOnClickListener {

            val intent = Intent(this, NumbersGame::class.java)
            startActivity(intent)
        }

        gphBtn.setOnClickListener {

            val intent = Intent(this, GuessThePhrase::class.java)
            startActivity(intent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.mi_numbers_game -> {

                val intent = Intent(this, NumbersGame::class.java)
                startActivity(intent)
                return true
            }
            R.id.mi_guess_the_phrase -> {

                val intent = Intent(this, GuessThePhrase::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


}