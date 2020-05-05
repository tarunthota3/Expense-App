package com.example.inclass11;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements ExpenseAdapter.InteractWithMainActivity {
    private static final String TAG="demo";
    static String EXPENSE_KEY = "EXPENSEKEY";
    private FirebaseFirestore db;

    Button buttonPlus;

    ArrayList<Expense> expenses;

    ProgressBar progressBar;
    TextView textViewNoExpense;
    RecyclerView recyclerView;
    RecyclerView.Adapter rv_adapter;
    RecyclerView.LayoutManager rv_layoutManager;
    private static final int REQ_CODE =0x001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Expense App");

        progressBar = findViewById(R.id.progressBar);
        textViewNoExpense = findViewById(R.id.textViewNoExpense);
        buttonPlus = findViewById(R.id.buttonPlus);
        textViewNoExpense.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        expenses = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);

        rv_layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(rv_layoutManager);
        db = FirebaseFirestore.getInstance();

        buttonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddExpense.class);
                startActivityForResult(intent, REQ_CODE);
            }
        });




        db.collection("expenses")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(DocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            
                            String id = documentSnapshot.getId();
                            String title = documentSnapshot.getString("title");
                            Double cost = documentSnapshot.getDouble("cost");
                            String category = documentSnapshot.getString("category");
                            Date date = documentSnapshot.getDate("date");
                            Expense expense = new Expense(id, title, cost, category, date);
                            expenses.add(expense);

                            if(expenses.size()> 0) {
                                textViewNoExpense.setVisibility(View.INVISIBLE);
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                            else{
                                textViewNoExpense.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.INVISIBLE);
                            }

                            rv_adapter = new ExpenseAdapter(expenses, MainActivity.this);
                            recyclerView.setAdapter(rv_adapter);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Falied to load the document " + e.getMessage());
                    }
                });



//        HashMap<String, Object> hmap1 = new HashMap<>();
//        hmap1.put("title","Costco1");
//        db.collection("expenses")
//                .document("ibn3pZtWMaIA632ozsLY")
//                .update(hmap1)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Log.d(TAG, "Susccessfully updated");
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.d(TAG, "Update failed");
//                    }
//                });
    }

    @Override
    public void deleteItem(final int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete an item?")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.collection("expenses").document(expenses.get(position).id)
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(MainActivity.this, "Expense successfully deleted", Toast.LENGTH_SHORT).show();
                                        expenses.remove(position);
                                        rv_adapter.notifyDataSetChanged();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "Unable to delete the expense: " + e.getMessage());
                                    }
                                });
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();

    }

    @Override
    public void showItem(Expense expense) {
        Intent intent = new Intent(this, ShowExpense.class);
        intent.putExtra(EXPENSE_KEY, expense);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: ");
        if(requestCode == REQ_CODE && resultCode == RESULT_OK){
            String id = data.getExtras().getString("id");
            db.collection("expenses").document(id)
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                Log.w(TAG, "Listen failed.", e);
                                return;
                            }
                            if (snapshot != null && snapshot.exists()) {
                                Log.d(TAG, "Current data: " + snapshot.getData());
                            } else {
                                Log.d(TAG, "Current data: null");
                            }
                        }
                    });

        }
    }
}
