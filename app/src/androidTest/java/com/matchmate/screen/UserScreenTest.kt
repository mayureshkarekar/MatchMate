package com.matchmate.screen

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.matchmate.model.Dob
import com.matchmate.model.Location
import com.matchmate.model.Name
import com.matchmate.model.Picture
import com.matchmate.model.User
import com.matchmate.model.User.Companion.PENDING
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserDetailScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * Test for checking if the view has click action.
     **/
    @Test
    fun testOpenInBrowserClick_ExpectedHasClickAction() {
        // Rendering the view.
        composeTestRule.setContent {
            UserItem(
                user = User(
                    id = 1,
                    name = Name("Tony", "Stark"),
                    dob = Dob(50, "21/01/1960"),
                    gender = "male",
                    location = Location("Thane", "Maharashtra", "India"),
                    picture = Picture(
                        "https://picsum.photos/300/150.jpg",
                        "https://picsum.photos/200/100.jpg"
                    ),
                    status = PENDING
                )
            ) {}
        }

        // Verifying if the button has click action.
        val btnAccept = composeTestRule.onNodeWithTag(TEST_TAG_BTN_ACCEPT)
        btnAccept.assertHasClickAction()

        val btnReject = composeTestRule.onNodeWithTag(TEST_TAG_BTN_REJECT)
        btnReject.assertHasClickAction()
    }
}