package org.d3ifcool.nabung;

import android.util.Log;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.Buffer;

import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
public class UiTest {
    @Rule
    public ActivityTestRule<LoginActivity> mLoginTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Before
    public void init() {
        mLoginTestRule.getActivity();
    }

    @Test
    public void UiTest() throws InterruptedException, IOException {

//      Register
        String nama = "fikri";
        String email = "fikrifikri@gmail.com";
        String pass = "satuduatiga";

//      Login
        String username = "fikrifikri@gmail.com";
        String password = "satuduatiga";

//      wishlist
        String namabarang = "bola basket";
        String hargabarang = "10000";
        String target = "100";

//      Pemasukan
        String saldo = "10000";
        String catatan = "beli wiskes";

//      pengeluaran
        String saldokeluar = "20000";
        String catatkeluar = "40000";


//      Register
        Espresso.onView(withId(R.id.tv_daftar)).perform(click());
        Espresso.onView(withId(R.id.et_nama_regis)).perform(typeText(nama), closeSoftKeyboard());
        Espresso.onView(withId(R.id.et_email_regis)).perform(typeText(email), closeSoftKeyboard());
        Espresso.onView(withId(R.id.et_password_regis)).perform(typeText(pass), closeSoftKeyboard());
        Espresso.onView(withId(R.id.bt_register)).perform(click());
        Thread.sleep(7000);

        //      Stage 2 Login
        Espresso.onView(withId(R.id.et_email_login)).perform(typeText(username), closeSoftKeyboard());
        Espresso.onView(withId(R.id.et_password_login)).perform(typeText(password), closeSoftKeyboard());
        Espresso.onView(withId(R.id.bt_login)).perform(click());
        Thread.sleep(5000);

        //Stage 3 Tambah Wishlists
        Espresso.onView(withId(R.id.iv_tambah_wishlist)).perform(click());
        Espresso.onView(withId(R.id.et_tambah_judul_wsh)).perform(typeText(namabarang), closeSoftKeyboard());
        Espresso.onView(withId(R.id.et_tambah_saldo_wsh)).perform(typeText(hargabarang), closeSoftKeyboard());
        Espresso.onView(withId(R.id.et_tambah_hari_wsh)).perform(typeText(target), closeSoftKeyboard());
        Espresso.onView(withId(R.id.bt_tambah_wishlist)).perform(click());
        Thread.sleep(2000);

        // klik nav pemasukan
        Espresso.onView(withId(R.id.navigation_pemasukan)).perform(click());
        Thread.sleep(2000);

        // Tambah pemasukan
        Espresso.onView(withId(R.id.bt_tambah_pemasukan)).perform(click());
        Espresso.onView(withId(R.id.et_catatan_tambah_pms)).perform(typeText(catatan), closeSoftKeyboard());
        Espresso.onView(withId(R.id.et_saldo_tambah_pms)).perform(typeText(saldo), closeSoftKeyboard());
        Espresso.onView(withId(R.id.bt_tambah_pms)).perform(click());
        Thread.sleep(2000);

        //klik nav pengeluaran
        Espresso.onView(withId(R.id.navigation_pengeluaran)).perform(click());
        Thread.sleep(2000);

        //Tambah pengeluaran
        Espresso.onView(withId(R.id.bt_tambah_pengeluaran)).perform(click());
        Espresso.onView(withId(R.id.et_tambah_catatan_plr)).perform(typeText(catatkeluar), closeSoftKeyboard());
        Espresso.onView(withId(R.id.et_tambah_saldo_plr)).perform(typeText(saldokeluar), closeSoftKeyboard());
        Espresso.onView(withId(R.id.bt_tambah_plr)).perform(click());
        Thread.sleep(2000);

        //klik nav wishlist
        Espresso.onView(withId(R.id.navigation_profile)).perform(click());
        Espresso.onView(withId(R.id.bt_logout)).perform(click());
        Thread.sleep(2000);

    }
}