package com.example.caleb.comics;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager pager = (ViewPager) findViewById(R.id.viewPager);

        //get comic name in format for the URL (without spaces and all lowercase)
        String comic = getPreferences(Context.MODE_PRIVATE).getString(getString(R.string.comic_name), "calvinandhobbes");

        pager.setAdapter(new ComicAdapter(getSupportFragmentManager(), comic));
        pager.setCurrentItem(9999, true); //Set to 9999 so there are 10000 comics to scroll through, from right to left

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /*
    If an option is selected from the menu, change the default comic in the preferences to the one selected and reload the activity
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        switch(item.getItemId()) {
            case R.id.comic_bc:
                editor.putString(getString(R.string.comic_name), "bc");
                break;
            case R.id.comic_calvinandhobbes:
                editor.putString(getString(R.string.comic_name), "calvinandhobbes");
                break;
            case R.id.comic_foxtrot:
                editor.putString(getString(R.string.comic_name), "foxtrot");
                break;
            case R.id.comic_garfield:
                editor.putString(getString(R.string.comic_name), "garfield");
                break;
            case R.id.comic_nonsequitur:
                editor.putString(getString(R.string.comic_name), "nonsequitur");
                break;
            case R.id.comic_peanuts:
                editor.putString(getString(R.string.comic_name), "peanuts");
                break;
            case R.id.comic_pearlsbeforeswine:
                editor.putString(getString(R.string.comic_name), "pearlsbeforeswine");
                break;
        }
        editor.commit();
        finish();
        startActivity(getIntent());
        return super.onOptionsItemSelected(item);
    }


    private class ComicAdapter extends FragmentPagerAdapter {

        private String mComic;

        public ComicAdapter(FragmentManager fm,String comic) {
            super(fm);
            mComic = comic;

        }

        @Override
        public Fragment getItem(int pos) {
            long time = System.currentTimeMillis();
            time -= 1000*60*60*24*(9999 - pos); //pos defaults to 9999. Each time the user scrolls left, pos will decrement by 1, making the time subtract 24 hours
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTimeInMillis(time);
            return ComicFragment.newInstance(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH), mComic);
        }

        @Override
        public int getCount() {
            return 10000;
        }
    }



}
