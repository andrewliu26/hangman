package com.example.hangman

import android.widget.ImageView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.SavedStateHandle
import kotlin.random.Random

private const val TAG = "MainViewModel"

const val CONST_CURRENT_WORD = "CONST_CURRENT_WORD"
const val CONST_DISPLAY_WORD = "CONST_DISPLAY_WORD"
const val CONST_DRAWABLE = "CONST_DRAWABLE"
const val CONST_HINTS = "CONST_HINTS"
const val CONST_LIFE = "CONST_LIFE"

class MainViewModel (private val savedStateHandle: SavedStateHandle) : ViewModel() {

    private val wordsList = arrayOf("apple", "banana", "cherry", "lemons", "papaya")
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

    // data to/from saved state
    private var life: Int
        get() = savedStateHandle[CONST_LIFE] ?: 0
        set(value) = savedStateHandle.set(CONST_LIFE, value)

    private var currentWord: String
        get() = savedStateHandle[CONST_CURRENT_WORD] ?: ""
        set(value) = savedStateHandle.set(CONST_CURRENT_WORD, value)

    private var displayWord: String
        get() = savedStateHandle[CONST_DISPLAY_WORD] ?: ""
        set(value) = savedStateHandle.set(CONST_DISPLAY_WORD, value)

    private var hintCount: Int
        get() = savedStateHandle[CONST_HINTS] ?: 0
        set(value) = savedStateHandle.set(CONST_HINTS, value)

    private var hangmanDisplay: Int
        get() = savedStateHandle[CONST_DRAWABLE] ?: 0
        set(value) = savedStateHandle.set(CONST_DRAWABLE, value)

    // life functions
    fun updateLife(lives: Int) {
        life = lives
    }
    fun deductLife() {
        life -= 1
    }
    fun fetchLives(): Int {
        return life
    }

    // hint functions
    fun updateHints(hints: Int) {
        hintCount = hints
    }
    fun incHintCount() {
        hintCount++
    }
    fun fetchHintCount(): Int {
        return hintCount
    }

    // current word functions
    fun generateWord() {
        currentWord = wordsList[Random.nextInt(wordsList.size)]
    }
    fun fetchCurrentWord(): String {
        return currentWord
    }

    // display word functions
    fun initDisplayWord() {
        displayWord = "_".repeat(currentWord.length)
    }
    fun updateDisplayWord(word: String) {
        displayWord = word
    }
    fun fetchDisplayWord(): String {
        return displayWord
    }

    // buttons functions


    // image functions
    fun fetchImage(): Int {
        return hangmanDisplay
    }
    fun updateImage(view: ImageView, index: Int) {
        view.setImageResource(hangmanImages[index])
        hangmanDisplay = index
    }
    fun advanceImage(view: ImageView) {
        updateImage(view, hangmanImages[7 - life])
    }
}