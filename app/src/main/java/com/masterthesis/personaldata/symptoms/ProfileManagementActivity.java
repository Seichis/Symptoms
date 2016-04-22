package com.masterthesis.personaldata.symptoms;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.masterthesis.personaldata.symptoms.welcomewizard.animators.ChatAvatarsAnimator;
import com.masterthesis.personaldata.symptoms.welcomewizard.animators.InSyncAnimator;
import com.masterthesis.personaldata.symptoms.welcomewizard.animators.RocketAvatarsAnimator;
import com.masterthesis.personaldata.symptoms.welcomewizard.animators.RocketFlightAwayAnimator;
import com.redbooth.WelcomeCoordinatorLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ProfileManagementActivity extends AppCompatActivity {

    @Bind(R.id.coordinator_profile)
    WelcomeCoordinatorLayout coordinatorLayout;
    private boolean animationReady = false;
    private ValueAnimator backgroundAnimator;
//    private RocketAvatarsAnimator rocketAvatarsAnimator;
//    private ChatAvatarsAnimator chatAvatarsAnimator;
//    private RocketFlightAwayAnimator rocketFlightAwayAnimator;
//    private InSyncAnimator inSyncAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_management);
        ButterKnife.bind(this);
        initializeListeners();
        initializePages();
        initializeBackgroundTransitions();
    }

    private void initializePages() {
        final WelcomeCoordinatorLayout coordinatorLayout
                = (WelcomeCoordinatorLayout) findViewById(R.id.coordinator_profile);
        coordinatorLayout.addPage(R.layout.profile_setup,
                R.layout.fragment_diary,
                R.layout.fragment_symptom);
    }

    private void initializeListeners() {
        coordinatorLayout.setOnPageScrollListener(new WelcomeCoordinatorLayout.OnPageScrollListener() {
            @Override
            public void onScrollPage(View v, float progress, float maximum) {
                if (!animationReady) {
                    animationReady = true;
                    backgroundAnimator.setDuration((long) maximum);
                }
                backgroundAnimator.setCurrentPlayTime((long) progress);
            }

            @Override
            public void onPageSelected(View v, int pageSelected) {
                switch (pageSelected) {
                    case 0:
//                        if (rocketAvatarsAnimator == null) {
//                            rocketAvatarsAnimator = new RocketAvatarsAnimator(coordinatorLayout);
//                            rocketAvatarsAnimator.play();
//                        }
                        break;
                    case 1:
//                        if (chatAvatarsAnimator == null) {
//                            chatAvatarsAnimator = new ChatAvatarsAnimator(coordinatorLayout);
//                            chatAvatarsAnimator.play();
//                        }
                        break;
                    case 2:
//                        if (inSyncAnimator == null) {
//                            inSyncAnimator = new InSyncAnimator(coordinatorLayout);
//                            inSyncAnimator.play();
//                        }
                        break;
                    case 3:
//                        if (rocketFlightAwayAnimator == null) {
//                            rocketFlightAwayAnimator = new RocketFlightAwayAnimator(coordinatorLayout);
//                            rocketFlightAwayAnimator.play();
//                        }
                        break;
                }
            }
        });
    }

    private void initializeBackgroundTransitions() {
        final Resources resources = getResources();
        final int colorPage1 = ResourcesCompat.getColor(resources, R.color.page1, getTheme());
        final int colorPage2 = ResourcesCompat.getColor(resources, R.color.page2, getTheme());
        final int colorPage3 = ResourcesCompat.getColor(resources, R.color.page3, getTheme());
        final int colorPage4 = ResourcesCompat.getColor(resources, R.color.page4, getTheme());
        backgroundAnimator = ValueAnimator
                .ofObject(new ArgbEvaluator(), colorPage1, colorPage2, colorPage3, colorPage4);
        backgroundAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                coordinatorLayout.setBackgroundColor((int) animation.getAnimatedValue());
            }
        });
    }

//    @OnClick(R.id.skip)
//    void skip() {
//        coordinatorLayout.setCurrentPage(coordinatorLayout.getNumOfPages() - 1, true);
//    }
}


