package ca.college.es03;


import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;


public class JeuActivity extends AppCompatActivity {

    private static final String sNomFichier = "usa.json";
    private static int attempts = 0;
    private static String site = "";
    private static boolean clickable = true;
    // Source de données
    private ArrayList<Etat> mlisteEtats;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_visit_site:
                // we start a new external uri intent
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(site));
                startActivity(browserIntent);
                return true;
            case R.id.action_reset:
                // we kill the activity to erase all data and restart it
                finish();
                startActivity(getIntent());

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jeu);


        // Lire les données du fichier dans mlisteEtats
        mlisteEtats = Etat.lireDonnees(this, sNomFichier);



        // random state from states array
        Etat etat = mlisteEtats.get(new Random().nextInt(mlisteEtats.size()));
        TextView name = findViewById(R.id.username);
        TextView stateNameView = findViewById(R.id.state_name);
        String capital = etat.getCapitale();
        ImageView stateImage = findViewById(R.id.state_img);
        Bundle extras = getIntent().getExtras();
        String drawable = etat.getDrawable();
        Button isValid = findViewById(R.id.button);
        if (extras != null) {
            // set the textview to our user's name
            String value = extras.getString("name");
            Toast.makeText(getApplicationContext(), drawable,Toast.LENGTH_LONG).show();
            name.setText(value);
            stateNameView.setText(capital);
        }
        site = etat.getWikiUrl();
        stateImage.setImageResource(getResources().getIdentifier(drawable, "drawable", getPackageName()));
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        StateRecyclerView adapter = new StateRecyclerView(this, mlisteEtats);
        StateRecyclerView.ItemClickListener itemClickListener = new StateRecyclerView.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(clickable) {
                    attempts++;
                    if(!Objects.equals(etat.getNom(), mlisteEtats.get(position).getNom())){
                        isValid.setVisibility(View.VISIBLE);
                        isValid.setText(String.valueOf(attempts));
                        isValid.setBackgroundColor(Color.parseColor("#FF0000"));
                    }
                    else {
                        isValid.setVisibility(View.VISIBLE);
                        isValid.setText(String.valueOf(attempts));
                        isValid.setBackgroundColor(Color.parseColor("#00FF00"));
                        clickable = false;
                    }
                }
            }
        };
        adapter.setClickListener(itemClickListener);
        recyclerView.setAdapter(adapter);
    }
}