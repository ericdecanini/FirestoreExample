package ericdecanini.firestoreexample;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

/**
 * Don't forget to check out my website!
 * https://www.ericdecanini.com/
 */

public class MainActivity extends AppCompatActivity {

    String LOG_TAG = MainActivity.class.getSimpleName();
    TextView textView;
    EditText editText;
    Button readButton, writeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textview);
        editText = findViewById(R.id.edit_text);
        readButton = findViewById(R.id.read_button);
        writeButton = findViewById(R.id.write_button);

        readButton.setOnClickListener(((View v) -> readData()));
        writeButton.setOnClickListener(((View v) -> writeData()));
    }

    private void readData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            Log.d(LOG_TAG, document.getId() + " => " + document.getData());
                            String name = document.getString("name");
                            textView.setText(name);
                        }
                    } else {
                        textView.setText("Error getting documents. " + task.getException());
                    }
                });
    }

    private void writeData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> user = new HashMap<>();
        user.put("name", editText.getText().toString());

        // Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(documentReference -> textView.setText("DocumentSnapshot added with ID: " + documentReference.getId()))
                .addOnFailureListener(e -> textView.setText("Error adding document " + e));
    }

}
