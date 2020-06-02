package com.example.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(@NonNull Context context, List<News> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View currentListView = convertView;
        if (currentListView== null){
            currentListView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        News currentNews = getItem(position);
        if (currentNews != null) {
            TextView listTitle = (TextView) currentListView.findViewById(R.id.list_item_title);
            listTitle.setText(currentNews.getTitle());

            TextView listDate = (TextView) currentListView.findViewById(R.id.list_item_date);
            String date = currentNews.getDate().substring(0, 10) + " " + currentNews.getDate().substring(11, 16);
            listDate.setText(date);

            TextView listType = (TextView) currentListView.findViewById(R.id.list_item_type);
            listType.setText(currentNews.getSection());
        }
        return currentListView;
    }
}
