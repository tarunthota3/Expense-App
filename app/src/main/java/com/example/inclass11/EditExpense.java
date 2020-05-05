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
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;

public class EditExpense extends AppCompatActivity {

    private static final String TAG = "demo";
    EditText editTextExpenseName1;
    Spinner spinner1;
    EditText editTextAmount1;
    Button buttonSave;
    Button buttonCancel1;
    String category = "Groceries";
    Expense expense = null;

    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_expense);
        setTitle("Edit Expense");
        db = FirebaseFirestore.getInstance();

        editTextExpenseName1 = findViewById(R.id.editTextExpenseName1);
        editTextAmount1 = findViewById(R.id.editTextAmount1);
        buttonSave = findViewById(R.id.buttonSave);
        buttonCancel1 = findViewById(R.id.buttonCancel1);

        buttonCancel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        spinner1 = findViewById(R.id.spinner1);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        spinner1.setAdapter(adapter);

        if(getIntent() != null && getIntent().getExtras() != null ){
            expense = (Expense) getIntent().getExtras().getSerializable(ShowExpense.EXPENSE_KEY);

            editTextExpenseName1.setText(expense.getTitle());
            editTextAmount1.setText(expense.getCost()+"");

            String categoryArray[] = getResources().getStringArray(R.array.categories_array);
            for (int i = 0; i< categoryArray.length; i++) {
                if(categoryArray[i].equals(expense.category)){
                    spinner1.setSelection(i);
                }
            }
        }

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Object> hmap = new HashMap<>();
                hmap.put("title",editTextExpenseName1.getText().toString());
                Double amount = Double.parseDouble(editTextAmount1.getText().toString());
                hmap.put("amount", amount);
                hmap.put("category",category);
                hmap.put("date", new Date());
                Log.d(TAG, "ExpenseID: " + expense.id);

                db.collection("expenses").document(expense.id)
                        .update(hmap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(EditExpense.this, "Expense successfully updated", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(EditExpense.this, MainActivity.class);
                                startActivity(intent);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "Error in updating the expense");
                            }
                        });
            }
        });

    }
}
