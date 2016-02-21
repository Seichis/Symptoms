package com.masterthesis.personaldata.symptoms.fragments;

import android.content.Context;
import android.database.SQLException;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.masterthesis.personaldata.symptoms.DAO.model.Symptom;
import com.masterthesis.personaldata.symptoms.R;
import com.masterthesis.personaldata.symptoms.managers.DiaryManager;
import com.masterthesis.personaldata.symptoms.managers.SymptomManager;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SymptomFragment.OnSymptomFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SymptomFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SymptomFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "SymptomFragment";
    List<Symptom> symptoms;
    @Bind(R.id.input_symptom_type)
    EditText symptomEditText;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnSymptomFragmentInteractionListener mListener;

    public SymptomFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SymptomFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SymptomFragment newInstance(String param1, String param2) {
        SymptomFragment fragment = new SymptomFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @OnClick(R.id.add_symptom_button)
    void addSymptom() {
        DiaryManager diaryManager = DiaryManager.getInstance();
        if (symptomEditText.getText().toString().equals("")) {
            Toast.makeText(getContext(), "Please add a symptom type", Toast.LENGTH_LONG).show();
        } else {
            //TODO catch no diary exception
            if (diaryManager.addSymptomType(diaryManager.getActiveDiary(), symptomEditText.getText().toString())) {
                Toast.makeText(getContext(), "Symptom created in the diary : " + diaryManager.getActiveDiary().getName(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), "Something went wrong while creating the symptom type in the diary " + diaryManager.getActiveDiary().getName(), Toast.LENGTH_LONG).show();

            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        try {
            symptoms = SymptomManager.getInstance().getAllSymptoms();

        } catch (SQLException e) {
            throw new RuntimeException("Could not lookup Think in the database", e);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        } finally {
            for (Symptom s : symptoms) {
                Log.i(TAG, "context" + s.getContext());
                Log.i(TAG, "symptom type" + s.getSymptomType());
                Log.i(TAG, "diary id" + s.getDiary().getId());
                Log.i(TAG, "diary name" + s.getDiary().getName());
                Log.i(TAG, "diary description" + s.getDiary().getDescription());
                Log.i(TAG, "symptom Id" + s.getId());
                Log.i(TAG, "timestamp" + s.getCreatedAt());
                Log.i(TAG, "timestamp" + s.getUpdatedAt());
                Log.i(TAG, "intensity" + s.getIntensity());
//            Log.i(TAG,""+s.());
//            Log.i(TAG,""+s.());

            }

        }
        View view = inflater.inflate(R.layout.fragment_symptom, container, false);
        ButterKnife.bind(this, view);


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSymptomFragmentInteractionListener) {
            mListener = (OnSymptomFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnSymptomFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
