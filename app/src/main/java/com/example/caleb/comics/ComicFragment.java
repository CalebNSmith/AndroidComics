package com.example.caleb.comics;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

/**
 * Created by Caleb on 10/9/15.
 */
public class ComicFragment extends Fragment {

    private static final String YEAR_KEY = "YEAR";
    private static final String MONTH_KEY = "MONTH";
    private static final String DAY_KEY = "DAY";
    private static final String COMIC_STRING = "COMIC_STRING";

    private ImageView mComicImage;
    private int mYear;
    private int mMonth;
    private int mDay;
    private String mComic;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.comic_fragment, container, false);

        mComicImage = (ImageView) v.findViewById(R.id.comic_imageview);

        mYear = getArguments().getInt(YEAR_KEY);
        mMonth = getArguments().getInt(MONTH_KEY);
        mDay = getArguments().getInt(DAY_KEY);
        mComic = getArguments().getString(COMIC_STRING);

        try {
            new ImageFetch(mComicImage).execute(getURL(mYear, mMonth, mDay, mComic));
        } catch(Exception e) {
            Log.d("Comics", e.getMessage());
        }

        return v;
    }



    public static ComicFragment newInstance(int year, int month, int day, String comic) {
        ComicFragment fragment = new ComicFragment();

        Bundle bundle = new Bundle();
        bundle.putInt(YEAR_KEY, year);
        bundle.putInt(MONTH_KEY, month);
        bundle.putInt(DAY_KEY, day);
        bundle.putString(COMIC_STRING, comic);

        fragment.setArguments(bundle);

        return fragment;
    }

    private class ImageFetch extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public ImageFetch(ImageView iv) {
            imageView = iv;
        }


        /*
        Finds the comic image on the page and returns a bitmap of it
         */
        @Override
        protected Bitmap doInBackground(String... params) {
            String url = "";
            try {
                Document doc = Jsoup.connect(params[0]).get();
                Elements tagElements = doc.getElementsByTag("img");
                for(int i = 0; i < tagElements.size(); i++) {
                    if(tagElements.get(i).hasAttr("src")) {
                        if(tagElements.get(i).attr("src").startsWith("http://assets.amuniversal")) {
                            url = tagElements.get(i).attr("src").toString() + ".png";
                        }
                    }
                }

            } catch(Exception e) {}

            Bitmap comic = null;
            try {
                InputStream in = new URL(url).openStream();
                comic = BitmapFactory.decodeStream(in);
                in.close();
            } catch(Exception e) {
            }
            return comic;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
             imageView.setImageBitmap(bitmap);
        }
    }

    private String getURL(int year, int month, int day, String comic) {
        String yearString = String.valueOf(year);
        String monthString;
        String dayString;

        //Pad a 0 if the day or month is less than 10, e.g. "3" -> "03"
        if(month < 10)
            monthString = "0" + String.valueOf(month);
        else
            monthString = String.valueOf(month);

        if(day < 10)
            dayString = "0" + String.valueOf(day);
        else
            dayString = String.valueOf(day);

        return "http://www.gocomics.com/" + comic + "/" + yearString + "/" + monthString + "/" + dayString;
    }


}
