package org.d3ifcool.nabung;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.sunflower.nabung.LoginActivity;

import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class DataDriven {
    @Rule
    public ActivityTestRule<LoginActivity> mLoginTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Before
    public void init() {
        mLoginTestRule.getActivity();
    }

    @Test
    public void datadriven() throws InterruptedException, IOException {
        BufferedReader bufferedReader
                = new BufferedReader(new InputStreamReader(openFile("data.csv")));

        String line = "";
        int iteration = 0;
//        while ((line = bufferedReader.readLine()) != null) {
        line = bufferedReader.readLine();
        String[] str = line.split(",");

        //Login
        String username1 = str[0].toString().replace("\"", "");
        String password1 = str[1].toString().replace("\"", "");
        String username2 = str[2].toString().replace("\"", "");
        String password2 = str[3].toString().replace("\"", "");
        String username3 = str[4].toString().replace("\"", "");
        String password3 = str[5].toString().replace("\"", "");
        String username4 = str[6].toString().replace("\"", "");
        String password4 = str[7].toString().replace("\"", "");

        //     Wishlist
        String namabarang = str[8].toString().replace("\"", "");
        String hargabarang = str[9].toString().replace("\"", "");
        String target = str[10].toString().replace("\"", "");

//        Pemasukan
        String saldo = str[11].toString().replace("\"", "");
        String catatan = str[12].toString().replace("\"", "");


        //      Stage 1 - Email salah password salah
        Espresso.onView(withId(R.id.et_email_login)).perform(typeText(username1), closeSoftKeyboard());
        Espresso.onView(withId(R.id.et_password_login)).perform(typeText(password1), closeSoftKeyboard());
        Espresso.onView(withId(R.id.bt_login)).perform(click());
        Thread.sleep(1000);
        Espresso.onView(withId(R.id.et_email_login)).perform(clearText());
        Espresso.onView(withId(R.id.et_password_login)).perform(clearText());

        //      Stage 2 - Email salah password Benar
        Espresso.onView(withId(R.id.et_email_login)).perform(typeText(username2), closeSoftKeyboard());
        Espresso.onView(withId(R.id.et_password_login)).perform(typeText(password2), closeSoftKeyboard());
        Espresso.onView(withId(R.id.bt_login)).perform(click());
        Thread.sleep(1000);
        Espresso.onView(withId(R.id.et_email_login)).perform(clearText());
        Espresso.onView(withId(R.id.et_password_login)).perform(clearText());

        //      Stage 3 - Email benar password salah
        Espresso.onView(withId(R.id.et_email_login)).perform(typeText(username3), closeSoftKeyboard());
        Espresso.onView(withId(R.id.et_password_login)).perform(typeText(password3), closeSoftKeyboard());
        Espresso.onView(withId(R.id.bt_login)).perform(click());
        Thread.sleep(1000);
        Espresso.onView(withId(R.id.et_email_login)).perform(clearText());
        Espresso.onView(withId(R.id.et_password_login)).perform(clearText());

        //      Stage 4 - Email benar password Benar
        Espresso.onView(withId(R.id.et_email_login)).perform(typeText(username4), closeSoftKeyboard());
        Espresso.onView(withId(R.id.et_password_login)).perform(typeText(password4), closeSoftKeyboard());
        Espresso.onView(withId(R.id.bt_login)).perform(click());
        Thread.sleep(5000);

        //      Stage 5 Tambah Wishlists
        Espresso.onView(withId(R.id.iv_tambah_wishlist)).perform(click());
        Espresso.onView(withId(R.id.et_tambah_judul_wsh)).perform(typeText(namabarang), closeSoftKeyboard());
        Espresso.onView(withId(R.id.et_tambah_saldo_wsh)).perform(typeText(hargabarang), closeSoftKeyboard());
        Espresso.onView(withId(R.id.et_tambah_hari_wsh)).perform(typeText(target), closeSoftKeyboard());
        Espresso.onView(withId(R.id.bt_tambah_wishlist)).perform(click());
        Thread.sleep(1000);

        //        klik nav pemasukan
        Espresso.onView(withId(R.id.navigation_pemasukan)).perform(click());
        Thread.sleep(1000);

        //        Tambah pms
        Espresso.onView(withId(R.id.bt_tambah_pemasukan)).perform(click());
        Espresso.onView(withId(R.id.et_catatan_tambah_pms)).perform(typeText(catatan), closeSoftKeyboard());
        Espresso.onView(withId(R.id.et_saldo_tambah_pms)).perform(typeText(saldo), closeSoftKeyboard());
        Espresso.onView(withId(R.id.bt_tambah_pms)).perform(click());
        Thread.sleep(1000);
    }

    //    }
    private InputStream openFile(String filename) throws IOException {
        return getClass().getClassLoader().getResourceAsStream(filename);
    }
}