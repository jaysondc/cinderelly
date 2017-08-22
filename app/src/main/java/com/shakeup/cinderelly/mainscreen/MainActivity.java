package com.shakeup.cinderelly.mainscreen;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.shakeup.cinderelly.R;
import com.shakeup.cinderelly.adapters.TaskAdapter;
import com.shakeup.cinderelly.editscreen.EditDialogCallback;
import com.shakeup.cinderelly.editscreen.EditDialogFragment;
import com.shakeup.cinderelly.model.DbUtils;
import com.shakeup.cinderelly.model.Task;

import java.util.ArrayList;

import static com.shakeup.cinderelly.model.DbUtils.getAllTasks;

public class MainActivity extends AppCompatActivity
        implements EditDialogCallback {

    ArrayList<Task> mTasks;
    TaskAdapter mTaskAdapter;
    ListView mListViewItems;

    public static final String EDIT_DIALOG_TAG = "edit_dialog_tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListViewItems = findViewById(R.id.list_to_do);

        // Read from database
        mTasks = getAllTasks();
        mTaskAdapter = new TaskAdapter(
                this,
                mTasks);
        mListViewItems.setAdapter(mTaskAdapter);

        // Set up long click to delete
        setupListLongClickListener();
        // Set up click to edit
        setupListClickListener();
    }

    /**
     * Adds the current contents of the EditText view to the mTaskAdapter
     *
     * @param view the view clicked
     */
    public void onAddItem(View view) {
        android.support.v4.app.FragmentTransaction ft =
                getSupportFragmentManager().beginTransaction();

        // Create and show the dialog.
        DialogFragment newFragment = EditDialogFragment.newInstance();
        newFragment.show(ft, EDIT_DIALOG_TAG);
    }

    /**
     * Setup the ListView OnItemLongClickListener to delete the item that was long clicked.
     */
    private void setupListLongClickListener() {
        mListViewItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView,
                                                   View view,
                                                   int position,
                                                   long id) {
                        // Delete the task from the database
                        Task task = (Task) mTaskAdapter.getItem(position);
                        DbUtils.deleteTask(task);
                        refreshTaskList();

                        return true;
                    }
                }
        );
    }

    /**
     * Setup the ListView OnItemClickListener to edit the item that was clicked.
     */
    private void setupListClickListener() {
        mListViewItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView,
                                            View view,
                                            int position,
                                            long id) {
                        Task task = (Task) mTaskAdapter.getItem(position);

                        android.support.v4.app.FragmentTransaction ft =
                                getSupportFragmentManager().beginTransaction();

                        Bundle args = new Bundle();
                        args.putInt(getResources().getString(R.string.EXTRA_TASK_ID), task.id);
                        // Create and show the dialog.
                        DialogFragment newFragment = EditDialogFragment.newInstance();
                        newFragment.setArguments(args);
                        newFragment.show(ft, EDIT_DIALOG_TAG);
                    }
                }
        );
    }

    private void refreshTaskList() {
        // Update the array and notify the adapter
        mTaskAdapter.updateList(DbUtils.getAllTasks());
    }


    /**
     * This is called by our edit dialog once the user saves edits
     */
    @Override
    public void onEditDialogResult() {
        refreshTaskList();
    }
}
