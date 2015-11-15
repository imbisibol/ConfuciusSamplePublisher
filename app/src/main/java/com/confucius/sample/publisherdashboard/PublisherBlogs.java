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
public class PublisherBlogs extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private BlogTask mAuthTask = null;
    private ProgressBar mProgressBar;
    private View dvContent;
    private BlogsAdapter blogsAdapter;
    private GridView gridview;

    private ArrayList<DATAArticle> blogData = new ArrayList<>();
    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PublisherBlogs newInstance(int sectionNumber) {
        PublisherBlogs fragment = new PublisherBlogs();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public PublisherBlogs() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.publisher_blogs, container, false);

        mProgressBar = ((ProgressBar)rootView.findViewById(R.id.login_progress));
        dvContent = (rootView.findViewById(R.id.dvContent));
        gridview = (GridView)rootView.findViewById(R.id.gvDataList);

        //GET DATA
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                Toast.makeText(getActivity().getBaseContext(), blogData.get(position).Id, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity().getBaseContext(), BlogsDetails.class);
                intent.putExtra(getString(R.string.INTENT_BlogId), blogData.get(position).Id);
                startActivity(intent);
            }
        });

        GetBlogs();


        return rootView;
    }

    public void GetBlogs() {

        if (mAuthTask != null) {
            return;
        }

        blogData = new ArrayList<>();
        mAuthTask = new BlogTask(getActivity().getString(R.string.tempPublisherId));
        mAuthTask.execute((Void) null);
    }


    public class BlogTask extends AsyncTask<Void, Void, Boolean> {

        private final String UserId;

        BlogTask(String userId) {
            UserId = userId;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            JSONObject jsonResponse = null;

            try {

                JSONObject jsonParam = new JSONObject();

                Common comm = new Common();
                comm.setAPIURL(getActivity().getString(R.string.ConfuciusLibraryAPIURL));
                jsonResponse = comm.GetAPI("/api/Article/?title=&subjectArea=&tag=&userId=" + UserId
                        + "&authToken=" + getActivity().getString(R.string.ApplicationId) + "&pageNo=1&pageSize=100000");

                if (jsonResponse != null) {

                    String strSuccess = jsonResponse.getString("Success");

                    if (strSuccess == "true") {

                        JSONObject responseData = jsonResponse.getJSONObject("ResponseData");

                        if(responseData != null) {

                            JSONArray courses = responseData.getJSONArray("Articles");

                            for(int ctr=0;ctr<courses.length();ctr++) {
                                DATAArticle newData = new DATAArticle();

                                newData.Id = courses.getJSONObject(ctr).getString("Id");
                                newData.Description = courses.getJSONObject(ctr).getString("Description");
                                newData.DateModified = courses.getJSONObject(ctr).getString("DateModified");
                                newData.SubjectArea = courses.getJSONObject(ctr).getString("SubjectArea");
                                newData.HTMLContent = courses.getJSONObject(ctr).getString("HTMLContent");
                                newData.SubTitle = courses.getJSONObject(ctr).getString("SubTitle");
                                newData.Tags = courses.getJSONObject(ctr).getString("Tags");
                                newData.Title = courses.getJSONObject(ctr).getString("Title");

                                blogData.add(newData);
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

                blogsAdapter = new BlogsAdapter(getActivity(), R.layout.blogs_listrow, blogData);
                gridview.setAdapter(blogsAdapter);

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


    //@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
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
