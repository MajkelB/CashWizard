package eu.pp.cashwizard;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import eu.pp.cashwizard.configuration.Conf;
import eu.pp.cashwizard.data.DataRepository;
import eu.pp.cashwizard.dict.Action;
import eu.pp.cashwizard.model.Bill;
import eu.pp.cashwizard.util.AUtil;
import eu.pp.cashwizard.util.JUtil;
import eu.pp.cashwizard.view.adapters.BillsListAdapter;
import eu.pp.cashwizard.view.adapters.other.DialogQuestionCallback;
import eu.pp.cashwizard.view.adapters.other.UniversalListCallback;
import eu.pp.cashwizard.view.adapters.other.ViewCaller;
import eu.pp.cashwizard.view.model.BillsListRow;
import eu.pp.cashwizard.view.model.PeopleListRow;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class BillsListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, UniversalListCallback, DialogQuestionCallback, ActivityResultCallback<ActivityResult>  {

    @BindView( R.id.lBillsList ) ListView lBillsList;
    @BindView( R.id.bBillsListBack ) ImageButton bBack;
    //@BindView( R.id.bBillsListAdd ) ImageButton bBillsListAdd;


    static final int PICK_CONTACT_REQUEST = 34;

    static final String COMM_DELETE_CONFIRM = "COMM_DELETE_CONFIRM";

    ArrayList<BillsListRow> items;
    BillsListAdapter billsAdapter;
    BillsListRow selectedItem;
    int selectedItemPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bills_list);
        ButterKnife.bind( this );
        init();
    }

    private void init() {
//        getSupportActionBar().hide();
        List<Bill> bills = JUtil.safeList( DataRepository.getBills() );
        items = new ArrayList<>();
        for( Bill b: bills ) items.add( new BillsListRow( b ) );

        billsAdapter = new BillsListAdapter(this, items, this );
        lBillsList.setAdapter( billsAdapter );
        lBillsList.setOnItemClickListener( this );
        billsAdapter.sort( (billsListRow, t1) -> billsListRow.compareDesc(t1) );

        View backgroundImage = findViewById(R.id.billsListBody);
        Drawable background = backgroundImage.getBackground();
        background.setAlpha(80);

        bBack.requestFocus();
    }

    @OnClick( R.id.bBillsListAdd )
    public void billAddClicked() {
        billDetails( null );
    }

//    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
//    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
//        new ActivityResultContracts.StartActivityForResult(),
//        new ActivityResultCallback<ActivityResult>() {
//            @Override
//            public void onActivityResult(ActivityResult result) {
//                if (result.getResultCode() == Activity.RESULT_OK) {
//                    // There are no request codes
//                    Intent data = result.getData();
//                    //doSomeOperations();
//                }
//            }
//        });

    public void billDetails( Long billId ) {
        Intent intentAct = new Intent(BillsListActivity.this, BillDetailsActivity.class);
        AUtil.logI( "Edit bill: " + billId );
        intentAct.putExtra("BillId",  "" + billId );
//        startActivityForResult(intentAct, PICK_CONTACT_REQUEST );

//        public void openSomeActivityForResult() {
//            Intent intent = new Intent(this, SomeActivity.class);
//
//        }

        // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),this);


        activityResultLauncher.launch(intentAct);

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        AUtil.logI( "Bill number: " + i );
        selectedItem = (BillsListRow) adapterView.getItemAtPosition(i);
        billDetails( selectedItem.bill.getId() );
    }


    @Override
    public void onActivityResult(ActivityResult result) {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();
            Bundle extras = data.getExtras();

            try {
                Action a = (Action) extras.get( "Action" );
                AUtil.logD( "Akcja: " + a.name() );
                long billId = extras.getLong("BillId" );
                if( a.equals( Action.ADD ) ) {
                    Bill bill = DataRepository.getBillById(billId);
                    items.add(new BillsListRow(bill));
                } else if( a.equals( Action.REMOVE ) ) {
                    DataRepository.removeBillById(billId);
                    items.remove(selectedItem);
                }
//                    } else if( a.equals( Action.CANCEL) ) {
//                        // do nothing
//                    }
                billsAdapter.sort( (billsListRow, t1) -> billsListRow.compareDesc(t1) );
                billsAdapter.notifyDataSetChanged();
                AUtil.logI("Result: billId odebrane: " + billId );
            }catch ( NumberFormatException nfe ) {
                AUtil.logE("Bill Id Error: " + extras.getLong("BillId") );
            }

        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        // Check which request we're responding to
//        if (requestCode == PICK_CONTACT_REQUEST ) {
//            // Make sure the request was successful
//            if (resultCode == RESULT_OK) {
//                Bundle extras = data.getExtras();
//                try {
//                    Action a = (Action) extras.get( "Action" );
//                    AUtil.logD( "Akcja: " + a.name() );
//                    long billId = extras.getLong("BillId" );
//                    if( a.equals( Action.ADD ) ) {
//                        Bill bill = DataRepository.getBillById(billId);
//                        items.add(new BillsListRow(bill));
//                    } else if( a.equals( Action.REMOVE ) ) {
//                        DataRepository.removeBillById(billId);
//                        items.remove(selectedItem);
//                    }
////                    } else if( a.equals( Action.CANCEL) ) {
////                        // do nothing
////                    }
//                    billsAdapter.sort( (billsListRow, t1) -> billsListRow.compareDesc(t1) );
//                    billsAdapter.notifyDataSetChanged();
//                    AUtil.logI("Result: " + requestCode + " * billId odebrane: " + billId );
//                }catch ( NumberFormatException nfe ) {
//                    AUtil.logE("Bill Id Error: " + extras.getLong("BillId") );
//                }
//
//            }
//        }
//    }

//    @OnClick( R.id.bBillsListOk )
//    public void settingsOk() {
//        AUtil.logD( "Clicked" );
//        BillsListActivity.this.finish();
//    }
//    @OnClick( R.id.bBillsListCancel )
//    public void settingsCancel() {
//        BillsListActivity.this.finish();
//    }
    @OnClick( R.id.bBillsListBack )
    public void settingsBack() {
        BillsListActivity.this.finish();
    }

    @Override
    public void updateActivity(int position, ViewCaller caller) {
        if( caller.equals( ViewCaller.BUTTON_DELETE ) ) {
            selectedItemPosition = position;
            AUtil.showYesNoDialog(this, "Confirm", "Are you sure?", this, COMM_DELETE_CONFIRM);
        } else if( caller.equals( ViewCaller.LIST_ROW ) ) {
            selectedItemPosition = position;
            selectedItem = billsAdapter.getItem( position );
            if( selectedItem != null ) billDetails( selectedItem.bill.getId() );
        }
    }

    @Override
    public void yesAnswer(String communicationId) {
        if( communicationId.equals( COMM_DELETE_CONFIRM ) ) {
            items.remove( selectedItemPosition );
            billsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void noAnswer(String communicationId) {

    }

}
