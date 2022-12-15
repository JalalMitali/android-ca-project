package ca.college.es03;


import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;
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
    private ConstraintSet mConstraintSet = new ConstraintSet();
    // Source de données
    private ArrayList<Etat> mlisteEtats;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // set action menu items
        switch (item.getItemId()) {
            case R.id.action_visit_site:
                // we start a new external uri intent
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(site));
                startActivity(browserIntent);
                return true;
            case R.id.action_reset:

                clickable = true;
                attempts = 0;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jeu);
         View stateImg = findViewById(R.id.state_img);
        if (getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            stateImg.setScaleX(2);
            stateImg.setScaleX(2);
        }
        // Lire les données du fichier dans mlisteEtats
        mlisteEtats = Etat.lireDonnees(this, sNomFichier);



        // random state from states array
        Etat etat = mlisteEtats.get(new Random().nextInt(mlisteEtats.size()));
        TextView name = findViewById(R.id.username);
        // store view variables by IDs
        TextView capitalName = findViewById(R.id.capital_name);
        TextView stateName = findViewById(R.id.state_name);
        String capital = etat.getCapitale();
        ImageView stateImage = findViewById(R.id.state_img);
        Bundle extras = getIntent().getExtras();
        String drawable = etat.getDrawable();

        // the button which we use to show the result
        Button isValid = findViewById(R.id.button);
        if (extras != null) {
            // set the textview to our user's name
            String value = extras.getString("name");

            name.setText(value);
            capitalName.setText(capital);
        }
        site = etat.getWikiUrl();

        // set the drawable image for the state
        stateImage.setImageResource(getResources().getIdentifier(drawable, "drawable", getPackageName()));
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        StateRecyclerView adapter = new StateRecyclerView(this, mlisteEtats);

        // on state item clicked login
        StateRecyclerView.ItemClickListener itemClickListener = new StateRecyclerView.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(clickable) {
                    // increase number of attempts ( tries )
                    attempts++;

                    // check if the name of the state is not equal to the item in position
                    if(!Objects.equals(etat.getNom(), mlisteEtats.get(position).getNom())){
                        // user couldn't get the right answer
                        isValid.setVisibility(View.VISIBLE);
                        isValid.setText(String.valueOf(attempts));
                        isValid.setBackgroundColor(Color.parseColor("#FF0000"));
                    }
                    else {
                        // else show the win button
                        isValid.setVisibility(View.VISIBLE);
                        stateName.setVisibility(View.VISIBLE);
                        stateName.setText(etat.getNom());
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