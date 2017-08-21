package com.shakeup.cinderelly.editscreen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.shakeup.cinderelly.R;
import com.shakeup.cinderelly.model.DbUtils;
import com.shakeup.cinderelly.model.Task;

/**
 * Created by Jayson on 8/18/2017.
 * Dialog used to edit or add tasks
 */

public class EditDialogFragment extends android.support.v4.app.DialogFragment {

    EditText mEditItemView;
    int mTaskId;
    Task mTask;
    Button mSave;

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

        getDialog().setTitle("Edit Task");

        View view = inflater.inflate(R.layout.fragment_edit, container, false);

        mEditItemView = view.findViewById(R.id.edit_text_edit_item);
        mSave = view.findViewById(R.id.button_save);

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveButtonClick();
            }
        });

        // Get extras
        Bundle args = getArguments();
        mTaskId = args.getInt(getString(R.string.EXTRA_TASK_ID), -1);

        // Get the task
        mTask = DbUtils.getTaskFromId(mTaskId);

        // Set edit text box and request focus
        mEditItemView.setText(mTask.text);
        mEditItemView.requestFocus();

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
}
