package com.shakeup.cinderelly.model;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;

import static com.raizlabs.android.dbflow.sql.language.SQLite.select;

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
    public static ArrayList<Task> getAllTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.addAll(select()
                .from(Task.class)
                .orderBy(Task_Table.priority.desc())
                .queryList());

        return tasks;
    }

    /**
     * Returns a single task with the specified ID
     * @param taskId is the ID of the task to retrieve
     * @return
     */
    public static Task getTaskFromId(int taskId) {
        return SQLite.select()
                .from(Task.class)
                .where(Task_Table.id.is(taskId))
                .querySingle();
    }

    /**
     * Update a task with updated args
     * @param task
     * @param updatedText
     */
    public static void updateTask(Task task, String updatedText){
        task.text = updatedText;

        // TODO: Update with using args
        task.dueDate = System.currentTimeMillis();
        task.isCompleted = false;

        task.save();
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

        // TODO: Update with using args
        newTask.dueDate = System.currentTimeMillis();
        newTask.isCompleted = false;

        newTask.save();
    }

    /**
     * Delete a given task
     */
    public static void deleteTask(Task task) {
        task.delete();
    }

    /**
     * Set whether or not the given task is complete
     */
    public static void setTaskCompleted(Task task, boolean isComplete) {
        task.isCompleted = isComplete;
        task.save();
    }


}
