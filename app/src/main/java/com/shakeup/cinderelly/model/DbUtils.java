package com.shakeup.cinderelly.model;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;

/**
 * Created by Jayson on 8/18/2017.
 *
 * Other classes can call this to interact with the task database
 */

public class DbUtils {


    /**
     * Returns a list of tasks ordered by descending priority
     * @return an ArrayList of tasks
     */
    public static ArrayList<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.addAll(SQLite.select()
                .from(Task.class)
                .orderBy(Task_Table.priority.desc())
                .queryList());

        return tasks;
    }


    /**
     * Adds a new task to the database with the specified text and priority
     * @param taskText is the text description of the task.
     * @param priority is the priority of the task.
     */
    public static void addTask(String taskText, int priority) {
        Task newTask = new Task();
        newTask.text = taskText;
        newTask.priority = priority;
        newTask.dateAdded = System.currentTimeMillis();
        newTask.save();
    }

    /**
     * Returns a list of tasks ordered by descending priority
     */
    public static boolean deleteTask() {
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.addAll(SQLite.select()
                .from(Task.class)
                .orderBy(Task_Table.priority.desc())
                .queryList());

        return true;
    }

}
