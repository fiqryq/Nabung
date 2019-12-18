package org.d3ifcool.nabung;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class RegisterActivityTest {
    @Rule
    public ActivityTestRule<LoginActivity> mLoginTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Before
    public void init(){
        mLoginTestRule.getActivity();
    }

    @Test
    public void registerakun(){
        String username = "fiqry";
        String password = "123123";
        String email = "fiqrychoerudin@gmail.com";

//      input form register
        Espresso.onView(withId(R.id.tv_daftar)).perform(click());
        Espresso.onView(withId(R.id.et_nama_regis)).perform(typeText(username));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.et_password_regis)).perform(typeText(password));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.et_email_regis)).perform(typeText(email));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.bt_register)).perform(click());
    }
}