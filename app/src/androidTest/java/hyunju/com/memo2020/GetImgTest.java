package hyunju.com.memo2020;


import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import hyunju.com.memo2020.view.MainActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class GetImgTest {

    @Rule
    public ActivityTestRule<MainActivity> mActvityRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() throws Exception {
        onView(withId(R.id.add_btn)).perform(click());
    }

    @Test
    public void getImgByAlbum() {
        onView(withId(R.id.album_btn_edit_img)).perform(click());
        Espresso.pressBack();
    }

    @Test
    public void getImgByCamera() {
        onView(withId(R.id.camera_btn_edit_img)).perform(click());
        Espresso.pressBack();
    }

    @Test
    public void getImgByUri() {
        onView(withId(R.id.uri_btn_edit_img)).perform(click());
//        String uri = "https://t1.daumcdn.net/cfile/tistory/993665425C469E250A";
        String uri = "https://image.chosun.com/sitedata/image/201410/30/2014103000633_0.jpg";


        onView(withId(R.id.url_input_et)).perform(replaceText(uri));
        onView(withId(R.id.input_btn)).perform(click());
        onView(withId(R.id.save_btn)).perform(click());
        Espresso.pressBack();
    }


    @After
    public void tearDown() throws Exception {

    }


}
