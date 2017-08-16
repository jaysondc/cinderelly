package com.shakeup.cinderelly;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.shakeup.cinderelly.editscreen.EditItemActivity;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static android.R.attr.name;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;

    static final int EDIT_REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvItems = (ListView) findViewById(R.id.list_to_do);

        // Read from file
        readItems();

        itemsAdapter = new ArrayAdapter<>(
                this,
                R.layout.listitem_todo,
                items);
        lvItems.setAdapter(itemsAdapter);

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
     * Replaces an item in the list with specified contents
     *
     * @param itemText     Updated text to set
     * @param itemPosition Position to set new text
     */
    private void replaceItem(String itemText, int itemPosition) {
        items.set(itemPosition, itemText);
        itemsAdapter.notifyDataSetChanged();

        // Save changes
        writeItems();
    }

    /**
     * Adds the current contents of the EditText view to the itemsAdapter
     *
     * @param view the view clicked
     */
    public void onAddItem(View view) {
        EditText etNewItem = (EditText) findViewById(R.id.edit_text_new_item);
        String itemText = etNewItem.getText().toString();
        itemsAdapter.add(itemText);
        etNewItem.setText("");

        // Save changes
        writeItems();
    }

    /**
     * Setup the ListView OnItemLongClickListener to delete the item that was long clicked.
     */
    private void setupListLongClickListener() {
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView,
                                                   View view,
                                                   int position,
                                                   long id) {
                        items.remove(position);
                        itemsAdapter.notifyDataSetChanged();

                        // Save changes
                        writeItems();
                        return true;
                    }
                }
        );
    }

    /**
     * Setup the ListView OnItemClickListener to edit the item that was clicked.
     */
    private void setupListClickListener() {
        lvItems.setOnItemClickListener(
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

    /**
     * Attempt to read the existing list from a file. If that fails, create a blank list.
     */
    private void readItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");

        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            items = new ArrayList<String>();
        }
    }

    /**
     * Save the contents of the ListAdapter to a text file.
     */
    private void writeItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
