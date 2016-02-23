package com.masterthesis.personaldata.symptoms.intro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.github.paolorotolo.appintro.AppIntro2;
import com.masterthesis.personaldata.symptoms.MainActivity;
import com.masterthesis.personaldata.symptoms.R;

public class SecondLayoutIntro extends AppIntro2 {
    int currentSlide;
    @Override
    public void init(Bundle savedInstanceState) {
        currentSlide=0;
        addSlide(DiaryCreationSlide.newInstance(R.layout.intro_2));
        addSlide(DiaryCreationSlide.newInstance(R.layout.intro2_2));
        addSlide(DiaryCreationSlide.newInstance(R.layout.intro3_2));


    }

    private void loadMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDonePressed() {
        loadMainActivity();
    }

    @Override
    public void onNextPressed() {
        currentSlide++;
//        getSlides().get(currentSlide);
    }

    @Override
    public void onSlideChanged() {

    }

    public void getStarted(View v){
        loadMainActivity();
    }
}
