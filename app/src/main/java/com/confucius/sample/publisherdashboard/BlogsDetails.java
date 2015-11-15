package com.confucius.sample.publisherdashboard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by imbisibol on 11/15/2015.
 */
public class BlogsDetails extends ActionBarActivity {

    private BlogTask mAuthTask = null;
    private ProgressBar mProgressBar;
    private View dvContent;

    private DATAArticle blogData = new DATAArticle();
    private TextView lblTitle;
    private TextView lblSubTitle;
    private TextView lblSubjectArea;
    private TextView lblDateModified;
    private WebView wvAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blogs_details);

        mProgressBar = ((ProgressBar)findViewById(R.id.login_progress));
        dvContent = (findViewById(R.id.dvContent));

        lblTitle = (TextView)findViewById(R.id.lblTitle);
        lblSubTitle = (TextView)findViewById(R.id.lblSubTitle);
        lblSubjectArea = (TextView)findViewById(R.id.lblSubjectArea);
        lblDateModified = (TextView)findViewById(R.id.lblDateModified);
        wvAbout = (WebView)findViewById(R.id.wvAbout);

        Intent intent = getIntent();
        String blogId = intent.getStringExtra(getString(R.string.INTENT_BlogId));
        GetBlogDetails(blogId);


    }

    private void GetBlogDetails(String id) {
        if (mAuthTask != null) {
            return;
        }

        blogData = new DATAArticle();
        mAuthTask = new BlogTask(id);
        mAuthTask.execute((Void) null);
    }

    public class BlogTask extends AsyncTask<Void, Void, Boolean> {

        private final String ArticleId;

        BlogTask(String articleID) {
            ArticleId = articleID;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            JSONObject jsonResponse = null;

            try {

                JSONObject jsonParam = new JSONObject();

                Common comm = new Common();
                comm.setAPIURL(getString(R.string.ConfuciusLibraryAPIURL));
                jsonResponse = comm.GetAPI("/api/Article/?id=" + ArticleId
                        + "&authToken=" + getString(R.string.ApplicationId));

                if (jsonResponse != null) {

                    String strSuccess = jsonResponse.getString("Success");

                    if (strSuccess == "true") {

                        JSONObject responseData = jsonResponse.getJSONObject("ResponseData");

                        if(responseData != null) {

                            JSONObject article = responseData.getJSONObject("Article");

                            if(article != null) {
                                blogData.Id = article.getString("Id");
                                blogData.Description = article.getString("Description");
                                blogData.DateModified = article.getString("DateModified");
                                blogData.SubjectArea = article.getString("SubjectArea");
                                blogData.HTMLContent = article.getString("HTMLContent");
                                blogData.SubTitle = article.getString("SubTitle");
                                blogData.Tags = article.getString("Tags");
                                blogData.Title = article.getString("Title");
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

            if (success && blogData != null) {

                lblTitle.setText(blogData.Title);
                lblSubTitle.setText(blogData.SubTitle);
                lblSubjectArea.setText(blogData.SubjectArea);
                lblDateModified.setText(blogData.DateModified);
                wvAbout.loadData("<div style='padding:5px'><p>" + blogData.Description + "</p><hr/>" + blogData.HTMLContent +  "<br/><br/>## " + blogData.Tags  + "</div>", "text/html", "utf-8");



            } else {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getBaseContext());
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

