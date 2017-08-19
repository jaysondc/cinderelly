package com.shakeup.cinderelly.editscreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.shakeup.cinderelly.R;
import com.shakeup.cinderelly.model.DbUtils;
import com.shakeup.cinderelly.model.Task;

/**
 * Created by Jayson Dela Cruz on 8/16/2017.
 */

public class EditItemActivity extends AppCompatActivity {

    EditText mEditItemView;
    int mTaskId;
    Task mTask;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit);

        mEditItemView = findViewById(R.id.edit_text_edit_item);

        // Get extras
        Intent callingIntent = getIntent();
        mTaskId = callingIntent.getIntExtra(getString(R.string.EXTRA_TASK_ID), -1);

        // Get the task
        mTask = DbUtils.getTaskFromId(mTaskId);

        // Set edit text box and request focus
        mEditItemView.setText(mTask.text);
        mEditItemView.requestFocus();

        // Open soft keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    public void saveButtonClick(View view) {
        // Update the record
        DbUtils.updateTask(mTask, mEditItemView.getText().toString());

        // Send result and finish activity
        setResult(RESULT_OK);
        finish();
    }
}
