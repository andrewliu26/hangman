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

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    // views
    private lateinit var wordToGuess: TextView
    private lateinit var hangmanDisplay: ImageView
    private lateinit var allLetters: GridLayout
    private lateinit var newGameButton: Button
    private var hintButton: Button? = null

    // variables
    private var lives = 6
    private var hints = 0
    private var currentWord = ""
    private var displayWord = ""
    private var image = 0

    // Connect ViewModel
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Linking views
        wordToGuess = findViewById(R.id.wordToGuess)
        hangmanDisplay = findViewById(R.id.hangmanDisplay)
        allLetters = findViewById(R.id.letterButtons)
        newGameButton = findViewById(R.id.newGameButton)
        hintButton = findViewById(R.id.hintButton)

        // Set button listeners
        newGameButton.setOnClickListener{ newGame() }
        hintButton?.setOnClickListener { showHint() }

        // Initial state
        // mainViewModel.updateImage(hangmanDisplay, 0)
        // hintButton?.isEnabled = false
        // disableLetters("all")

        updateState()
    }

    private fun newGame() {
        mainViewModel.updateLife(6)
        mainViewModel.updateHints(0)
        mainViewModel.generateWord()
        mainViewModel.initDisplayWord()
        mainViewModel.updateImage(hangmanDisplay, 1)

        hintButton?.isEnabled = true
        wordToGuess.text = mainViewModel.fetchDisplayWord()

        updateState()

        for (i in 0 until allLetters.childCount) {
            (allLetters.getChildAt(i)).isEnabled = true
        }

    }

    private fun updateState() {
        lives = mainViewModel.fetchLives()
        hints = mainViewModel.fetchHintCount()
        currentWord = mainViewModel.fetchCurrentWord()
        displayWord = mainViewModel.fetchDisplayWord()
        image = mainViewModel.fetchImage()

        wordToGuess.text = displayWord
        mainViewModel.updateImage(hangmanDisplay, image)
        // allLetters =
    }

    private fun showHint() {
        if (mainViewModel.fetchLives() <= 1) {
            Toast.makeText(this, "Hint not available", Toast.LENGTH_SHORT).show()
            return
        }

        when (mainViewModel.fetchHintCount()) {
            0 -> Toast.makeText(this, "Your first hint: Fruit!", Toast.LENGTH_SHORT).show()
            1 -> {
                mainViewModel.deductLife()
                disableLetters("half")
                mainViewModel.advanceImage(hangmanDisplay)
            }
            2 -> {
                mainViewModel.deductLife()
                showVowels()
                mainViewModel.advanceImage(hangmanDisplay)
            }
        }

        mainViewModel.incHintCount()
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
                    !mainViewModel.fetchCurrentWord().contains(btn.text.toString(), ignoreCase = true)
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
                if (mainViewModel.fetchCurrentWord().contains(letter, true)) {
                    for (j in mainViewModel.fetchCurrentWord().indices) {
                        if (mainViewModel.fetchCurrentWord()[j].equals(letter[0], true)) {
                            mainViewModel.updateDisplayWord(mainViewModel.fetchDisplayWord().substring(0, j) + letter + mainViewModel.fetchDisplayWord().substring(j + 1))
                        }
                    }
                }
            }
        }
        wordToGuess.text = mainViewModel.fetchDisplayWord()
    }

    fun letterClicked(view: View) {
        val button = view as Button
        val letter = button.text.toString()
        button.isEnabled = false
        Log.d("MainActivity", "Button clicked: $letter")

        if (mainViewModel.fetchCurrentWord().contains(letter, true)) {
            for (i in mainViewModel.fetchCurrentWord().indices) {
                if (mainViewModel.fetchCurrentWord()[i].equals(letter[0], true)) {
                    mainViewModel.updateDisplayWord(mainViewModel.fetchDisplayWord().substring(0, i) + letter + mainViewModel.fetchDisplayWord().substring(i + 1))
                }
            }
            wordToGuess.text = mainViewModel.fetchDisplayWord()

            if (!mainViewModel.fetchDisplayWord().contains("_")) {
                Toast.makeText(this, "You win!", Toast.LENGTH_SHORT).show()
                disableLetters("all")
                newGame()
            }
        } else {
            mainViewModel.deductLife()
            if (mainViewModel.fetchLives() == 0) {
                Toast.makeText(this, "You lose!", Toast.LENGTH_SHORT).show()
                mainViewModel.updateImage(hangmanDisplay, 7)
                disableLetters("all")
                hintButton?.isEnabled = false
            } else {
                mainViewModel.updateImage(hangmanDisplay,7 - mainViewModel.fetchLives())
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }
}