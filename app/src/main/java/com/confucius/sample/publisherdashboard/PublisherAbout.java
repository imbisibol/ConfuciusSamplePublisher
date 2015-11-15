package com.confucius.sample.publisherdashboard;

/**
 * Created by imbisibol on 11/15/2015.
 */

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONObject;

/**
 * A placeholder fragment containing a simple view.
 */
public class PublisherAbout extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private PublisherTask mAuthTask = null;
    private ProgressBar mProgressBar;
    private View dvContent;
    private ImageView imgBannerURL;
    private ImageView imgLogo;
    private TextView lblPublisherName;
    private WebView wvAbout;

    private DATAPublisher PublisherData;

    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PublisherAbout newInstance(int sectionNumber) {
        PublisherAbout fragment = new PublisherAbout();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public PublisherAbout() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mProgressBar = (ProgressBar)rootView.findViewById(R.id.mProgressBar);
        dvContent =rootView.findViewById(R.id.dvContent);
        imgBannerURL = (ImageView)rootView.findViewById(R.id.imgBannerURL);
        imgLogo = (ImageView)rootView.findViewById(R.id.imgLogo);
        lblPublisherName = (TextView)rootView.findViewById(R.id.lblPublisherName);
        wvAbout = (WebView)rootView.findViewById(R.id.wvAbout);

        GetInstituteProfile();


        return rootView;
    }

    public void GetInstituteProfile() {

        if (mAuthTask != null) {
            return;
        }

        mAuthTask = new PublisherTask(getActivity().getString(R.string.tempPublisherId));
        mAuthTask.execute((Void) null);
    }

    public class PublisherTask extends AsyncTask<Void, Void, Boolean> {

        private final String UserId;

        PublisherTask(String userId) {
            UserId = userId;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            String userId = "";
            JSONObject jsonResponse = null;

            try {

                JSONObject jsonParam = new JSONObject();

                Common comm = new Common();
                comm.setAPIURL(getActivity().getString(R.string.ConfuciusSecurityAPIURL));
                jsonResponse = comm.GetAPI("/api/ProfileView/?userId=" + UserId
                        + "&appId=" + getActivity().getString(R.string.ApplicationId));

                if (jsonResponse != null) {

                    String strSuccess = jsonResponse.getString("Success");

                    if (strSuccess == "true") {

                        JSONObject responseData = jsonResponse.getJSONObject("ResponseData");

                        if (responseData != null) {

                            PublisherData = new DATAPublisher();

                            //DO SOMETHING
                            JSONObject profile = responseData.getJSONObject("UserProfileView");

                            PublisherData.UserId = profile.getString("UserId");
                            PublisherData.PublisherName = profile.getString("PublisherName");
                            PublisherData.AvatarURL = profile.getString("AvatarURL");
                            PublisherData.BannerImageURL = profile.getString("BannerURL");
                            PublisherData.BannerText = profile.getString("BannerText");
                            PublisherData.FBAccountURL = profile.getString("PublisherFB");
                            PublisherData.TWAccountURL = profile.getString("PublisherTW");
                            PublisherData.AboutHTMLContent = profile.getString("PublisherAboutContent");
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

            if (success && PublisherData != null) {

                lblPublisherName.setText(PublisherData.PublisherName);
                wvAbout.loadData("<div style='padding:5px'><p>" + PublisherData.BannerText + "</p><hr/>" + PublisherData.AboutHTMLContent + "</div>", "text/html", "utf-8");

                if (PublisherData != null && PublisherData.AvatarURL.equals(getString(R.string.DEFAULT_blank_institute)))
                    PublisherData.AvatarURL = getString(R.string.DEFAULT_image_domain) + PublisherData.AvatarURL;
                Common.getImageLoader(null).displayImage(PublisherData.AvatarURL, imgLogo);
                Common.getImageLoader(null).displayImage(PublisherData.BannerImageURL, imgBannerURL);

            } else {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity().getBaseContext());
                dialog.setTitle("Message Alert");
                dialog.setMessage("Failed tor retrieve Publisher Profile!");
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
