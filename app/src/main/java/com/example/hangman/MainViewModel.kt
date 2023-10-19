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
        Log.v("MainViewModel", "Hints updated")
    }
    fun incHintCount() {
        hintCount++
        Log.v("MainViewModel", "Hints incremented")
    }
    fun fetchHintCount(): Int {
        Log.v("MainViewModel", "Hints used: $hintCount")
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
    private val clickedButtons: MutableList<String>
        get() = savedStateHandle.get<MutableList<String>>(CONST_CLICKED_BUTTONS) ?: mutableListOf()

    fun buttonClicked(letter: String) {
        val updatedList = clickedButtons.apply { add(letter) }
        savedStateHandle[CONST_CLICKED_BUTTONS] = updatedList
    }

    fun fetchClickedButtons(): List<String> {
        Log.d("MainActivity", "$clickedButtons")
        return clickedButtons
    }

    fun clearClickedButtons() {
        clickedButtons.clear()
        savedStateHandle[CONST_CLICKED_BUTTONS] = clickedButtons
        Log.v(TAG, "Clicked buttons cleared")
    }

    companion object {
        const val CONST_CLICKED_BUTTONS = "CONST_CLICKED_BUTTONS"
    }

    // image functions
    fun fetchImage(): Int {
        Log.v("MainViewModel", "Image index: $hangmanId")
        return hangmanId
    }
    fun updateImage(view: ImageView, index: Int) {
        if (index in hangmanImages.indices) {
            view.setImageResource(hangmanImages[index])
            hangmanId = index
            Log.v("MainViewModel", "Image updated")
        } else {
            Log.e("MainViewModel", "Invalid index: $index")
        }
    }
    fun advanceImage(view: ImageView) {
        Log.v(TAG, "lives: $life")
        val newIndex = 7 - life
        Log.v("MainViewModel", "New index calculated: $newIndex")
        updateImage(view, newIndex)
        Log.v("MainViewModel", "Image advanced")
    }
}