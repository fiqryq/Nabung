package org.d3ifcool.nabung;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.sunflower.nabung.LoginActivity;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class TambahPengeluaranTest {
    @Rule
    public ActivityTestRule<LoginActivity> mLoginTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Before
    public void init() {
        mLoginTestRule.getActivity();
    }


    @Test
    public void tambahpengeluaran() throws InterruptedException {

        String username = "fiqrychoerudin@gmail.com";
        String password = "123123";
        String catatan = "beli makan";
        String pengeluaran = "1200";

//        login
        Espresso.onView(withId(R.id.et_email_login)).perform(typeText(username));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.et_password_login)).perform(typeText(password));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.bt_login)).perform(click());
        Thread.sleep(5000);

//      klik fragment pemasukan lalu tambah pemasukan
        Espresso.onView(withId(R.id.navigation_pengeluaran)).perform(click());
        Espresso.onView(withId(R.id.bt_tambah_pengeluaran)).perform(click());
        Espresso.onView(withId(R.id.et_tambah_catatan_plr)).perform(typeText(catatan));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.et_tambah_saldo_plr)).perform(typeText(pengeluaran));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.bt_tambah_plr)).perform(click());
        Thread.sleep(5000);
    }
}