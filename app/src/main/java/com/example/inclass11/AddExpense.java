package com.example.inclass11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;

public class AddExpense extends AppCompatActivity {

    private static final String TAG = "demo";
    EditText editTextExpenseName;
    Spinner spinner;
    EditText editTextAmount;
    Button buttonAddExpense;
    Button buttonCancel;
    String category = "Groceries";



    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        setTitle("Add Expense");
        spinner = findViewById(R.id.spinner1);
        editTextExpenseName = findViewById(R.id.editTextExpenseName1);
        editTextAmount = findViewById(R.id.editTextAmount1);
        buttonAddExpense = findViewById(R.id.buttonSave);
        buttonCancel = findViewById(R.id.buttonCancel1);
        db = FirebaseFirestore.getInstance();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        buttonAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "data: " + editTextExpenseName.getText().toString() + " " + category + " "+ editTextAmount.getText().toString());
                if(editTextExpenseName.getText().toString().equals("")){
                    editTextExpenseName.setError("Can't be empty");
                }
                else if(editTextAmount.getText().toString().equals("")){
                    editTextAmount.setError("can't be empty");
                }
                else{
                    String title = editTextExpenseName.getText().toString();
                    Double amount = Double.parseDouble(editTextAmount.getText().toString());
                    HashMap<String, Object> hmap = new HashMap<>();
                    hmap.put("title",title);
                    hmap.put("amount", amount);
                    hmap.put("category",category);
                    hmap.put("date", new Date());

                    db.collection("expenses")
                            .add(hmap)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d(TAG, "onSuccess: Successful!!!");

                                    Intent intent = new Intent();
                                    intent.putExtra("id",documentReference.getId());
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: save failed");
                                }
                            });
                }
            }
        });
    }
}
