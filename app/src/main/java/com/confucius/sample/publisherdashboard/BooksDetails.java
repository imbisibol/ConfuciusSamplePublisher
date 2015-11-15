package com.confucius.sample.publisherdashboard;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by imbisibol on 11/15/2015.
 */
public class BooksDetails extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.books_details);

        Intent intent = getIntent();
        String bookId = intent.getStringExtra(getString(R.string.INTENT_BookId));


    }

}
