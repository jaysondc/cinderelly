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
import android.widget.Toast;

import com.shakeup.cinderelly.R;
import com.shakeup.cinderelly.model.DbUtils;
import com.shakeup.cinderelly.model.Task;

import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Jayson on 8/18/2017.
 * Dialog used to edit or add tasks
 */

public class EditDialogFragment
        extends android.support.v4.app.DialogFragment
        implements DatePicker.OnDateChangedListener,
        AdapterView.OnItemSelectedListener {

    // Data
    Task mTask;
    int mTaskId;
    long mDueDate;
    int mPriority;

    // Views
    EditText mEditItemView;
    Button mSaveView;
    Spinner mDateView, mPriorityView;

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
            // Set task variables
            mPriority = Task.PRIORITY_LOW;
            mDueDate = System.currentTimeMillis();

        } else {
            mIsNewTask = false;
            getDialog().setTitle(R.string.title_edit_task);
            // Get the current
            mTask = DbUtils.getTaskFromId(mTaskId);
            // Set task variables
            mPriority = mTask.priority;
            mDueDate = mTask.dueDate;
        }

        // Get view references
        mEditItemView = view.findViewById(R.id.edit_text_edit_item);
        mSaveView = view.findViewById(R.id.button_save);
        mDateView = view.findViewById(R.id.spinner_date);
        mPriorityView = view.findViewById(R.id.spinner_priority);

        // Setup views
        mSaveView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveButtonClick();
            }
        });

        // Date Spinner
        String[] dateString = new String[1];
        // Populate spinner with single item
        dateString[0] = DateFormat.format("MM/dd/yyyy", new Date(mDueDate)).toString();
        ArrayAdapter<String> dateAdapter =
                new ArrayAdapter<>(
                        getContext(),
                        android.R.layout.simple_spinner_item,
                        Arrays.asList(dateString));
        mDateView.setAdapter(dateAdapter);
        // Hijack clicks to launch our own dialog
        mDateView.setOnTouchListener(new View.OnTouchListener() {
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
        mPriorityView.setAdapter(adapter);
        mPriorityView.setSelection(mPriority);
        mPriorityView.setOnItemSelectedListener(this);

        // Set edit text box
        mEditItemView.setText(mTask.text);

        // Open soft keyboard
        getActivity().getWindow()
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        return view;
    }

    public void saveButtonClick() {
        // Validate the inputs
        String taskText = mEditItemView.getText().toString();
        if (taskText == "") {
            Toast.makeText(getContext(), "The task must have a name!", Toast.LENGTH_SHORT).show();
            return;
        } else if (mIsNewTask) {
            DbUtils.addTask(taskText, taskText, mDueDate, mPriority);
        } else {
            // Update the record
            DbUtils.updateTask(mTask, taskText, mDueDate, mPriority);
        }
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
        GregorianCalendar date = new GregorianCalendar(year, month, day);
        mDueDate = date.getTimeInMillis();

        String[] dateString = new String[1];
        dateString[0] = DateFormat.format("MM/dd/yyyy", date).toString();
        ArrayAdapter<String> dateAdapter =
                new ArrayAdapter<>(
                        getContext(),
                        android.R.layout.simple_spinner_item,
                        Arrays.asList(dateString));
        mDateView.setAdapter(dateAdapter);
    }

    /**
     * Called when an item is selected from the Priority spinner. The priority is saved but
     * the record is not yet updated
     */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        mPriority = i;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // Do nothing
    }
}
