package com.example.android.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.TextView;

import java.util.ArrayList;


/**
 * {@link NewsAdapter} is an {@link ArrayAdapter} that can provide the layout for each list
 * based on a data source, which is a list of {@link News} objects.
 */
public class NewsAdapter extends ArrayAdapter<News> {

    /**
     * Create a new {@link NewsAdapter} object.
     *
     * @param context   is the current context (i.e. Activity) that the adapter is being created in.
     * @param newsItems is the list of {@link News} to be displayed, newsItems includes: Section,
     *                  title, date published and author(s)/contributor(s).
     */
    public NewsAdapter(Context context, ArrayList<News> newsItems) {

        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for four TextViews, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, newsItems);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_list_item, parent, false);
        }

        // Get the {@link News} object located at this position in the list
        News currentNews = getItem(position);

        // Find the TextView in the news_list_item.xml layout with the ID section_text_view
        TextView sectionTextView = (TextView) listItemView.findViewById(R.id.section_text_view);

        // Get the section's name from the current News object and set this text on the section TextView
        sectionTextView.setText(currentNews.getSection());

        // Find the TextView in the news_list_item.xml layout with the ID title_text_view
        TextView titleTextView = (TextView) listItemView.findViewById(R.id.title_text_view);

        // Get the title of the article from the current News object and set this text on the title TextView
        titleTextView.setText(currentNews.getTitle());

        // Find the TextView in the news_list_item.xml layout with the ID date_published_text_view
        TextView dateTextView = (TextView) listItemView.findViewById(R.id.date_published_text_view);

        // Check if there is a date published provided for the news article
        if (currentNews.hasDate()) {

            // Get the date published from the current News object and set this text on the date published TextView
            dateTextView.setText(currentNews.getDate());

            // Make sure the view is visible
            dateTextView.setVisibility(View.VISIBLE);

        } else {

            //Otherwise hide the date published TextView (set visibility to GONE)
            dateTextView.setVisibility(View.GONE);
        }

        // Find the TextView in the news_list_item.xml layout with the ID author_name_text_view
        TextView authorTextView = (TextView) listItemView.findViewById(R.id.author_name_text_view);

        //Check if there is (an) author(s)/contributor(s) provided for this article
        if (currentNews.hasAuthorName()) {

            //If there is (an) author(s)/contributor(s) provided.
            // Get the author(s)/contributor(s) from the current News object and set this text on the author TextView
            // If there is more than one author, the authors would have already been joined into one
            authorTextView.setText(currentNews.getAuthor());

            //Make sure the view is visible
            authorTextView.setVisibility(View.VISIBLE);

        } else {

            // Otherwise hide the author TextView (set visibility to GONE)
            authorTextView.setVisibility(View.GONE);
        }

        // Return the whole list item layout so that it can be shown in the ListView
        return listItemView;
    }
}
