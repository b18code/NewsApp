package com.example.android.newsapp;


public class News {

    /**
     * This checks if there isn't an author's/contributor's name
     */
    private static final String noAuthor = "";
    /**
     * This checks if there isn't a date published for the news article
     */
    private static final String noDatePublished = "";
    /**
     * Section the news article belongs to
     */
    private String mSection;
    /**
     * Title of the news article (article can also mean letters, reviews etc)
     */
    private String mTitle;
    /**
     * Author(s)/contributor(s) who wrote the article (article can also mean letters, reviews etc)
     */
    private String mAuthor;
    /**
     * Date the article was published on the web
     */
    private String mDate;
    /**
     * Website (web)URL of the news item
     */
    private String mUrl;

    /**
     * Constructs a new {@link News} object.
     *
     * @param section is the section the news article belongs to (e.g. Music)
     * @param title   is the title of the article (e.g. Arianna Neikrug: Changes review – a hugely impressive debut)
     * @param author  who wrote the article. Also known as contributor (e.g. Dave Gelly)
     * @param date    is when the article was published on the web (e.g. 2018-09-23T07:00:00Z)
     * @param url     is the website URL to the article
     *                (e.g. https://www.theguardian.com/music/2018/sep/16/arianna-neikrug-changes-review)
     */
    public News(String section, String title, String author, String date, String url) {

        mSection = section;
        mTitle = title;
        mAuthor = author;
        mDate = date;
        mUrl = url;
    }

    /**
     * Get the section the news article belongs to
     */
    public String getSection() {
        return mSection;
    }

    /**
     * Get the title of the news article
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * Returns the author who wrote the news article
     */
    public String getAuthor() {
        return mAuthor;
    }

    /**
     * Returns whether or not there is an author's/contributor's name
     */
    public boolean hasAuthorName() {
        return (!mAuthor.equals(noAuthor));
    }

    /**
     * Get the date the news article was published onto the web
     */
    public String getDate() {
        return mDate;
    }

    /**
     * Returns whether or not there is a date published
     */
    public boolean hasDate() {
        return (!mDate.equals(noDatePublished));
    }

    /**
     * Returns the website URL to find more information about the news item.
     */
    public String getUrl() {
        return mUrl;
    }
}
