package com.example.hangman

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testNewGameButton() {
        onView(withId(R.id.newGameButton)).perform(click())
        onView(withId(R.id.wordToGuess)).check(matches(isDisplayed()))
    }

    @Test
    fun testLetterButton() {
        onView(withId(R.id.letterButtons)).perform(click())
        onView(withId(R.id.letterButtons)).check(matches((isEnabled())))
    }


}


