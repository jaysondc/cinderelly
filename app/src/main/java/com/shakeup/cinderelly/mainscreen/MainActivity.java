package com.shakeup.cinderelly.mainscreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.shakeup.cinderelly.R;
import com.shakeup.cinderelly.adapters.TaskAdapter;
import com.shakeup.cinderelly.editscreen.EditItemActivity;
import com.shakeup.cinderelly.model.DbUtils;
import com.shakeup.cinderelly.model.Task;

import java.util.ArrayList;

import static com.shakeup.cinderelly.model.DbUtils.getTasks;

public class MainActivity extends AppCompatActivity {

    ArrayList<Task> mTasks;
    TaskAdapter mTaskAdapter;
    ListView mListViewItems;

    static final int EDIT_REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListViewItems = findViewById(R.id.list_to_do);

        // Read from database
        mTasks = getTasks();
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
     * Handle the results from called activities.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == EDIT_REQUEST_CODE) {
            // Extract name value from result extras
            String newText = data.getExtras().getString(getString(R.string.EXTRA_EDIT_RETURN_STRING));
            int newPosition = data.getExtras().getInt(getString(R.string.EXTRA_LIST_POSITION));

            replaceItem(newText, newPosition);
        }
    }

    /**
     * Adds the current contents of the EditText view to the mTaskAdapter
     *
     * @param view the view clicked
     */
    public void onAddItem(View view) {
        EditText editTextBox = findViewById(R.id.edit_text_new_item);
        String itemText = editTextBox.getText().toString();

        // Add item as low priority for now
        // TODO: Custom priority
        DbUtils.addTask(itemText, 1);

        // Update the array and notify the adapter
        mTaskAdapter.updateList(DbUtils.getTasks());

        editTextBox.setText("");
    }

    /**
     * Replaces an item in the list with specified contents
     *
     * @param itemText     Updated text to set
     * @param itemPosition Position to set new text
     */
    private void replaceItem(String itemText, int itemPosition) {
//        mTasks.set(itemPosition, itemText);
//        mTaskAdapter.notifyDataSetChanged();
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

                        DbUtils.deleteTask(task.id);

                        mTaskAdapter.updateList(DbUtils.getTasks());

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

                        // Get the item text
                        TextView textView = view.findViewById(R.id.text_item);
                        String text = textView.getText().toString();

                        // Bundle extras
                        Intent intent = new Intent(getApplicationContext(), EditItemActivity.class);
                        intent.putExtra(getResources().getString(R.string.EXTRA_EDIT_STRING), text);
                        intent.putExtra(getResources().getString(R.string.EXTRA_LIST_POSITION), position);

                        // Start intent
                        startActivityForResult(intent, EDIT_REQUEST_CODE);
                    }
                }
        );
    }

}
