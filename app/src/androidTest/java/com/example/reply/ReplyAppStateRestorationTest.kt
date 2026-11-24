package com.example.reply

import androidx.activity.ComponentActivity
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertAny
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.onNodeWithText
import com.example.reply.data.local.LocalEmailsDataProvider
import com.example.reply.test.onNodeWithContentDescriptionForStringId
import com.example.reply.test.onNodeWithTagForStringId
import com.example.reply.ui.ReplyApp
import com.example.reply.ui.theme.ReplyTheme
import org.junit.Rule
import org.junit.Test

class ReplyAppStateRestorationTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    @TestCompactWidth
    fun compactDevice_selectedEmailEmailRetained_afterConfigChange() {
        val stateRestorationTester = StateRestorationTester(composeTestRule)
        val thirdEmail = LocalEmailsDataProvider.allEmails[2]
        val thirdEmailBody = composeTestRule.activity.getString(thirdEmail.body)
        val thirdEmailSubject = composeTestRule.activity.getString(thirdEmail.subject)

        stateRestorationTester.setContent {
            ReplyTheme {
                ReplyApp(windowSize = WindowWidthSizeClass.Compact)
            }
        }

        composeTestRule.onNodeWithText(thirdEmailBody).assertIsDisplayed()
        composeTestRule.onNodeWithText(thirdEmailSubject).performClick()

        composeTestRule.onNodeWithContentDescriptionForStringId(R.string.navigation_back)
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(thirdEmailBody).assertIsDisplayed()

        stateRestorationTester.emulateSavedInstanceStateRestore()

        composeTestRule.onNodeWithContentDescriptionForStringId(R.string.navigation_back)
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(thirdEmailBody).assertIsDisplayed()
    }

    @Test
    @TestExpandedWidth
    fun expandedDevice_selectedEmailEmailRetained_afterConfigChange() {
        val stateRestorationTester = StateRestorationTester(composeTestRule)
        val thirdEmail = LocalEmailsDataProvider.allEmails[2]
        val thirdEmailBody = composeTestRule.activity.getString(thirdEmail.body)
        val thirdEmailSubject = composeTestRule.activity.getString(thirdEmail.subject)

        stateRestorationTester.setContent {
            ReplyTheme {
                ReplyApp(windowSize = WindowWidthSizeClass.Expanded)
            }
        }

        composeTestRule.onNodeWithText(thirdEmailBody).assertIsDisplayed()
        composeTestRule.onNodeWithText(thirdEmailSubject).performClick()

        composeTestRule.onNodeWithTagForStringId(R.string.details_screen)
            .onChildren()
            .assertAny(hasAnyDescendant(hasText(thirdEmailBody)))

        stateRestorationTester.emulateSavedInstanceStateRestore()

        composeTestRule.onNodeWithTagForStringId(R.string.details_screen)
            .onChildren()
            .assertAny(hasAnyDescendant(hasText(thirdEmailBody)))
    }
}
