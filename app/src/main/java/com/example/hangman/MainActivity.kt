package com.example.hangman

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var wordToGuess: TextView
    private lateinit var hangmanDisplay: ImageView
    private val wordsList = arrayOf("apple", "fruit", "chair", "table", "plane")
    private var currentWord = ""
    private var currentHint = ""
    private var displayWord = ""
    private var life = 6

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        wordToGuess = findViewById(R.id.wordToGuess)
        hangmanDisplay = findViewById(R.id.hangmanDisplay)

        newGame()
    }

    private fun newGame() {
        life = 6
        currentWord = wordsList[Random.nextInt(wordsList.size)]
        displayWord = "_".repeat(currentWord.length)
        wordToGuess.text = displayWord
        hangmanDisplay.setImageResource(R.drawable.hangman_02)
    }

    fun letterClicked(view: View) {
        val button = view as Button
        val letter = button.text.toString()
        Log.d("MainActivity", "Button clicked: $letter")

        if (currentWord.contains(letter, true)) {
            for (i in currentWord.indices) {
                if (currentWord[i].equals(letter[0], true)) {
                    displayWord = displayWord.substring(0, i) + letter + displayWord.substring(i + 1)
                }
            }
            wordToGuess.text = displayWord

            if (!displayWord.contains("_")) {
                Toast.makeText(this, "You win!", Toast.LENGTH_SHORT).show()
                newGame()
            }
        } else {
            life--
            if (life == 0) {
                Toast.makeText(this, "You lose!", Toast.LENGTH_SHORT).show()
                newGame()
            } else {
                hangmanDisplay.setImageResource(
                    resources.getIdentifier(
                        "hangman_${(6 - life) + 1}",
                        "drawable",
                        packageName
                    )
                )
            }
        }
        button.isEnabled = false
    }

    fun newGameButton(view: View) {
        newGame()
    }
}