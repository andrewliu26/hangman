package com.example.hangman

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var wordToGuess: TextView
    private lateinit var hangmanDisplay: ImageView
    private lateinit var allLetters: GridLayout
    private lateinit var newGameButton: Button
    private var hintButton: Button? = null

    private val hangmanImages = arrayOf(
        R.drawable.hangman_1,
        R.drawable.hangman_2,
        R.drawable.hangman_3,
        R.drawable.hangman_4,
        R.drawable.hangman_5,
        R.drawable.hangman_6,
        R.drawable.hangman_7,
        R.drawable.hangman_8
    )
    private val wordsList = arrayOf("apple", "banana", "cherry", "lemons", "papaya")
    private var currentWord = ""
    private var displayWord = ""
    private var hintCount = 0
    private var life = 6



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        wordToGuess = findViewById(R.id.wordToGuess)
        hangmanDisplay = findViewById(R.id.hangmanDisplay)
        allLetters = findViewById(R.id.letterButtons)
        newGameButton = findViewById(R.id.newGameButton)
        hintButton = findViewById(R.id.hintButton)

        newGameButton.setOnClickListener{ newGame() }
        hintButton?.setOnClickListener { showHint() }

        hangmanDisplay.setImageResource(hangmanImages[0])
        hintButton?.isEnabled = false
        disableLetters("all")
    }

    private fun newGame() {
        life = 6
        hintCount = 0
        hintButton?.isEnabled = true
        currentWord = wordsList[Random.nextInt(wordsList.size)]
        displayWord = "_".repeat(currentWord.length)
        wordToGuess.text = displayWord
        hangmanDisplay.setImageResource(hangmanImages[1])

        for (i in 0 until allLetters.childCount) {
            (allLetters.getChildAt(i)).isEnabled = true
        }

    }

    private fun showHint() {
        if (life <= 1) {
            Toast.makeText(this, "Hint not available", Toast.LENGTH_SHORT).show()
            return
        }

        when (hintCount) {
            0 -> Toast.makeText(this, "Your first hint: Fruit!", Toast.LENGTH_SHORT).show()
            1 -> {
                life--
                disableLetters("half")
                hangmanDisplay.setImageResource(hangmanImages[7 - life])
            }
            2 -> {
                life--
                showVowels()
                hangmanDisplay.setImageResource(hangmanImages[7 - life])
            }
        }

        hintCount++
    }

    private fun disableLetters(tag: String    ) {
        when (tag) {
            "all" -> {
                for (i in 0 until allLetters.childCount) {
                    (allLetters.getChildAt(i) as Button).isEnabled = false
                }
            }
            "half" -> {
                val enabledLetters = mutableListOf<Button>()

                for (i in 0 until allLetters.childCount) {
                    val button = allLetters.getChildAt(i) as Button
                    if (button.isEnabled) {
                        enabledLetters.add(button)
                    }
                }

                val unusedLetters = enabledLetters.filter { btn ->
                    !currentWord.contains(btn.text.toString(), ignoreCase = true)
                }

                unusedLetters.shuffled().take(unusedLetters.size / 2).forEach { button ->
                    button.isEnabled = false
                }
            }
        }
    }


    private fun showVowels() {
        val vowels = "aeiou"
        for (i in 0 until allLetters.childCount) {
            val button = allLetters.getChildAt(i) as Button
            val letter = button.text.toString()
            if (vowels.contains(letter, true)) {
                button.isEnabled = false
                if (currentWord.contains(letter, true)) {
                    for (j in currentWord.indices) {
                        if (currentWord[j].equals(letter[0], true)) {
                            displayWord = displayWord.substring(0, j) + letter + displayWord.substring(j + 1)
                        }
                    }
                }
            }
        }
        wordToGuess.text = displayWord
    }

    fun letterClicked(view: View) {
        val button = view as Button
        val letter = button.text.toString()
        button.isEnabled = false
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
                disableLetters("all")
                newGame()
            }
        } else {
            life--
            if (life == 0) {
                Toast.makeText(this, "You lose!", Toast.LENGTH_SHORT).show()
                hangmanDisplay.setImageResource(hangmanImages[7])
                disableLetters("all")
                hintButton?.isEnabled = false
            } else {
                hangmanDisplay.setImageResource(hangmanImages[7 - life])
            }
        }
    }
}