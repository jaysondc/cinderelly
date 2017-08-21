package com.shakeup.cinderelly.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.shakeup.cinderelly.R;
import com.shakeup.cinderelly.Utilities;
import com.shakeup.cinderelly.model.Task;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Jayson on 8/18/2017.
 */

public class TaskAdapter extends BaseAdapter {

    ArrayList<Task> mTasks;
    Context mContext;

    public TaskAdapter(@NonNull Context context, ArrayList<Task> tasks) {
        mTasks = tasks;
        mContext = context;
    }

    /**
     * Handles the creation of views from each Task in the adapter.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        if (convertView == null) {
            LayoutInflater layoutInflater =
                    (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.listitem_todo, parent, false);
        } else {
            view = convertView;
        }

        Task task = mTasks.get(position);

        // Assign to views
        TextView taskText = view.findViewById(R.id.text_item);
        TextView priorityText = view.findViewById(R.id.text_priority);
        TextView dueDate = view.findViewById(R.id.text_due_date);
        CheckBox isCompleted = view.findViewById(R.id.check_is_completed);

        // Get date in String format
        String dateString = DateFormat.format("MM/dd/yyyy", new Date(task.dueDate)).toString();

        // Set view values
        taskText.setText(task.text);
        priorityText.setText(Utilities.priorityToString(task.priority));
        dueDate.setText(dateString);
        isCompleted.setChecked(task.isCompleted);

        return view;
    }

    /**
     * Update the adapter with a new ArrayList of Tasks
     * @param newList ArrayList of type Task
     */
    public void updateList(ArrayList<Task> newList) {
        mTasks.clear();
        mTasks.addAll(newList);
        this.notifyDataSetChanged();
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int i) {
        return true;
    }


    @Override
    public int getCount() {
        return mTasks.size();
    }

    @Override
    public Object getItem(int i) {
        return mTasks.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public int getItemViewType(int i) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return mTasks.isEmpty();
    }


}
