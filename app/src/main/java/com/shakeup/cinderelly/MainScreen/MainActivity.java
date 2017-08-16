package com.shakeup.cinderelly;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvItems = (ListView) findViewById(R.id.list_to_do);

        // Read from file
        readItems();

        itemsAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                items);
        lvItems.setAdapter(itemsAdapter);

        // Set up long click to delete
        setupListViewListener();
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
    private void setupListViewListener() {
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
