package com.example.android.newsapp;

import android.util.Log;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


/**
 * Helper methods related to requesting and receiving news data from The Guardian api.
 */
public final class QueryUtils {

    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();
    /**
     * Global variables used to store the authors/contributors from the
     * JSONArray "tags"
     */
    static StringBuilder builder = new StringBuilder();
    static String authorWebTitle;

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Query the Guardian dataset and return a list of {@link News} objects.
     */
    public static List<News> fetchNewsData(String requestUrl) {

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);

        } catch (IOException e) {

            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link News}
        List<News> newsList = extractFeatureFromJson(jsonResponse);

        // Return the list of {@link News}
        return newsList;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;

        try {
            url = new URL(stringUrl);

        } catch (MalformedURLException e) {

            Log.e(LOG_TAG, "Problem building the URL ", e);
        }

        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {

        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {

            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {

                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {

            Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e);
        } finally {
            if (urlConnection != null) {

                urlConnection.disconnect();
            }
            if (inputStream != null) {

                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }

        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {

        StringBuilder output = new StringBuilder();

        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();

            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }

        return output.toString();
    }

    /**
     * Return a list of {@link News} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<News> extractFeatureFromJson(String newsJSON) {

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding news items to.
        List<News> newsItems = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(newsJSON);

            // Extract the JSONObject associated with the key called "response",
            // which represents a list of response.
            JSONObject newsObject = baseJsonResponse.getJSONObject("response");

            // Extract the JSONArray associated with the key called "results",
            // which represents a list of results (or news items) such as: "id",
            // "type", sectionId" etc.
            JSONArray newsArray = newsObject.getJSONArray("results");

            //For each news item in the newsArray, create a {@link News} object
            for (int i = 0; i < newsArray.length(); i++) {

                // Get a single news at position i within the list of news items
                JSONObject currentNews = newsArray.getJSONObject(i);

                // Extract the value for the key called "sectionName"
                String sectionName = currentNews.getString("sectionName");

                // Extract the value for the key called "webTitle"
                String webTitle = currentNews.getString("webTitle");

                // Extract the value for the key called "webPublicationDate"
                String webPublicationDate = currentNews.getString("webPublicationDate");

                //Remove all characters after and including the "T" in the string:
                //webPublicationDate variable
                //For example: "2018-09-25T15:45:34Z" from JSON Primitive: "webPublicationDate"
                //becomes: "2018-09-25" and is stored in String webPublicationDate variable
                webPublicationDate = webPublicationDate.split("T")[0];

                // Extract the value for the key called "webUrl"
                String url = currentNews.getString("webUrl");

                // Extract the JSONArray associated with the key called "tags",
                // which represents a list of tags (or tag items) such as: "id",
                // "type","webTitle" etc.
                JSONArray tagsArray = currentNews.getJSONArray("tags");

                //Remove current authors/contributors' data before new data is appended
                builder.setLength(0);

                //Check if tagsArray is not empty, if not proceed to 'for' loop to extract author(s)
                if (tagsArray.length() > 0) {

                    //the 'for' loop is used to check if there is more than one author/contributor
                    // for the same article in the tagsArray.
                    //If there is more than one author, the StringBuilder will build a 'list'
                    //of authors and store them in the authorWebTitle variable
                    for (int j = 0; j < tagsArray.length(); j++) {

                        //Get an author/contributor at position i within the list of tags
                        JSONObject currentAuthor = tagsArray.getJSONObject(j);

                        //If there is more than one author/contributor, based on the length of the
                        //JSONArray tagsArray, then all authors/contributors are appended together
                        //into the StringBuilder variable: 'builder', then 'builder' is converted to
                        //string and stored in the string: 'authorWebTitle'
                        if (tagsArray.length() > 1) {

                            // Extract the value for the key called "webTitle"
                            //"webTitle" contains the author/contributor's name for the news article
                            builder.append(currentAuthor.getString("webTitle"));
                            builder.append("\n");

                            //When all authors/contributors are appended, they are stored into the
                            //authorWebTitle string
                            authorWebTitle = builder.toString();
                        } else {

                            //Only one author/contributor
                            // Extract the value for the key called "webTitle"
                            //"webTitle" contains the author/contributor's name for the news article
                            builder.append(currentAuthor.getString("webTitle"));
                            authorWebTitle = builder.toString();
                        }
                    }
                } else {

                    //otherwise, authorWebTitle ="", if tagsArray is empty
                    authorWebTitle = "";
                }

                // Create a new {@link News} object with the sectionName,webTitle,
                // authorWebTitle,webPublicationDate,url from the JSON response.
                News news = new News(sectionName, webTitle, authorWebTitle, webPublicationDate, url);

                // Add the new {@link News} to the list of news items
                newsItems.add(news);
            }

        } catch (JSONException e) {

            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the news JSON results", e);
        }

        // Return the list of news items
        return newsItems;
    }
}
