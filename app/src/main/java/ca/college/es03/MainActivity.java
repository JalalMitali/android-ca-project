package ca.college.es03;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText name = (EditText)findViewById(R.id.name);
        Button btn = findViewById(R.id.connect_btn);
        btn.setOnClickListener(v -> {
            /* validate if the length of the name
            if there are less 3 three chars we skip
            otherwise we go to next activity
             */
            if(name.length() < 3) {
                // we always start with false condition
                Toast.makeText(getApplicationContext(),R.string.short_name,Toast.LENGTH_LONG).show();
            }
            else {
                // save the data ( name string ) in intent extras
                Intent i = new Intent(MainActivity.this, JeuActivity.class);
                i.putExtra("name", name.getText().toString());
                startActivity(i);
            }
        });
    }
}