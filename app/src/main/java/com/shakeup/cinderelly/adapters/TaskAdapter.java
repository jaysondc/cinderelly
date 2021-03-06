package com.shakeup.cinderelly.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.shakeup.cinderelly.R;
import com.shakeup.cinderelly.Utilities;
import com.shakeup.cinderelly.model.DbUtils;
import com.shakeup.cinderelly.model.Task;

import java.util.ArrayList;
import java.util.Date;

import de.halfbit.pinnedsection.PinnedSectionListView;

import static com.shakeup.cinderelly.model.Task.VIEW_TYPE_SECTION;

/**
 * Created by Jayson on 8/18/2017.
 */

public class TaskAdapter extends BaseAdapter implements PinnedSectionListView.PinnedSectionListAdapter{

    ArrayList<Task> mTasks;
    Context mContext;

    public TaskAdapter(@NonNull Context context, ArrayList<Task> tasks) {
        mTasks = tasks;
        mContext = context;

        addSections();
    }

    /**
     * Add task headers to our ArrayList
     */
    private void addSections() {

        // High
        Task headerHigh = new Task();
        Task headerMedium = new Task();
        Task headerLow = new Task();

        headerHigh.viewType = VIEW_TYPE_SECTION;
        headerMedium.viewType = VIEW_TYPE_SECTION;
        headerLow.viewType = VIEW_TYPE_SECTION;

        headerHigh.text = "HIGH";
        headerMedium.text = "MEDIUM";
        headerLow.text = "LOW";

        int i = mTasks.size()-1;
        while(i >= 0 && mTasks.get(i).priority != Task.PRIORITY_MEDIUM
                && mTasks.get(i).priority != Task.PRIORITY_HIGH) {
            i--;
        }
        mTasks.add(i+1, headerLow);

        while(i >= 0 && mTasks.get(i).priority != Task.PRIORITY_HIGH) {
            i--;
        }
        mTasks.add(i+1, headerMedium);

        mTasks.add(0, headerHigh);

    }

    /**
     * Handles the creation of views from each Task in the adapter.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final Task task = mTasks.get(position);

        View view;
        if (convertView == null) {
            LayoutInflater layoutInflater =
                    (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (task.viewType == Task.VIEW_TYPE_SECTION) {
                view = layoutInflater.inflate(R.layout.listitem_section_header, parent, false);
            } else {
                view = layoutInflater.inflate(R.layout.listitem_todo, parent, false);
            }
        } else {
            view = convertView;
        }


        if (task.viewType == Task.VIEW_TYPE_SECTION) {
            TextView taskText = view.findViewById(R.id.text_item);
            taskText.setText(task.text);
            view.setClickable(false);
        } else {
            // Assign to views
            final TextView taskText = view.findViewById(R.id.text_item);
            TextView priorityText = view.findViewById(R.id.text_priority);
            TextView dueDate = view.findViewById(R.id.text_due_date);
            final CheckBox isCompleted = view.findViewById(R.id.check_is_completed);
            final ConstraintLayout taskDetails = view.findViewById(R.id.task_detail);

            // Get date in String format
            String dateString = DateFormat.format("MM/dd/yyyy", new Date(task.dueDate)).toString();

            // Set view values
            taskText.setText(task.text);
            priorityText.setText(Utilities.priorityToString(task.priority));
            dueDate.setText(dateString);
            isCompleted.setChecked(task.isCompleted);

            // Modify views if the task is completed
            showTaskComplete(view, isCompleted.isChecked());

            // Set click listener for checkbox and apply appropriate styles
            isCompleted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DbUtils.setTaskCompleted(task, isCompleted.isChecked());

                    View listItemView = (View) view.getParent();
                    if (listItemView != null){
                        showTaskComplete(listItemView, isCompleted.isChecked());
                    }
                }
            });
        }

        return view;
    }

    /**
     * Changes the task appearance to show completed or uncompleted tasks
     * @param view is the view we're modifying
     * @param complete is whether or not the task is complete
     */
    void showTaskComplete(View view, boolean complete) {
        TextView taskText = view.findViewById(R.id.text_item);
        ConstraintLayout taskDetails = view.findViewById(R.id.task_detail);

        if (complete) {
            taskText.setPaintFlags(taskText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            taskText.setTextAppearance(R.style.TaskTextCompleted);
            taskDetails.setVisibility(View.GONE);
        } else {
            taskText.setPaintFlags(taskText.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
            taskText.setTextAppearance(R.style.TaskTextNormal);
            taskDetails.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Update the adapter with a new ArrayList of Tasks
     * @param newList ArrayList of type Task
     */
    public void updateList(ArrayList<Task> newList) {
        mTasks.clear();
        mTasks.addAll(newList);
        addSections();
        this.notifyDataSetChanged();
    }

    @Override
    public boolean isItemViewTypePinned(int viewType) {
        return viewType == Task.VIEW_TYPE_SECTION;
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
        return mTasks.get(i).viewType;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public boolean isEmpty() {
        return mTasks.isEmpty();
    }


}
