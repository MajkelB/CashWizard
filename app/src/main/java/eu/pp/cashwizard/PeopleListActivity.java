package eu.pp.cashwizard;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import eu.pp.cashwizard.data.DataRepository;
import eu.pp.cashwizard.dict.Action;
import eu.pp.cashwizard.model.Bill;
import eu.pp.cashwizard.model.Person;
import eu.pp.cashwizard.util.AUtil;
import eu.pp.cashwizard.util.JUtil;
import eu.pp.cashwizard.view.adapters.BillsListAdapter;
import eu.pp.cashwizard.view.adapters.PeopleListAdapter;
import eu.pp.cashwizard.view.model.BillsListRow;
import eu.pp.cashwizard.view.model.PeopleListRow;

public class PeopleListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, ActivityResultCallback<ActivityResult> {

    @BindView( R.id.lPeopleList ) ListView lPeopleList;
    @BindView( R.id.bPeopleListBack ) ImageButton bBack;

    static final int PERSON_DETAILS = 33;

    ArrayList<PeopleListRow> items;
    PeopleListAdapter peopleAdapter;
    PeopleListRow selectedItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_list);
        ButterKnife.bind( this );
        init();
    }

    private void init() {
//        getSupportActionBar().hide();

        View decorView = getWindow().getDecorView();
// Hide both the navigation bar and the status bar.
// SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
// a general rule, you should design your app to hide the status bar whenever you
// hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


//        Glide.with(PeopleListActivity.this)
//                .load(R.drawable.plus)
//                .apply(new RequestOptions()
//                        .placeholder(R.drawable.logo2)
//                        .override(50, 50) // set exact size
//                        .fitCenter() // keep memory usage low by fitting into (w x h) [optional]
//                )
//                .into(holder.my_image_view) // probably set ImageView.scaleType to `fitXY` so it stretches
//        ;

        List<Bill> bills = JUtil.safeList( DataRepository.getBills() );
        items = new ArrayList<>();
        for( Person p: DataRepository.getPersons() ) items.add( new PeopleListRow( p ) );

        peopleAdapter = new PeopleListAdapter(this, items );
        lPeopleList.setAdapter( peopleAdapter );
        lPeopleList.setOnItemClickListener( this );
        peopleAdapter.sort( (peopleListRow, t1) -> peopleListRow.compareAsc(t1) );

        View backgroundImage = findViewById(R.id.peopleListBody);
        Drawable background = backgroundImage.getBackground();
        background.setAlpha(80);

        bBack.requestFocus();

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        selectedItem = (PeopleListRow) adapterView.getItemAtPosition(i);
        selectedItem.person.getId();
        Intent intentAct = new Intent(PeopleListActivity.this, PersonDetailsActivity.class);
        AUtil.logI( "Edit person: " + selectedItem.person.getId() );
        intentAct.putExtra("PersonId",  "" + selectedItem.person.getId() );
//        startActivityForResult(intentAct, PERSON_DETAILS );
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),this);
        activityResultLauncher.launch(intentAct);
    }

    @OnClick( R.id.bPeopleListBack )
    public void settingsBack() {
        PeopleListActivity.this.finish();
    }

    @OnClick( R.id.bPeopleListAdd )
    public void addPerson() {
        AUtil.logI( "Add person! " );
        Intent intentAct = new Intent(PeopleListActivity.this, PersonDetailsActivity.class);
        intentAct.putExtra("PersonId",  "-1" );
//        startActivityForResult(intentAct, PERSON_DETAILS );
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),this);
        activityResultLauncher.launch(intentAct);
    }


    @Override
    public void onActivityResult(ActivityResult result) {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();
            Bundle extras = data.getExtras();


//            @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        // Check which request we're responding to
//        if (requestCode == PERSON_DETAILS ) {
            // Make sure the request was successful
 //           if (resultCode == RESULT_OK) {
 //               Bundle extras = data.getExtras();
            try {
                Action a = (Action) extras.get( "Action" );
                AUtil.logD( "Akcja: " + a.name() );
                long personId = extras.getLong("PersonId" );
                if( a.equals( Action.ADD ) ) {
                    Person person = DataRepository.getPersonById(personId);
                    items.add(new PeopleListRow(person));
                } else if( a.equals( Action.REMOVE ) ) {
                    DataRepository.removePersonById(personId);
                    items.remove(selectedItem);
                }
//                    } else if( a.equals( Action.CANCEL) ) {
//                        // do nothing
//                    }
                peopleAdapter.sort( (peopleListRow, t1) -> peopleListRow.compareAsc(t1) );
                peopleAdapter.notifyDataSetChanged();
                AUtil.logI("Result: personId odebrane: " + personId );
            }catch ( NumberFormatException nfe ) {
                AUtil.logE("Bill Id Error: " + extras.getLong("BillId") );
            }

        }
    }

}
