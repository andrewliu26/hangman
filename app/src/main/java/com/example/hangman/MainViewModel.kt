package com.example.hangman

import androidx.lifecycle.ViewModel
import androidx.lifecycle.SavedStateHandle

private const val TAG = "MainViewModel"

class MainViewModel (private val savedStateHandle: SavedStateHandle) : ViewModel() {
    val hangmanImages = arrayOf(
        R.drawable.hangman_1,
        R.drawable.hangman_2,
        R.drawable.hangman_3,
        R.drawable.hangman_4,
        R.drawable.hangman_5,
        R.drawable.hangman_6,
        R.drawable.hangman_7,
        R.drawable.hangman_8
    )

    val wordsList = arrayOf("apple", "banana", "cherry", "lemons", "papaya")

    var currentWord = ""
    var displayWord = ""
    var hintCount = 0
    var life = 6
    var message = ""
}