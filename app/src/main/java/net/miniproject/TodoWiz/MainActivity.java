package net.miniproject.TodoWiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.miniproject.TodoWiz.Adapters.ToDoAdapter;
import net.miniproject.TodoWiz.Model.ToDoModel;
import net.miniproject.TodoWiz.Utils.DatabaseHandler;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements DialogCloseListener{

    private DatabaseHandler db;

    private RecyclerView tasksRecyclerView;
    private ToDoAdapter tasksAdapter;
    private FloatingActionButton fab;

    private List<ToDoModel> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();

        db = new DatabaseHandler(this);
        db.openDatabase();
        getSupportActionBar().setTitle("TodoWiz");
        tasksRecyclerView = findViewById(R.id.tasksRecyclerView);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tasksAdapter = new ToDoAdapter(db,MainActivity.this);
        tasksRecyclerView.setAdapter(tasksAdapter);

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new RecyclerItemTouchHelper(tasksAdapter));
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView);

        fab = findViewById(R.id.fab);

        taskList = db.getAllTasks();
        Collections.reverse(taskList);

        tasksAdapter.setTasks(taskList);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
            }
        });
    }

    @Override
    public void handleDialogClose(DialogInterface dialog){
        taskList = db.getAllTasks();
        Collections.reverse(taskList);
        tasksAdapter.setTasks(taskList);
        tasksAdapter.notifyDataSetChanged();
    }
}




/*




1. Classes: The code defines multiple classes, such as `MainActivity`, `AddNewTask`, `Item`,
    and `RecyclerItemTouchHelper`. Each class encapsulates related data and behavior.

2. Objects: Instances of the defined classes are created using the `new` keyword, such as
    `new DatabaseHandler(this)`, `AddNewTask.newInstance()`, and `new AlertDialog.Builder(adapter.getContext())`.

3. Encapsulation: Each class encapsulates its data and behavior within its scope. For example, `MainActivity`
    encapsulates methods and variables related to the main activity, while `AddNewTask` encapsulates methods and
    variables related to adding a new task.

4. Inheritance: The code extends the `AppCompatActivity` class to create the `MainActivity` and `SplashActivity`
    classes. Inheritance allows these classes to inherit properties and methods from the base class.

5. Polymorphism: The code utilizes method overriding, such as `onCreate()`(Main) and `onSwiped()`(RecyclerItemTouchHelper),
    to provide different implementations of inherited methods based on the specific context.

6. Abstraction: The code uses abstract classes, such as `BottomSheetDialogFragment` and `SimpleCallback`, to
    define common behaviors that can be implemented by subclasses.
    (BottomSheetDialogFragment: extended in AddNewTask. SimpleCallBack: RecyclerItemTouchHelper)

7. Composition: The code demonstrates composition by creating objects of one class within another class.
    For example, `MainActivity` creates an instance of the `ToDoAdapter` class, and `RecyclerItemTouchHelper`
    accepts an instance of the `ToDoAdapter` class as a parameter.


 */