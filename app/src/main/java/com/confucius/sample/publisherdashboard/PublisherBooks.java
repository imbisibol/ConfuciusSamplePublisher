package com.confucius.sample.publisherdashboard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by imbisibol on 11/15/2015.
 */
public class PublisherBooks extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private BookTask mAuthTask = null;
    private ProgressBar mProgressBar;
    private View dvContent;
    private BooksAdapter booksAdapter;
    private GridView gridview;

    private ArrayList<DATAReadingMaterial> bookData = new ArrayList<>();
    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PublisherBooks newInstance(int sectionNumber) {
        PublisherBooks fragment = new PublisherBooks();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public PublisherBooks() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.publisher_books, container, false);

        mProgressBar = ((ProgressBar)rootView.findViewById(R.id.login_progress));
        dvContent = (rootView.findViewById(R.id.dvContent));
        gridview = (GridView)rootView.findViewById(R.id.gvDataList);

        //GET DATA
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                Toast.makeText(getActivity().getBaseContext(), bookData.get(position).Id, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity().getBaseContext(), BlogsDetails.class);
                intent.putExtra(getString(R.string.INTENT_BookId), bookData.get(position).Id);
                startActivity(intent);
            }
        });

        GetBooks();

        return rootView;
    }

    public void GetBooks() {

        if (mAuthTask != null) {
            return;
        }

        bookData = new ArrayList<>();
        mAuthTask = new BookTask(getActivity().getString(R.string.tempPublisherId));
        mAuthTask.execute((Void) null);
    }


    public class BookTask extends AsyncTask<Void, Void, Boolean> {

        private final String UserId;

        BookTask(String userId) {
            UserId = userId;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            JSONObject jsonResponse = null;

            try {

                JSONObject jsonParam = new JSONObject();

                Common comm = new Common();
                comm.setAPIURL(getActivity().getString(R.string.ConfuciusLibraryAPIURL));
                jsonResponse = comm.GetAPI("/api/ReadingMaterial/?title=&author=&subjectArea=&tag=&userId=" + UserId
                        + "&authToken=" + getActivity().getString(R.string.ApplicationId) + "&pageNo=1&pageSize=100000");

                if (jsonResponse != null) {

                    String strSuccess = jsonResponse.getString("Success");

                    if (strSuccess == "true") {

                        JSONObject responseData = jsonResponse.getJSONObject("ResponseData");

                        if(responseData != null) {

                            JSONArray courses = responseData.getJSONArray("ReadingMaterials");

                            for(int ctr=0;ctr<courses.length();ctr++) {
                                DATAReadingMaterial newData = new DATAReadingMaterial();

                                newData.Id = courses.getJSONObject(ctr).getString("Id");
                                newData.Description = courses.getJSONObject(ctr).getString("Description");
                                newData.DateModified = courses.getJSONObject(ctr).getString("DateModified");
                                newData.SubjectArea = courses.getJSONObject(ctr).getString("SubjectArea");
                                newData.Tags = courses.getJSONObject(ctr).getString("Tags");
                                newData.Title = courses.getJSONObject(ctr).getString("Title");
                                newData.CoverImageURL = courses.getJSONObject(ctr).getString("CoverImageURL");
                                newData.Edition = courses.getJSONObject(ctr).getString("Edition");
                                newData.PriceRange = courses.getJSONObject(ctr).getString("PriceRange");
                                newData.PublicationDate = courses.getJSONObject(ctr).getString("PublicationDate");

                                bookData.add(newData);
                            }
                        }

                    }
                }

            } catch (Exception ex) {

                String abc = ex.getMessage();

            }


            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {

                booksAdapter = new BooksAdapter(getActivity(), R.layout.books_listrow, bookData);
                gridview.setAdapter(booksAdapter);

            } else {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity().getBaseContext());
                dialog.setTitle("Message Alert");
                dialog.setMessage("Failed tor retrieve Course List!");
                dialog.show();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);


        }
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mProgressBar.setVisibility(show ? View.GONE : View.VISIBLE);
            dvContent.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    dvContent.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressBar.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            dvContent.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}