package com.shakeup.cinderelly.editscreen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.shakeup.cinderelly.R;
import com.shakeup.cinderelly.model.DbUtils;
import com.shakeup.cinderelly.model.Task;

import java.util.Arrays;
import java.util.Date;

/**
 * Created by Jayson on 8/18/2017.
 * Dialog used to edit or add tasks
 */

public class EditDialogFragment
        extends android.support.v4.app.DialogFragment
        implements DatePicker.OnDateChangedListener,
        AdapterView.OnItemSelectedListener {

    EditText mEditItemView;
    int mTaskId;
    Task mTask;
    Button mSave;
    Spinner mDate, mPriority;

    boolean mIsNewTask;

    public static EditDialogFragment newInstance() {

        Bundle args = new Bundle();

        EditDialogFragment fragment = new EditDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(R.style.EditDialog, R.style.EditDialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_edit, container, false);

        // Get extras
        Bundle args = getArguments();
        mTaskId = args.getInt(getString(R.string.EXTRA_TASK_ID), -1);

        if (mTaskId == -1) {
            mIsNewTask = true;
            getDialog().setTitle(getString(R.string.title_new_task));
            // Create a new task
            mTask = new Task();
        } else {
            mIsNewTask = false;
            getDialog().setTitle(R.string.title_edit_task);
            // Get the current
            mTask = DbUtils.getTaskFromId(mTaskId);
        }

        // Get view references
        mEditItemView = view.findViewById(R.id.edit_text_edit_item);
        mSave = view.findViewById(R.id.button_save);
        mDate = view.findViewById(R.id.spinner_date);
        mPriority = view.findViewById(R.id.spinner_priority);

        // Setup views
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveButtonClick();
            }
        });

        // Date Spinner
        String[] dateString = new String[1];
        // Populate spinner with single item
        dateString[0] = DateFormat.format("MM/dd/yyyy", new Date(mTask.dueDate)).toString();
        ArrayAdapter<String> dateAdapter =
                new ArrayAdapter<>(
                        getContext(),
                        android.R.layout.simple_spinner_item,
                        Arrays.asList(dateString));
        mDate.setAdapter(dateAdapter);
        // Hijack clicks to launch our own dialog
        mDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    DialogFragment newFragment = new DatePickerFragment();
                    newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
                }
                return true;
            }
        });

        // Priority Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.array_priorities, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPriority.setAdapter(adapter);
        mPriority.setSelection(mTask.priority);
        mPriority.setOnItemSelectedListener(this);

        // Set edit text box and request focus
        mEditItemView.setText(mTask.text);

        // Open soft keyboard
        getActivity().getWindow()
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        return view;
    }

    public void saveButtonClick() {
        // Update the record
        DbUtils.updateTask(mTask, mEditItemView.getText().toString());

        // Send result and finish activity
        EditDialogCallback callback =
                (EditDialogCallback) getActivity();
        callback.onEditDialogResult();
        this.dismiss();
    }


    /**
     * Called when the date picker returns a user selected date. This method saves
     * the date but does not update the record.
     */
    @Override
    public void onDateChanged(DatePicker datePicker, int year, int month, int day) {

    }

    /**
     * Called when an item is selected from the Priority spinner. The priority is saved but
     * the record is not yet updated
     */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        mTask.priority = i;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // Do nothing
    }
}
