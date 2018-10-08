package com.example.android.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;


import java.util.List;

/**
 * Loads a list of news items by using an AsyncTask to perform the
 * network request to the given URL.
 */
public class NewsLoader extends AsyncTaskLoader<List<News>> {

    /**
     * Query URL
     */
    private String mUrl;

    /**
     * Constructs a new {@link NewsLoader}.
     *
     * @param context of the activity
     * @param url     to load data from
     */
    public NewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {

        //Force an asynchronous load.
        // This will ignore a previously loaded data set and load a new one.
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<News> loadInBackground() {

        //Return null if mURL is null
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of newsItems.
        List<News> newsItems = QueryUtils.fetchNewsData(mUrl);
        return newsItems;
    }
}
