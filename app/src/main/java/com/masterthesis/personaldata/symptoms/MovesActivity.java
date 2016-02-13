package com.masterthesis.personaldata.symptoms;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.masterthesis.personaldata.symptoms.managers.MovesManager;
import com.midhunarmid.movesapi.auth.AuthData;

public class MovesActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "Moves";
    private MovesManager movesManager = MovesManager.getInstance();
    private Spinner mSpinnerAPI;
    private Button mButtonSubmit;
    private ProgressBar mProgressRequest;
    private TextView mTvResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moves);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mSpinnerAPI = (Spinner) findViewById(R.id.spinnerAPI);
        mButtonSubmit = (Button) findViewById(R.id.buttonSubmit);
        mTvResponse = (TextView) findViewById(R.id.tvResponse);
        mProgressRequest = (ProgressBar) findViewById(R.id.progressRequest);

        mButtonSubmit.setOnClickListener(this);

        movesManager.init(this);

    }

    @Override
    public void onClick(View v) {
//        toggleProgress(true);
        switch (mSpinnerAPI.getSelectedItemPosition()) {
            case 0: // Authenticate
//                MovesAPI.authenticate(movesManager.getAuthDialogHandler(), MovesActivity.this);
                movesManager.authenticate(MovesActivity.this);
                break;
            case 1: // Get Auth Data
                AuthData auth = movesManager.getAuthData();
                if (auth != null) {
                    updateResponse("Access Token : " + auth.getAccessToken() + "\n"
                            + "Expires In : " + auth.getExpiresIn() + "\n"
                            + "User ID : " + auth.getUserID());
                } else {
                    updateResponse("Not Authenticated Yet!");
                }
//                toggleProgress(false);
                break;
            case 2: // Get Profile
                movesManager.profile();
                break;
            case 4: // Get Summary Day
                movesManager.summarySingleDay("20160212");
                break;
            case 5: // Get Summary Week
                movesManager.summaryWeek("2016-W06");
                break;
            case 6: // Get Summary Month
                movesManager.summaryMonth("201602");
                break;
            case 7: // Get Summary Range
                movesManager.summaryRange("20160211", "20160212");
                break;
            case 8: // Get Summary PastDays
                movesManager.summaryPastDays("31");
                break;
            case 10: // Get Storyline Day
                movesManager.storylineDay("20160212",true);
                break;
            case 11: // Get Storyline Week
                movesManager.storylineWeek("2016-W06",true);
                break;
            case 12: // Get Storyline Month
                movesManager.storylineMonth("201602");
                break;
            case 13: // Get Storyline Range
                movesManager.storylineRange("20160211", "20160212",true);
                break;
            case 14: // Get Storyline PastDays
                movesManager.storylinePastDays("7",true);
                break;
            case 16: // Get Activities Day
                movesManager.activitiesDay("20160212");
                break;
            case 17: // Get Activities Week
                movesManager.activitiesWeek("2016-W06");
                break;
            case 18: // Get Activities Month
                movesManager.activitiesMonth("201602");
                break;
            case 19: // Get Activities Range
                movesManager.activitiesRange("20160211", "20160212");
                break;
            case 20: // Get Activities PastDays
                movesManager.activitiesPastDays("31");
                break;
            default:
                break;
        }
    }

//    public void toggleProgress(final boolean isProgressing) {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                mProgressRequest.setVisibility(isProgressing ? View.VISIBLE : View.GONE);
//                mButtonSubmit.setVisibility(isProgressing ? View.GONE : View.VISIBLE);
//            }
//        });
//    }

    public void updateResponse(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTvResponse.setText(String.valueOf(message));
            }
        });
    }
}