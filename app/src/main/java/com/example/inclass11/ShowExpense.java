package com.example.inclass11;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;

public class ShowExpense extends AppCompatActivity {

    Expense expense = null;
    static String EXPENSE_KEY = "EXPENSEKEY";
    TextView textViewName, textViewCategory, textViewAmount, textViewDate;
    Button buttonEditExpense, butttonClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_expense);
        setTitle("Show Expense");
        textViewName = findViewById(R.id.textViewName);
        textViewCategory = findViewById(R.id.textViewCategory);
        textViewAmount = findViewById(R.id.textViewAmount);
        textViewDate = findViewById(R.id.textViewDate);
        buttonEditExpense = findViewById(R.id.buttonEditExpense);
        butttonClose = findViewById(R.id.butttonClose);

        if(getIntent() != null && getIntent().getExtras() != null ){
            expense = (Expense) getIntent().getExtras().getSerializable(MainActivity.EXPENSE_KEY);
            textViewName.setText(expense.title);
            textViewCategory.setText(expense.category);
            textViewAmount.setText("$ " + expense.cost);
            Log.d("demo", "date " + expense.date);
            SimpleDateFormat sdfr = new SimpleDateFormat("dd/mm/yyyy");
            textViewDate.setText(expense.date.toString());
        }

        butttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        buttonEditExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowExpense.this, EditExpense.class);
                intent.putExtra(EXPENSE_KEY, expense);
                startActivity(intent);
            }
        });
    }
}
