package com.shakeup.cinderelly.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by Jayson on 8/18/2017.
 *
 * Defines the object model for each task.
 */

@Table(database = TaskDatabase.class)
public class Task extends BaseModel {

    public static final int PRIORITY_LOW = 0;
    public static final int PRIORITY_MEDIUM = 1;
    public static final int PRIORITY_HIGH = 2;

    public static final int VIEW_TYPE_ITEM = 0;
    public static final int VIEW_TYPE_SECTION = 1;

    // Default type is item
    public int viewType = VIEW_TYPE_ITEM;

    @Column
    @PrimaryKey(autoincrement = true)
    public int id;

    @Column
    public String text;

    @Column
    public long dateAdded;

    // Priority is 0 through 2, 2being the highest. Possibility for adding
    // higher priorities in the future
    @Column
    public int priority;

    // Optional due date of task
    @Column
    public long dueDate;

    // Whether or not this task is complete
    @Column
    public boolean isCompleted;

}
