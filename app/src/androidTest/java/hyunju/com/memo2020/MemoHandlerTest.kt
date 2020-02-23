package hyunju.com.memo2020

import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import hyunju.com.memo2020.view.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MemoHandlerTest {

    @get:Rule
    @JvmField
    val memoHandlerTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun getImgFromAlbum() {
        getImgFromReqType(R.id.album_btn_edit_img)
    }

    @Test
    fun getImgFromCamera() {
        getImgFromReqType(R.id.camera_btn_edit_img)
    }

    @Test
    fun getImgFromUrl() {
        getImgFromReqType(R.id.uri_btn_edit_img)
    }

    fun getImgFromReqType(reqType: Int) {
        memoHandlerTestRule.launchActivity(Intent())
        Espresso.onView(withId(R.id.add_btn)).perform(click())
        Espresso.onView(withId(reqType)).perform(click())
    }


}