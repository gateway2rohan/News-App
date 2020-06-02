package com.example.newsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {
    private static final int NEWS_LOADER_ID = 1;
    public static final String GUARDIAN_REQUEST_URL = "https://content.guardianapis.com/search?api-key=be499361-20cf-4522-91c3-3120c45f4559";

    private ListView mListView;
    private TextView mEmptyStateTextView;
    private NewsAdapter mAdapter;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.list_view);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        mListView.setEmptyView(mEmptyStateTextView);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

        final ArrayList<News> news = new ArrayList<News>();
        mAdapter = new NewsAdapter(this, news);
        mListView.setAdapter(mAdapter);

        // To check for internet connectivity
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            //initiating Loader
            getSupportLoaderManager().initLoader(NEWS_LOADER_ID, null, this);

        } else {
            mProgressBar.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News currentClickedNews = news.get(position);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(currentClickedNews.getUrl()));
                startActivity(intent);
            }
        });
    }


    /**
     * It is called when getSupportLoaderManager.initLoader() is called.
     * @param id
     * @param args
     * @return new NewsLoader
     */
    @NonNull
    @Override
    public Loader<List<News>> onCreateLoader(int id, @Nullable Bundle args) {
        return new NewsLoader(MainActivity.this, GUARDIAN_REQUEST_URL);
    }

    /**
     * It is called after data is fetched from server and list contains required data.
     * @param loader
     * @param data
     */
    @Override
    public void onLoadFinished(@NonNull Loader<List<News>> loader, List<News> data) {

        mProgressBar.setVisibility(View.GONE);
        mEmptyStateTextView.setText(R.string.no_news);

        //Clear the adapter of previous loaded data
        mAdapter.clear();

        if (data!=null && !data.isEmpty())
            mAdapter.addAll(data);

    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<News>> loader) {
        mAdapter.clear();
    }
}