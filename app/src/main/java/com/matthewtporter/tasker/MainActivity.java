package com.matthewtporter.tasker;

import android.app.Activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.matthewtporter.tasker.fragments.CompletedTasksFragment;
import com.matthewtporter.tasker.fragments.PlaceholderFragment;
import com.matthewtporter.tasker.fragments.TasksFragment;
import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private Menu mMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set up parse
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "t1TXWPsgYz7tYCeVpQQt0xRwB9ecAFl1MoSosj31", "xEJXQZfu35BalSoPGahiMEbi8ZTnbhUcRtzfkSqy");

        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        switch (position) {
            case 0:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, TasksFragment.newInstance(position + 1))
                        .commit();
                break;
            case 1:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, CompletedTasksFragment.newInstance(position + 1))
                        .commit();
                break;
        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_example) {
//            Toast.makeText(this, "Make a new task!", Toast.LENGTH_SHORT).show();
//            return true;
//        }

        switch(id) {
            case R.id.action_example:
                Toast.makeText(this, "Make a new task!", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_mark_completed:
                Toast.makeText(this, "Marked as completed", Toast.LENGTH_SHORT).show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showMenuItem(int position) {
        mMenu.getItem(position).setVisible(true);
    }

    public void hideMenuItem(int position) {
        mMenu.getItem(position).setVisible(false);
    }

//    /**
//     * A placeholder fragment containing a simple view.
//     */
//    public static class PlaceholderFragment extends Fragment {
//
//
//        List<ParseObject> mOpenTasksObjectList;
//        List<String> mTaskNameList;
//        ListView mListView;
//        View rootView;
//
//
//
//        /**
//         * The fragment argument representing the section number for this
//         * fragment.
//         */
//        private static final String ARG_SECTION_NUMBER = "section_number";
//
//        /**
//         * Returns a new instance of this fragment for the given section
//         * number.
//         */
//        public static PlaceholderFragment newInstance(int sectionNumber) {
//            PlaceholderFragment fragment = new PlaceholderFragment();
//            Bundle args = new Bundle();
//            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//            fragment.setArguments(args);
//            return fragment;
//        }
//
//        public PlaceholderFragment() {
//        }
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                                 Bundle savedInstanceState) {
//
//            rootView = inflater.inflate(R.layout.fragment_main, container, false);
//
//            mTaskNameList = new ArrayList<>();
//
//            ParseQuery<ParseObject> query = ParseQuery.getQuery("OpenTasks");
//            query.findInBackground(new FindCallback<ParseObject>() {
//                @Override
//                public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
//                    if (e == null) {
//                        Log.d("task", "Retrieved " + parseObjects.size() + " tasks");
//                        mOpenTasksObjectList = parseObjects;
//                        for (ParseObject parseObject : parseObjects) {
//                            String tmpName = parseObject.getString("taskName");
//                            mTaskNameList.add(tmpName);
//                            Log.d("task name", tmpName);
//                        }
//                        inflateListView();
//                    } else {
//                        Log.d("task", "Error: " + e.getMessage());
//                    }
//                }
//            });
//
//            return rootView;
//
//        }
//
//        public void inflateListView() {
//
//            mListView = (ListView) rootView.findViewById(R.id.tasks_list_view);
//
//            TaskListAdapter mTaskListAdapter = new TaskListAdapter(getActivity(), mOpenTasksObjectList);
//
//            mListView.setAdapter(mTaskListAdapter);
//            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                    CheckBox mCheckBox = (CheckBox) view.findViewById(R.id.checkbox);
//                    mCheckBox.setChecked(!mCheckBox.isChecked());
//                    ((MainActivity) getActivity()).hideMenuItem(0);
//                    for(int i=0; i<mOpenTasksObjectList.size(); i++) {
//                        CheckBox tmpChecker = (CheckBox) mListView.getChildAt(i).findViewById(R.id.checkbox);
//                        Log.i("checker", tmpChecker.isChecked() + "");
//                        if(tmpChecker.isChecked()) {
//                            ((MainActivity) getActivity()).showMenuItem(0);
//                        }
//                    }
//
//                }
//            });
//
//        }
//
//
//        @Override
//        public void onAttach(Activity activity) {
//            super.onAttach(activity);
//            ((MainActivity) activity).onSectionAttached(
//                    getArguments().getInt(ARG_SECTION_NUMBER));
//        }
//    }

}
