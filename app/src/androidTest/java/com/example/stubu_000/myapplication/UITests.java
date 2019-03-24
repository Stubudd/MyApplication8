package com.example.stubu_000.myapplication;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;


import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class UITests {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void TestSearchDate() {
        onView(withId(R.id.search_search)).perform(click());
        onView(withId(R.id.search_toDate)).perform(typeText("20190302"), closeSoftKeyboard());
        onView(withId(R.id.search_fromDate)).perform(typeText("20190301"), closeSoftKeyboard());
        onView(withId(R.id.search)).perform(click());
        for (int i = 0; i <= 5; i++) {
            onView(withId(R.id.btnRight)).perform(click());
        }
    }
    @Test
    public void TestSearchCaption() {
        onView(withId(R.id.search_search)).perform(click());
        onView(withId(R.id.search_toCaption)).perform(typeText("NoCaption"), closeSoftKeyboard());
        onView(withId(R.id.search)).perform(click());
        for (int i = 0; i <= 5; i++) {
            onView(withId(R.id.btnRight)).perform(click());
        }
    }
    @Test
    public void TestSearchLocation() {
        onView(withId(R.id.search_search)).perform(click());
        onView(withId(R.id.search_toLatitude)).perform(typeText("49.2696505"), closeSoftKeyboard());
        onView(withId(R.id.search_toLongitude)).perform(typeText("-123.1565645"), closeSoftKeyboard());
        onView(withId(R.id.search)).perform(click());
        for (int i = 0; i <= 5; i++) {
            onView(withId(R.id.btnRight)).perform(click());
        }
    }

    @Test
    public void TestSearchDateAndCaptionUpdate() {
        onView(withId(R.id.CaptionCaptured)).perform(typeText("BobCaption"), closeSoftKeyboard());
        onView(withId(R.id.buttonCaption)).perform(click());
        onView(withId(R.id.search_search)).perform(click());
        onView(withId(R.id.search_toCaption)).perform(typeText("BobCaption"), closeSoftKeyboard());
        onView(withId(R.id.search)).perform(click());
        for (int i = 0; i <= 5; i++) {
            onView(withId(R.id.btnRight)).perform(click());
        }
        onView(withId(R.id.search_search)).perform(click());
        onView(withId(R.id.search_toDate)).perform(typeText("20190302"), closeSoftKeyboard());
        onView(withId(R.id.search_fromDate)).perform(typeText("20190301"), closeSoftKeyboard());
        onView(withId(R.id.search)).perform(click());
        for (int i = 0; i <= 5; i++) {
            onView(withId(R.id.btnRight)).perform(click());
        }
    }
        }