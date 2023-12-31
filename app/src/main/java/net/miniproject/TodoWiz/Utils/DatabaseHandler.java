package net.miniproject.TodoWiz.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import net.miniproject.TodoWiz.Model.ToDoModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String NAME = "toDoListDatabase";
    private static final String TODO_TABLE = "todo";
    private static final String ID = "id";
    private static final String TASK = "task";
    private static final String STATUS = "status";
    private static final String CREATE_TODO_TABLE = "CREATE TABLE " + TODO_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TASK + " TEXT, "
            + STATUS + " INTEGER)";

    private SQLiteDatabase db;

    public DatabaseHandler(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);
        // Create tables again
        onCreate(db);
    }

    public void openDatabase() {
        db = this.getWritableDatabase();
    }

    public void insertTask(ToDoModel task){
        ContentValues cv = new ContentValues();
        cv.put(TASK, task.getTask());
        cv.put(STATUS, 0);
        try {
            db.insert(TODO_TABLE, null, cv);
        }
        catch (Exception e) {

            Log.e("DatabaseHandler", "Error inserting task: " + e.getMessage());
        }
    }

    public List<ToDoModel> getAllTasks(){
        List<ToDoModel> taskList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try{
            cur = db.query(TODO_TABLE, null, null, null, null, null, null, null);
            if(cur != null){
                if(cur.moveToFirst()){
                    do{
                        ToDoModel task = new ToDoModel();
                        task.setId(cur.getInt(cur.getColumnIndex(ID)));
                        task.setTask(cur.getString(cur.getColumnIndex(TASK)));
                        task.setStatus(cur.getInt(cur.getColumnIndex(STATUS)));
                        taskList.add(task);
                    }
                    while(cur.moveToNext());
                }
            }
        }
        catch (Exception e) {
            //for query error
            Log.e("DatabaseHandler", "Error retrieving tasks: " + e.getMessage());
        }
        finally {
            db.endTransaction();
            assert cur != null;
            cur.close();
        }
        return taskList;
    }

    public void updateStatus(int id, int status){
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        try {
            db.update(TODO_TABLE, cv, ID + "= ?", new String[]{String.valueOf(id)});
        }
        catch (Exception e) {
            Log.e("DatabaseHandler", "Error updating task status: " + e.getMessage());
        }
    }

    public void updateTask(int id, String task) {
        ContentValues cv = new ContentValues();
        cv.put(TASK, task);
        try {
            db.update(TODO_TABLE, cv, ID + "= ?", new String[]{String.valueOf(id)});
        }
        catch (Exception e) {
            Log.e("DatabaseHandler", "Error updating task: " + e.getMessage());
        }
    }

    public void deleteTask(int id){
        try {
            db.delete(TODO_TABLE, ID + "= ?", new String[]{String.valueOf(id)});
        }
        catch (Exception e) {
            // Handle delete error
            Log.e("DatabaseHandler", "Error deleting task: " + e.getMessage());
        }
    }

    public void printDatabaseContents() {
        List<ToDoModel> tasks = getAllTasks();

        for (ToDoModel task : tasks) {
            Log.d("DatabaseHandler", "Task ID: " + task.getId());
            Log.d("DatabaseHandler", "Task: " + task.getTask());
            Log.d("DatabaseHandler", "Status: " + task.getStatus());
            Log.d("DatabaseHandler", "----------------------");
        }
    }
}
