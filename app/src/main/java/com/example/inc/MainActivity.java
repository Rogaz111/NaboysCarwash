package com.example.inc;

import android.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText userName,passWord;
    private Button button;
    private AlertDialog confirmationDialog;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userName = findViewById(R.id.editTextUsername);
        passWord = findViewById(R.id.editTextPassword);
        button = findViewById(R.id.button);

        //dbHelper = new DatabaseHelper(this);
        //database = dbHelper.getWritableDatabase();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogAndStartIntent();
            }
        });
    }

    @Override
    protected void onDestroy() {
        dbHelper.close(); // Close the database when the activity is destroyed
        super.onDestroy();
    }

    private void showDialogAndStartIntent() {
        // Create the dialog object
         confirmationDialog = new AlertDialog.Builder(this)
                .setTitle("Confirmation")
                .setMessage("Your Account will be created using your cellphone number, do you agree ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Start your intent here
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        intent.putExtra("username",userName.getText().toString());
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();

        // Show the dialog
        confirmationDialog.show();
    }


    private void storeCredentialsInPreferences(String username, String password) {
        SharedPreferences sharedPreferences = getSharedPreferences("user_credentials", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.apply();
    }




}