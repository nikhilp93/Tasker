package com.matthewtporter.tasker;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.matthewtporter.tasker.fragments.CompletedTasksFragment;
import com.matthewtporter.tasker.fragments.TasksFragment;
import com.parse.Parse;
import com.parse.ParseObject;

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
    private int currentPostion;
    private List<ParseObject> taskList;
    private List<ParseObject> completedTaskList;

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
        currentPostion = position;
        switch (position) {
            case 0:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, TasksFragment.newInstance(position + 1), "task_fragment")
                        .commit();
                break;
            case 1:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, CompletedTasksFragment.newInstance(position + 1), "completed_task_fragment")
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
        FragmentManager fm = getFragmentManager();
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            if (currentPostion == 0) {
                TasksFragment tFrag = (TasksFragment) fm.findFragmentByTag("task_fragment");
                tFrag.checkForCheckedItems();
            } else {
                CompletedTasksFragment cFrag = (CompletedTasksFragment) fm.findFragmentByTag("completed_task_fragment");
                cFrag.checkForCheckedItems();
            }
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

        switch (id) {
            case R.id.action_example:
                Toast.makeText(this, "Make a new task!", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_mark_completed:
//                Toast.makeText(this, "Marked as completed", Toast.LENGTH_SHORT).show();
                menuClickComplete();
                return true;
            case R.id.action_delete:
                menuClickDelete();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void menuClickDelete() {
        FragmentManager fm = getFragmentManager();
        switch(currentPostion) {
            case 0:
                TasksFragment tFrag = (TasksFragment) fm.findFragmentByTag("task_fragment");
                tFrag.deleteTask();
                break;
            case 1:
                CompletedTasksFragment cFrag = (CompletedTasksFragment) fm.findFragmentByTag("completed_task_fragment");
                break;
        }
    }

    public void menuClickComplete() {
        FragmentManager fm = getFragmentManager();
        switch(currentPostion) {
            case 0:
                TasksFragment tFrag = (TasksFragment) fm.findFragmentByTag("task_fragment");
                tFrag.completeTask();
                break;
            case 1:
                CompletedTasksFragment cFrag = (CompletedTasksFragment) fm.findFragmentByTag("completed_task_fragment");
                break;
        }
    }


    public void showMenuItem(int position) {
        mMenu.getItem(position).setVisible(true);
    }

    public void hideMenuItem(int position) {
        mMenu.getItem(position).setVisible(false);
    }

}
