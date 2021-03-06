package com.masterthesis.personaldata.symptoms.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.masterthesis.personaldata.symptoms.DAO.model.Diary;
import com.masterthesis.personaldata.symptoms.DAO.model.Symptom;
import com.masterthesis.personaldata.symptoms.R;
import com.masterthesis.personaldata.symptoms.dragNdrop.CoolDragAndDropGridView;
import com.masterthesis.personaldata.symptoms.dragNdrop.DiaryItem;
import com.masterthesis.personaldata.symptoms.dragNdrop.DiaryItemAdapter;
import com.masterthesis.personaldata.symptoms.dragNdrop.SimpleScrollingStrategy;
import com.masterthesis.personaldata.symptoms.dragNdrop.SpanVariableGridView;
import com.masterthesis.personaldata.symptoms.managers.DiaryManager;
import com.masterthesis.personaldata.symptoms.managers.SymptomManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DiaryFragment.OnDiaryFragmentListener} interface
 * to handle interaction events.
 * Use the {@link DiaryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiaryFragment extends Fragment implements CoolDragAndDropGridView.DragAndDropListener, SpanVariableGridView.OnItemClickListener,
        SpanVariableGridView.OnItemLongClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "DiaryFragment";
    public static List<Diary> diaries = new ArrayList<>();
    DiaryItemAdapter mItemAdapter;
    DiaryManager diaryManager;
    //    CoolDragAndDropGridView mCoolDragAndDropGridView;
    List<DiaryItem> mItems = new LinkedList<>();
    @Bind(R.id.create_diary_button)
    Button createDiaryButton;
    @Bind(R.id.scrollView)
    ScrollView scrollView;
    @Bind(R.id.coolDragAndDropGridViewDiary)
    CoolDragAndDropGridView mCoolDragAndDropGridView;

    @Bind(R.id.input_diary_name)
    EditText inputDiaryNameEditText;

    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
    private OnDiaryFragmentListener mListener;

    public DiaryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DiaryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DiaryFragment newInstance(String param1, String param2) {
        DiaryFragment fragment = new DiaryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @OnClick(R.id.create_diary_button)
    void createDiary() {

//        Intent intent = new Intent(getContext(), SecondLayoutIntro.class);
//        startActivity(intent);

        List<Diary> sameNameDiaries = null;
        String input = inputDiaryNameEditText.getText().toString();
        try {
            sameNameDiaries = DiaryManager.getInstance().searchByName(inputDiaryNameEditText.getText().toString());
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error searching the database for diary with the same name", Toast.LENGTH_LONG).show();

        }
        Log.i(TAG, "same name diaries " + sameNameDiaries);
        // If the input field is not empty continue
        if (input.isEmpty()) {
            Toast.makeText(getContext(), "Please add a name for the diary you want to create", Toast.LENGTH_LONG).show();
            return;
        }

        // Check if there is another diary with the same name.
        // If there is not create a new diary and update the list

        if (sameNameDiaries != null && !sameNameDiaries.isEmpty()) {
            Toast.makeText(getContext(), "A diary with the same name exists. Choose another name", Toast.LENGTH_LONG).show();
        } else {
            Diary diary = diaryManager.createDiary(input, "This is the description : Monitor something");
            diaries.add(diary);

            mItems.add(new DiaryItem(R.drawable.ic_local_search_airport_highlighted, 3, diary.getName(), diary.getDescription()));
            mItemAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        diaryManager = DiaryManager.getInstance();

        try {
            diaries = diaryManager.getDiaries();
            if (!diaries.isEmpty()) {
                for (Diary d : diaries) {
                    mItems.add(new DiaryItem(R.drawable.ic_local_search_airport_highlighted, 3, d.getName(), d.getDescription()));
                    Log.i(TAG, "name" + d.getName());
                    Log.i(TAG, "description" + d.getDescription());
//                    Log.i(TAG, "symptoms" + d.getSymptoms().toString());
//                    Log.i(TAG, "symptoms" + d.getSymptoms().size());
                    TreeMap<Integer, String> symTypes = d.getSymptomTypes();
                    Log.i(TAG, String.valueOf(symTypes));
                    HashMap<String, List<Symptom>> symptomsMap = SymptomManager.getInstance().getSymptomsByDiary(d);
                    for (List<Symptom> slist : symptomsMap.values()) {
                        for (Symptom s : slist) {
                            Log.i(TAG,"created"+ String.valueOf(s.getCreatedAt()));
                            Log.i(TAG,"updated" + String.valueOf(s.getUpdatedAt()));
                            Log.i(TAG,"context " + String.valueOf(s.getContext()));
                        }
                    }

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error fetching the diaries", Toast.LENGTH_LONG).show();
        }

//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_diary, container, false);
        ButterKnife.bind(this, view);

        mItemAdapter = new DiaryItemAdapter(getContext(), mItems);
        mCoolDragAndDropGridView.setAdapter(mItemAdapter);
        mCoolDragAndDropGridView.setScrollingStrategy(new SimpleScrollingStrategy(scrollView));
        mCoolDragAndDropGridView.setDragAndDropListener(this);
        mCoolDragAndDropGridView.setOnItemLongClickListener(this);
        mItemAdapter.notifyDataSetChanged();

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onDiaryFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDiaryFragmentListener) {
            mListener = (OnDiaryFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnDiaryFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

        mCoolDragAndDropGridView.startDragAndDrop();

        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

    }

    @Override
    public void onDragItem(int from) {

    }

    @Override
    public void onDraggingItem(int from, int to) {

    }

    @Override
    public void onDropItem(int from, int to) {
        if (from != to) {
            Log.i(TAG, "on drop from " + from + " to " + to);
            mItems.add(to, mItems.remove(from));
            mItemAdapter.notifyDataSetChanged();
        }
        if (to == 0) {
            Log.i(TAG, "on drop from " + diaries.get(from).getName() + " to " + diaries.get(to).getName());
            Collections.swap(diaries, to, from);
        }
    }

    @Override
    public boolean isDragAndDropEnabled(int position) {
        return true;
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
    public interface OnDiaryFragmentListener {
        // TODO: Update argument type and name
        void onDiaryFragmentInteraction(Uri uri);
    }
}
