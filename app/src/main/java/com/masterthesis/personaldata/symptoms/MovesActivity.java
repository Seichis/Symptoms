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

import com.masterthesis.personaldata.symptoms.managers.DataManager;
import com.masterthesis.personaldata.symptoms.managers.DataManager;
import com.midhunarmid.movesapi.auth.AuthData;

public class MovesActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "Moves";
    private DataManager dataManager = DataManager.getInstance();
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

//        dataManager.init(this);

    }

    @Override
    public void onClick(View v) {
//        toggleProgress(true);
        switch (mSpinnerAPI.getSelectedItemPosition()) {
            case 0: // Authenticate
//                MovesAPI.authenticate(dataManager.getAuthDialogHandler(), MovesActivity.this);
                dataManager.movesAuthenticate(MovesActivity.this);
                break;
            case 1: // Get Auth Data
                AuthData auth = dataManager.getMovesAuthData();
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
                dataManager.movesProfile();
                break;
            case 4: // Get Summary Day
//                dataManager.movesSummarySingleDay("20160214");
                dataManager.movesSummaryToday();
                break;
            case 5: // Get Summary Week
                dataManager.movesSummaryWeek("2016-W06");
                break;
            case 6: // Get Summary Month
                dataManager.movesSummaryMonth("201602");
                break;
            case 7: // Get Summary Range
                dataManager.movesSummaryRange("20160211", "20160212");
                break;
            case 8: // Get Summary PastDays
                dataManager.movesSummaryPastDays("31");
                break;
            case 10: // Get Storyline Day
                dataManager.movesStorylineToday(true);
//                dataManager.movesStorylineDay("20160214", true);
                break;
            case 11: // Get Storyline Week
                dataManager.movesStorylineWeek("2016-W06", true);
                break;
            case 12: // Get Storyline Month
                dataManager.movesStorylineMonth("201602");
                break;
            case 13: // Get Storyline Range
                dataManager.movesStorylineRange("20160211", "20160212", true);
                break;
            case 14: // Get Storyline PastDays
                dataManager.movesStorylinePastDays("7", true);
                break;
            case 16: // Get Activities Day
//                dataManager.movesActivitiesDay("20160214");
                dataManager.movesActivitiesToday();
                break;
            case 17: // Get Activities Week
                dataManager.movesActivitiesWeek("2016-W06");
                break;
            case 18: // Get Activities Month
                dataManager.movesActivitiesMonth("201602");
                break;
            case 19: // Get Activities Range
                dataManager.movesActivitiesRange("20160211", "20160212");
                break;
            case 20: // Get Activities PastDays
                dataManager.movesActivitiesPastDays("31");
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