package com.masterthesis.personaldata.symptoms.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Toast;

import com.masterthesis.personaldata.symptoms.DAO.model.Diary;
import com.masterthesis.personaldata.symptoms.R;
import com.masterthesis.personaldata.symptoms.dragNdrop.CoolDragAndDropGridView;
import com.masterthesis.personaldata.symptoms.dragNdrop.Item;
import com.masterthesis.personaldata.symptoms.dragNdrop.ItemAdapter;
import com.masterthesis.personaldata.symptoms.dragNdrop.SimpleScrollingStrategy;
import com.masterthesis.personaldata.symptoms.dragNdrop.SpanVariableGridView;
import com.masterthesis.personaldata.symptoms.managers.DiaryManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
    ItemAdapter mItemAdapter;
    DiaryManager diaryManager;
    //    CoolDragAndDropGridView mCoolDragAndDropGridView;
    List<Item> mItems = new LinkedList<Item>();
    List<Diary> diaries=new ArrayList<>();
    @Bind(R.id.create_diary_button)
    Button createDiaryButton;
    @OnClick(R.id.create_diary_button) void createDiary(){

        Diary diary=diaryManager.createDiary("Pain diary", "Monitor distress of pain");
        diaries.add(diary);
        for (Diary d :diaries){
            mItems.add(new Item(R.drawable.ic_local_search_airport_highlighted,3,d.getName(),d.getDescription()));
        }
        mItemAdapter.notifyDataSetChanged();
    }
    @Bind(R.id.scrollView)
    ScrollView scrollView;
    @Bind(R.id.coolDragAndDropGridView)
    CoolDragAndDropGridView mCoolDragAndDropGridView;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        scrollView = (ScrollView) findViewById(R.id.scrollView);
//        mCoolDragAndDropGridView = (CoolDragAndDropGridView) findViewById(R.id.coolDragAndDropGridView);
        View view = inflater.inflate(R.layout.fragment_diary, container, false);
        ButterKnife.bind(this, view);
        diaryManager = DiaryManager.getInstance();
        diaryManager.init(getContext());
        try {
            diaries=diaryManager.getDiaries();
            for (Diary d :diaries){
                mItems.add(new Item(R.drawable.ic_local_search_airport_highlighted,3,d.getName(),d.getDescription()));
            }
//            mItemAdapter.notifyDataSetChanged();
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(getContext(),"Error fetching the diaries",Toast.LENGTH_LONG);
        }
//        mItems.add(new Item(R.drawable.ic_local_search_airport_highlighted, 3, "Airport", "Heathrow"));
//        mItems.add(new Item(R.drawable.ic_local_search_bar_highlighted, 3, "Bar", "Connaught Bar"));


        mItemAdapter = new ItemAdapter(getContext(), mItems);
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

            mItems.add(to, mItems.remove(from));
            mItemAdapter.notifyDataSetChanged();
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
