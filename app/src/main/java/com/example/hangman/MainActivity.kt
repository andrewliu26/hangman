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
import androidx.activity.viewModels
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var wordToGuess: TextView
    private lateinit var hangmanDisplay: ImageView
    private lateinit var allLetters: GridLayout
    private lateinit var newGameButton: Button
    private var hintButton: Button? = null

    private val mainViewModel: MainViewModel by viewModels()

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

        hangmanDisplay.setImageResource(mainViewModel.hangmanImages[0])
        hintButton?.isEnabled = false
        disableLetters("all")
    }

    private fun newGame() {
        mainViewModel.life = 6
        mainViewModel.hintCount = 0
        hintButton?.isEnabled = true
        mainViewModel.currentWord = mainViewModel.wordsList[Random.nextInt(mainViewModel.wordsList.size)]
        mainViewModel.displayWord = "_".repeat(mainViewModel.currentWord.length)
        wordToGuess.text = mainViewModel.displayWord
        hangmanDisplay.setImageResource(mainViewModel.hangmanImages[1])

        for (i in 0 until allLetters.childCount) {
            (allLetters.getChildAt(i)).isEnabled = true
        }

    }

    private fun showHint() {
        if (mainViewModel.life <= 1) {
            Toast.makeText(this, "Hint not available", Toast.LENGTH_SHORT).show()
            return
        }

        when (mainViewModel.hintCount) {
            0 -> Toast.makeText(this, "Your first hint: Fruit!", Toast.LENGTH_SHORT).show()
            1 -> {
                mainViewModel.life--
                disableLetters("half")
                hangmanDisplay.setImageResource(mainViewModel.hangmanImages[7 - mainViewModel.life])
            }
            2 -> {
                mainViewModel.life--
                showVowels()
                hangmanDisplay.setImageResource(mainViewModel.hangmanImages[7 - mainViewModel.life])
            }
        }

        mainViewModel.hintCount++
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
                    !mainViewModel.currentWord.contains(btn.text.toString(), ignoreCase = true)
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
                if (mainViewModel.currentWord.contains(letter, true)) {
                    for (j in mainViewModel.currentWord.indices) {
                        if (mainViewModel.currentWord[j].equals(letter[0], true)) {
                            mainViewModel.displayWord = mainViewModel.displayWord.substring(0, j) + letter + mainViewModel.displayWord.substring(j + 1)
                        }
                    }
                }
            }
        }
        wordToGuess.text = mainViewModel.displayWord
    }

    fun letterClicked(view: View) {
        val button = view as Button
        val letter = button.text.toString()
        button.isEnabled = false
        Log.d("MainActivity", "Button clicked: $letter")

        if (mainViewModel.currentWord.contains(letter, true)) {
            for (i in mainViewModel.currentWord.indices) {
                if (mainViewModel.currentWord[i].equals(letter[0], true)) {
                    mainViewModel.displayWord = mainViewModel.displayWord.substring(0, i) + letter + mainViewModel.displayWord.substring(i + 1)
                }
            }
            wordToGuess.text = mainViewModel.displayWord

            if (!mainViewModel.displayWord.contains("_")) {
                Toast.makeText(this, "You win!", Toast.LENGTH_SHORT).show()
                disableLetters("all")
                newGame()
            }
        } else {
            mainViewModel.life--
            if (mainViewModel.life == 0) {
                Toast.makeText(this, "You lose!", Toast.LENGTH_SHORT).show()
                hangmanDisplay.setImageResource(mainViewModel.hangmanImages[7])
                disableLetters("all")
                hintButton?.isEnabled = false
            } else {
                hangmanDisplay.setImageResource(mainViewModel.hangmanImages[7 - mainViewModel.life])
            }
        }
    }
}