package com.example.hangman

import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.SavedStateHandle
import kotlin.random.Random

private const val TAG = "MainViewModel"

const val CONST_CURRENT_WORD = "CONST_CURRENT_WORD"
const val CONST_DISPLAY_WORD = "CONST_DISPLAY_WORD"
const val CONST_DRAWABLE = "CONST_DRAWABLE"
const val CONST_PRESSED = "CONST_PRESSED"
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

    private var hangmanId: Int
        get() = savedStateHandle[CONST_DRAWABLE] ?: 0
        set(value) = savedStateHandle.set(CONST_DRAWABLE, value)

    private var lettersPressed: ArrayList<Int>
        get() = savedStateHandle[CONST_PRESSED] ?: ArrayList<Int>()
        set(value) = savedStateHandle.set(CONST_PRESSED, value)

    // life functions
    fun updateLife(lives: Int) {
        life = lives
        Log.v(TAG, "Lives updated")
    }
    fun deductLife() {
        life--
        Log.v(TAG, "Lives deducted")
    }
    fun fetchLives(): Int {
        Log.v(TAG, "Lives: $life")
        return life
    }

    // hint functions
    fun updateHints(hints: Int) {
        hintCount = hints
        Log.v(TAG, "Hints updated")
    }
    fun incHintCount() {
        hintCount++
        Log.v(TAG, "Hints incremented")
    }
    fun fetchHintCount(): Int {
        Log.v(TAG, "Hints used: $hintCount")
        return hintCount
    }

    // current word functions
    fun generateWord() {
        currentWord = wordsList[Random.nextInt(wordsList.size)]
        Log.v(TAG, "Word generated: $currentWord")
    }
    fun fetchCurrentWord(): String {
        Log.v(TAG, "Current word: $currentWord")
        return currentWord
    }

    // display word functions
    fun initDisplayWord() {
        displayWord = "_".repeat(currentWord.length)
        Log.v(TAG, "Display word initialized")
    }
    fun updateDisplayWord(word: String) {
        displayWord = word
        Log.v(TAG, "Display word updated")
    }
    fun fetchDisplayWord(): String {
        Log.v(TAG, "Display word: $displayWord")
        return displayWord
    }

    // buttons functions
    fun addToPressed(index: Int) {
        lettersPressed.add(index)
        savedStateHandle[CONST_PRESSED] = lettersPressed
        Log.v(TAG, "Added $index to lettersPressed")
    }
    fun fetchPressed(): ArrayList<Int> {
        Log.v(TAG, "Letters pressed: $lettersPressed")
        return lettersPressed
    }
    fun clearPressed() {
        lettersPressed.clear()
    }


    // image functions
    fun fetchImage(): Int {
        Log.v(TAG, "Image index: $hangmanId")
        return hangmanId
    }
    fun updateImage(view: ImageView, index: Int) {
        view.setImageResource(hangmanImages[index])
        hangmanId = index
        Log.v(TAG, "Image updated")
    }
    fun advanceImage(view: ImageView) {
        updateImage(view, hangmanImages[7 - life])
        Log.v(TAG, "Image advanced")
    }
}