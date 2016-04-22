package com.masterthesis.personaldata.symptoms;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.masterthesis.personaldata.symptoms.DAO.model.Patient;
import com.masterthesis.personaldata.symptoms.DAO.model.Settings;
import com.masterthesis.personaldata.symptoms.fragments.DiaryFragment;
import com.masterthesis.personaldata.symptoms.fragments.SymptomFragment;
import com.masterthesis.personaldata.symptoms.managers.PatientManager;
import com.masterthesis.personaldata.symptoms.managers.SettingsManager;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import java.sql.Timestamp;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreatePatientActivity extends AppCompatActivity implements SymptomFragment.OnSymptomFragmentInteractionListener, DiaryFragment.OnDiaryFragmentListener {
    // 0 male,1 female
    static int GENDER = -1;
    @Bind(R.id.viewpagerCreatePatient)
    ViewPager viewPager;
    @Bind(R.id.viewpagertabCreatePatient)
    SmartTabLayout viewPagerTab;
    FragmentPagerItemAdapter adapter;
    @Bind(R.id.no_patient_layout)
    LinearLayout noPatientLayout;
    @Bind(R.id.save_patient_info)
    Button savePatientButton;
    @Bind(R.id.male)
    ImageButton maleButton;
    @Bind(R.id.female)
    ImageButton femaleButton;
    @Bind(R.id.name_input)
    EditText nameInputEditText;
    @Bind(R.id.add_diary_and_symptoms)
    RelativeLayout diarySymptomsLayout;

    @OnClick(R.id.male)
    void male() {
        GENDER = 0;
        maleButton.setBackgroundColor(ContextCompat.getColor(this, R.color.high));
        femaleButton.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));
        Log.i("gender","male"+GENDER);
    }

    @OnClick(R.id.female)
    void female() {
        GENDER = 1;
        maleButton.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));
        femaleButton.setBackgroundColor(ContextCompat.getColor(this, R.color.high));
    }

    @OnClick(R.id.save_patient_info)
    void savePatient() {
        if (nameInputEditText.getText().length() > 0) {
            Patient p = new Patient(new Timestamp(System.currentTimeMillis()));
            p.setPatientName(nameInputEditText.getText().toString());
            switch (GENDER) {
                case 0:
                    p.setGender("f");
                    break;
                case 1:
                    p.setGender("m");
                    break;
                default:
                    p.setGender("NA");
                    break;
            }
            PatientManager.getInstance().createNewProfile(p);
            noPatientLayout.setVisibility(View.GONE);
            SettingsManager.getInstance().createSettings(p);
            setupDiaryLayout();

        }
    }

    private void setupDiaryLayout() {
        diarySymptomsLayout.setVisibility(View.VISIBLE);
        FragmentPagerItems.Creator creator = FragmentPagerItems.with(this);
        creator.add("Diary", DiaryFragment.class);
        creator.add("Symptoms", SymptomFragment.class);
        adapter = new FragmentPagerItemAdapter(getSupportFragmentManager(), creator.create());
        viewPager.setAdapter(adapter);
        viewPagerTab.setViewPager(viewPager);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_patient);
        ButterKnife.bind(this);
    }

    @Override
    public void onDiaryFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
