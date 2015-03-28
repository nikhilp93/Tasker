package com.matthewtporter.tasker.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import com.afollestad.materialdialogs.MaterialDialog;
import com.matthewtporter.tasker.MainActivity;
import com.matthewtporter.tasker.R;
import com.matthewtporter.tasker.TaskListAdapter;
import com.parse.*;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class TasksFragment extends Fragment {


    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    List<ParseObject> mOpenTasksObjectList;
    List<String> mTaskNameList;
    ListView mListView;
    View rootView;
    TaskListAdapter mTaskListAdapter;
    ParseQuery<ParseObject> query = ParseQuery.getQuery("OpenTasks");
    private int numberChecked = 0;


    public TasksFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static TasksFragment newInstance(int sectionNumber) {
        TasksFragment fragment = new TasksFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mTaskNameList = new ArrayList<>();

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


    public void completeTask() {

        if(mOpenTasksObjectList==null) return;

        final AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
        dialog.setTitle("Marking Tasks Complete");
        dialog.setMessage("Hold on while we mark your task complete");
        dialog.getWindow().addFlags(LayoutParams.FLAG_NOT_TOUCHABLE | LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.show();

        int localChecked = 0;
        for(int i = 0; i < mOpenTasksObjectList.size(); i++) {
            CheckBox tmpChecker = (CheckBox) mListView.getChildAt(i).findViewById(R.id.checkbox);
            if (tmpChecker.isChecked()) {
                localChecked++;
                final int lastChecked = localChecked;
                final int someNumber = i;

                ParseObject copyObject = new ParseObject("CompletedTasks");
                copyObject.put("taskName", mOpenTasksObjectList.get(i).getString("taskName"));
                copyObject.put("taskDescription", mOpenTasksObjectList.get(i).getString("taskDescription"));

                copyObject.saveEventually(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e != null) return;
                        ParseObject.createWithoutData("OpenTasks", mOpenTasksObjectList.get(someNumber).getObjectId()).deleteEventually(new DeleteCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    mOpenTasksObjectList.remove(someNumber);
                                    mTaskListAdapter.notifyDataSetChanged();
                                    if (lastChecked == getNuberChecked()) {
                                        uncheckAll();
                                        checkForCheckedItems();
                                        dialog.dismiss();
                                    }
                                } else {
                                    Log.i("ERROR", "error!");
                                }
                            }
                        });
                    }
                });
            }
        }
    }


    public void deleteTask() {

    }


    public void checkForCheckedItems() {
        ((MainActivity) getActivity()).hideMenuItem(0);
        ((MainActivity) getActivity()).hideMenuItem(1);
        if(mOpenTasksObjectList==null) return;
        for (int i = 0; i < mOpenTasksObjectList.size(); i++) {
            CheckBox tmpChecker = (CheckBox) mListView.getChildAt(i).findViewById(R.id.checkbox);
            if (tmpChecker.isChecked()) {
                ((MainActivity) getActivity()).showMenuItem(0);
                ((MainActivity) getActivity()).showMenuItem(1);
            }
        }
    }

    public void inflateListView() {

        mListView = (ListView) rootView.findViewById(R.id.tasks_list_view);

        mTaskListAdapter = new TaskListAdapter(getActivity(), mOpenTasksObjectList);

        mListView.setAdapter(mTaskListAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CheckBox mCheckBox = (CheckBox) view.findViewById(R.id.checkbox);
                if(mCheckBox.isChecked()) {
                    mCheckBox.setChecked(false);
                    decrementChecked();
                } else {
                    mCheckBox.setChecked(true);
                    incrementChecked();
                }

                checkForCheckedItems();

            }
        });

    }

    public void incrementChecked() {
        numberChecked++;
        Log.i("Task", getNuberChecked() + "");
    }

    public void decrementChecked() {
        numberChecked--;
        Log.i("Task", getNuberChecked() + "");
    }

    public int getNuberChecked() {
        return numberChecked;
    }

    public void uncheckAll() {
        if(mOpenTasksObjectList==null) return;
        for (int i = 0; i < mOpenTasksObjectList.size(); i++) {
            CheckBox tmpChecker = (CheckBox) mListView.getChildAt(i).findViewById(R.id.checkbox);
            tmpChecker.setChecked(false);
            checkForCheckedItems();
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }
}
