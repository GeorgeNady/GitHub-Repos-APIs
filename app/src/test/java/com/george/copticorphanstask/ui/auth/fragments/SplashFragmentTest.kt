package com.george.copticorphanstask.ui.auth.fragments

import androidx.navigation.Navigation
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.george.copticorphanstask.R
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SplashFragmentTest {

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    /*@Test
    fun testNavigationToInGameScreen() {
        // Create a TestNavHostController
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())

        // Create a graphical FragmentScenario for the TitleScreen
        val titleScenario = launchFragmentInContainer<SplashFragment>()

        titleScenario.onFragment { fragment ->
            // Set the graph on the TestNavHostController
            navController.setGraph(R.navigation.auth_nav_graph)

            // Make the NavController available via the findNavController() APIs
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        // Verify that performing a click changes the NavController’s state
        onView(ViewMatchers.withId(R.id.play_btn)).perform(ViewActions.click())
        assertThat(navController.currentDestination?.id).isEqualTo(R.id.in_game)
    }*/
}