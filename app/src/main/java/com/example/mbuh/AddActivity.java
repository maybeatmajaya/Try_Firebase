package com.example.mbuh;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddActivity extends AppCompatActivity {

    private EditText editTextQuote;
    private EditText editTextAuthor;
    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        editTextQuote = findViewById(R.id.editTextQuote);
        editTextAuthor = findViewById(R.id.edittextAuthor);
        addButton = findViewById(R.id.addButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String quote = editTextQuote.getText().toString();
                String author = editTextAuthor.getText().toString();

                if (quote.isEmpty()) {
                    editTextQuote.setError("Cannot be empty");
                    return;
                }
                if (author.isEmpty()) {
                    editTextAuthor.setError("Cannot be empty");
                    return;
                }
                addQuoteToDB(quote, author);
            }
        });
    }

    private void addQuoteToDB(String quote, String author) {
        HashMap<String, Object> quoteHashMap = new HashMap<>();
        quoteHashMap.put("quote", quote);
        quoteHashMap.put("author", author);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference quoteRef = database.getReference("quote");
        String key = quoteRef.push().getKey();
        quoteHashMap.put("key",key);

        quoteRef.child(key).setValue(quoteHashMap).addOnCompleteListener
                (new OnCompleteListener<Void>() {
        @Override
                 public void onComplete(@NonNull Task<Void> task) {
            if (task.isSuccessful()) {
            Toast.makeText(AddActivity.this, "Added",
                    Toast.LENGTH_SHORT).show();
            editTextQuote.getText().clear();
            editTextAuthor.getText().clear();
            } else {
                Toast.makeText(AddActivity.this, "Failed to add quote", Toast.LENGTH_SHORT).show();
            }
        }
     });
    }
}
