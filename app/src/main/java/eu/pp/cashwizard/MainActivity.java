package eu.pp.cashwizard;

import android.app.Activity;
//import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView( R.id.bMainEnd) ImageButton bEnd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind( this );
        init();
    }

    private void init() {
//        getSupportActionBar().hide();
        View backgroundImage = findViewById(R.id.mainBody);
        Drawable background = backgroundImage.getBackground();
        background.setAlpha(100);

    }

    @OnClick( R.id.bMainEnd )
    public void endClicked() {
        Activity mainActivity = MainActivity.this;
        mainActivity.finish();
        System.exit( 0 );
    }

    @OnClick( R.id.bMainBills )
    public void billsClicked() {
        Intent intent = new Intent( MainActivity.this, BillsListActivity.class );
        startActivity( intent );
    }

    @OnClick( R.id.bMainPeople )
    public void peopleClicked() {
        Intent intent = new Intent( MainActivity.this, PeopleListActivity.class );
        startActivity( intent );
    }

    @OnClick( R.id.bMainBalance )
    public void balancesClicked() {
        Intent intent = new Intent( MainActivity.this, BalancesActivity.class );
        startActivity( intent );
    }

    @OnClick( R.id.bMainSettings )
    public void settingsClicked() {
        Intent intent = new Intent( MainActivity.this, SettingsActivity.class );
        startActivity( intent );
    }


}
