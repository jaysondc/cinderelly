package com.shakeup.cinderelly.model;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by Jayson on 8/18/2017.
 *
 * This creates the basic database schema for our tasks
 */

@Database(name = TaskDatabase.NAME, version = TaskDatabase.VERSION)
public class TaskDatabase {
    public static final String NAME = "TaskDatabase";
    public static final int VERSION = 2;
}




