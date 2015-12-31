package de.weightlifting.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseCrashReporting;

import de.weightlifting.app.helper.UiHelper;

public class SplashActivity extends Activity {

    // Splash screen timer
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_STATUS = "status";
    public static final String MESSAGE_INITIALIZED = "initialized";
    public static final int STATUS_OK = 0;
    public static final int STATUS_ERROR_NETWORK = 10;

    WeightliftingApp app;
    Handler callbackHandler;

    TextView status;
    ImageView iconLogo;
    ImageView iconError;

    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        app = (WeightliftingApp) getApplicationContext();

        iconLogo = (ImageView) findViewById(R.id.splash_logo);
        iconError = (ImageView) findViewById(R.id.splash_error);
        iconError.setVisibility(View.GONE);

        status = (TextView) findViewById(R.id.splash_text);
        status.setAlpha(0.0f);

        callbackHandler = new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {
                //Log.d(WeightliftingApp.TAG, "splashCallBackHandler received a message");
                try {
                    Bundle data = msg.getData();

                    // Get status
                    int callbackStatus = data.getInt(KEY_STATUS);
                    switch (callbackStatus) {
                        case SplashActivity.STATUS_OK: {
                            //Log.d(WeightliftingApp.TAG, "Status: OK");
                            setStatusText("");
                            break;
                        }
                        case SplashActivity.STATUS_ERROR_NETWORK: {
                            //Log.d(WeightliftingApp.TAG, "Status: Network error");
                            showError(getString(R.string.error_network));
                            break;
                        }
                    }

                    // Get message
                    String message = data.getString(KEY_MESSAGE);
                    if (message != null) {
                        if (message.equals(MESSAGE_INITIALIZED)) {
                            // Initialization done, start the app
                            initializationDone();
                        } else {
                            // Show status message in UI
                            status.setText(message);
                            status.invalidate();
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        if (!app.initializedParse) {
            Parse.enableLocalDatastore(this);
            ParseCrashReporting.enable(this);
            app.initializedParse = true;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            // Makes sure that the splash screen is visible to the user
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadData();
                }
            }, 50);
        }
    }

    private void loadData() {
        // Loading indicator
        startLoading();

        // initialize the app
        app.initialize(callbackHandler);
    }

    private void switchToMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        if (getIntent().getExtras() != null)
            i.putExtras(getIntent().getExtras());
        startActivity(i);

        // Close this splash screen
        finish();
    }

    private void initializationDone() {
        switchToMainActivity();
    }

    private void showError(String errorMessage) {
        status.setText(errorMessage);

        iconLogo.setVisibility(View.INVISIBLE);
        iconError.setVisibility(View.VISIBLE);

        stopLoading();
        initializationDone();
    }

    private void setStatusText(final String text) {
        Runnable fadeRunnable = new Runnable() {
            @Override
            public void run() {
                UiHelper.fadeInView(status, 0.01f);
                status.setText(text);
            }
        };
        runOnUiThread(fadeRunnable);
    }

    private void startLoading() {
        animation = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(1000);
        animation.setFillAfter(true);
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.INFINITE);
        animation.setInterpolator(new LinearInterpolator());
        iconLogo.startAnimation(animation);
    }

    private void stopLoading() {
        iconLogo.clearAnimation();
    }

}