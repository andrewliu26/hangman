package com.example.hangman

import androidx.lifecycle.SavedStateHandle
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class MainViewModelTest {
    private lateinit var viewModel: MainViewModel
    private lateinit var savedStateHandle: SavedStateHandle

    @Before
    fun setUp() {
        val initialData = HashMap<String, Any>()
        initialData["lives"] = 6
        initialData["hintCount"] = 0
        savedStateHandle = SavedStateHandle(initialData)
        viewModel = MainViewModel(savedStateHandle)
    }

    // Testing that the lives and hints are not initialized because no new game started
    @Test
    fun testInitialLives() {
        assertEquals(0, viewModel.fetchLives())
    }

    @Test
    fun testInitialHintCount() {
        assertEquals(3, viewModel.fetchHintCount() + 3)
    }
}