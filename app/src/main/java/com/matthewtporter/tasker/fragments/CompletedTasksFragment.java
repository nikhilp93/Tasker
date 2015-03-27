package com.matthewtporter.tasker.fragments;

/**
 * Created by mporter on 3/27/15.
 */

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import com.matthewtporter.tasker.MainActivity;
import com.matthewtporter.tasker.R;
import com.matthewtporter.tasker.TaskListAdapter;
import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class CompletedTasksFragment extends Fragment {

    List<ParseObject> mOpenTasksObjectList;
    List<String> mTaskNameList;
    ListView mListView;
    View rootView;



    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static CompletedTasksFragment newInstance(int sectionNumber) {
        CompletedTasksFragment fragment = new CompletedTasksFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public CompletedTasksFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mTaskNameList = new ArrayList<>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("CompletedTasks");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
                if (e == null) {
                    Log.d("task", "Retrieved " + parseObjects.size() + " tasks");
                    mOpenTasksObjectList = parseObjects;
                    for (ParseObject parseObject : parseObjects) {
                        String tmpName = parseObject.getString("taskName");
                        mTaskNameList.add(tmpName);
                        Log.d("task name", tmpName);
                    }
                    inflateListView();
                } else {
                    Log.d("task", "Error: " + e.getMessage());
                }
            }
        });

        return rootView;

    }

    public void inflateListView() {

        mListView = (ListView) rootView.findViewById(R.id.tasks_list_view);

        TaskListAdapter mTaskListAdapter = new TaskListAdapter(getActivity(), mOpenTasksObjectList);

        mListView.setAdapter(mTaskListAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CheckBox mCheckBox = (CheckBox) view.findViewById(R.id.checkbox);
                mCheckBox.setChecked(!mCheckBox.isChecked());
                ((MainActivity) getActivity()).hideMenuItem(0);
                for(int i=0; i<mOpenTasksObjectList.size(); i++) {
                    CheckBox tmpChecker = (CheckBox) mListView.getChildAt(i).findViewById(R.id.checkbox);
                    Log.i("checker", tmpChecker.isChecked() + "");
                    if(tmpChecker.isChecked()) {
                        ((MainActivity) getActivity()).showMenuItem(0);
                    }
                }

            }
        });

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }
}

