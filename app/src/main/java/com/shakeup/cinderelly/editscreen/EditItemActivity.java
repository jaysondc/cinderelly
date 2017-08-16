package com.shakeup.cinderelly.editscreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.shakeup.cinderelly.R;

/**
 * Created by Jayson Dela Cruz on 8/16/2017.
 */

public class EditItemActivity extends AppCompatActivity {

    EditText etEditItem;
    int editPosition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit);

        etEditItem = (EditText) findViewById(R.id.edit_text_edit_item);

        // Get extras
        Intent callingIntent = getIntent();
        etEditItem.setText(callingIntent.getStringExtra(getString(R.string.EXTRA_EDIT_STRING)));
        editPosition = callingIntent.getIntExtra(getString(R.string.EXTRA_LIST_POSITION), -1);

        etEditItem.requestFocus();

        // Open soft keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    public void saveButtonClick(View view) {
        // Set return data
        String text = etEditItem.getText().toString();
        Intent returnData = new Intent();
        returnData.putExtra(getString(R.string.EXTRA_EDIT_RETURN_STRING), text);
        returnData.putExtra(getString(R.string.EXTRA_LIST_POSITION), editPosition);
        setResult(RESULT_OK, returnData);

        // Send result and finish activity
        finish();
    }
}
