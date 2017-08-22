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
     */
    public static void updateTask(Task task, String updatedText, long updatedDueDate, int updatedPriority){
        task.text = updatedText;
        task.dueDate = updatedDueDate;
        task.priority = updatedPriority;
        task.save();
    }


    /**
     * Adds a new task to the database with the specified text and priority
     */
    public static void addTask(String taskText, String newText, long newDueDate, int newPriority) {
        Task newTask = new Task();
        newTask.text = newText;
        newTask.priority = newPriority;
        newTask.dueDate = newDueDate;
        newTask.isCompleted = false;
        newTask.dateAdded = System.currentTimeMillis();
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
