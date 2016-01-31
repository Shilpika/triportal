package edu.purdue.cs;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * A login screen that offers login via OAuth providers.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private GoogleLoginTask mGoogleAuthTask = null;
    private FacebookLoginTask mFacebookAuthTask = null;

    // UI references.
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Setting up user events.
        Button mGoogleSignInButton = (Button) findViewById(R.id.google_sign_in_button);
        mGoogleSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptGoogleLogin();
            }
        });
        Button mFacebookSignInButton = (Button) findViewById(R.id.facebook_sign_in_button);
        mFacebookSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptFacebookLogin();
            }
        });

        // Getting view references.
        mProgressView = findViewById(R.id.login_progress);
        mLoginFormView = findViewById(R.id.login_form);
    }

    /**
     * Attempts to sign in with Google+.
     */
    private void attemptGoogleLogin() {
        if (mGoogleAuthTask != null) {
            return;
        }
        // Show a progress spinner, and kick off a background task to
        // perform the user login attempt.
        showProgress(true);
        mGoogleAuthTask = new GoogleLoginTask();
        mGoogleAuthTask.execute((Void) null);
    }

    /**
     * Attempts to sign in with Facebook.
     */
    private void attemptFacebookLogin() {
        if (mFacebookAuthTask != null) {
            return;
        }
        // Show a progress spinner, and kick off a background task to
        // perform the user login attempt.
        showProgress(true);
        mFacebookAuthTask = new FacebookLoginTask();
        mFacebookAuthTask.execute((Void) null);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Authenticate with Google+.
     */
    public class GoogleLoginTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mGoogleAuthTask = null;

            if (success) {
                // Direct user to the startup page.
                Intent intent = new Intent(LoginActivity.this, Startup.class);
                startActivity(intent);
                finish();
            } else {
                // Recover the login form
                showProgress(false);

                // Display some error messages to the user
                Log.e("Login", "Unknown error happened");
            }
        }

        @Override
        protected void onCancelled() {
            mGoogleAuthTask = null;
            showProgress(false);
        }
    }

    /**
     * Authenticate with Facebook.
     */
    public class FacebookLoginTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mFacebookAuthTask = null;

            if (success) {
                // Direct user to the startup page.
                Intent intent = new Intent(LoginActivity.this, Startup.class);
                startActivity(intent);
                finish();
            } else {
                // Recover the login form
                showProgress(false);

                // Display some error messages to the user
                Log.e("Login", "Unknown error happened");
            }
        }

        @Override
        protected void onCancelled() {
            mFacebookAuthTask = null;
            showProgress(false);
        }
    }
}

