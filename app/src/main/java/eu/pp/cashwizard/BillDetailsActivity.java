package eu.pp.cashwizard;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import butterknife.OnTextChanged;
import eu.pp.cashwizard.data.DataRepository;
import eu.pp.cashwizard.dict.Action;
import eu.pp.cashwizard.dict.Currency;
import eu.pp.cashwizard.model.Bill;
import eu.pp.cashwizard.model.Cash;
import eu.pp.cashwizard.model.Payment;
import eu.pp.cashwizard.model.Person;
import eu.pp.cashwizard.util.AUtil;
import eu.pp.cashwizard.util.JUtil;
import eu.pp.cashwizard.view.adapters.PeopleChooseListAdapter;
import eu.pp.cashwizard.view.adapters.PeopleListAdapter;
import eu.pp.cashwizard.view.adapters.other.PickDateCallback;
import eu.pp.cashwizard.view.adapters.other.ViewCaller;
import eu.pp.cashwizard.view.model.BillsListRow;
import eu.pp.cashwizard.view.model.PeopleChooseListRow;
import eu.pp.cashwizard.view.model.PeopleListRow;

public class BillDetailsActivity extends AppCompatActivity implements PickDateCallback, AdapterView.OnItemClickListener {

    @BindView( R.id.lBillDetailsPayersList ) ListView lPayers;
    @BindView( R.id.lBillDetailsPeopleList ) ListView lPeople;
    @BindView( R.id.billDetailsDate ) EditText eDate;
    @BindView( R.id.billDetailsAmount ) EditText eAmount;
//    @BindView( R.id.billDetailsCurrency ) TextView eCurrency;
    @BindView( R.id.sBillDetailsCurrency ) Spinner sCurrency;
    @BindView( R.id.billDetailsTitle ) EditText eTitle;
    @BindView( R.id.billDetailsDescription ) EditText eDescription;

    @BindView( R.id.bBillDetailsCancel ) ImageButton bCancel;
    Bill bill;
    Long billId = null;
    ArrayAdapter<String> currencyAdapter;

    ArrayList<PeopleChooseListRow> payersList;
    PeopleChooseListAdapter payersAdapter;
    PeopleChooseListRow selectedPayer;

    ArrayList<PeopleChooseListRow> peopleList;
    PeopleChooseListAdapter peopleAdapter;
    PeopleChooseListRow selectedPerson;

//    final Calendar myCal = Calendar.getInstance();
//    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

//        @Override
//        public void onDateSet(DatePicker view, int year, int monthOfYear,
//                              int dayOfMonth) {
//            myCal.set(Calendar.YEAR, year);
//            myCal.set(Calendar.MONTH, monthOfYear);
//            myCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//            eDate.setText( JUtil.getDate( myCal.getTime() ) );
//        }
//
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_details);
        ButterKnife.bind( this );
        init();
    }

    private void init() {
//        getSupportActionBar().hide();
        View backgroundImage = findViewById(R.id.billDetailsBody);
        Drawable background = backgroundImage.getBackground();
        background.setAlpha(80);
//        List<Bill> bills = JUtil.safeList( DataRepository.getBills() );
//        items = new ArrayList<>();
//        for( Bill b: bills ) items.add( new BillsListRow( b ) );
//
//        peopleAdapter = new BillsListAdapter(this, items );
//        lPeopleList.setAdapter( peopleAdapter );
//        lPeopleList.setOnItemClickListener( this );
//
//        View backgroundImage = findViewById(R.id.peopleListBody);
//        Drawable background = backgroundImage.getBackground();
//        background.setAlpha(80);

//        eTitle.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                bill.setTitle( charSequence.toString() );
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });

       // currencyAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, Currency.getVisibleValues() );
        currencyAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, Currency.values() );
        sCurrency.setAdapter( currencyAdapter );
        sCurrency.setSelection( 0 );


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String str = extras.getString("BillId");
            try {
                billId = (str == null ? null : Long.parseLong(str));
            } catch( NumberFormatException e ) {
                billId = null;
            }
            AUtil.logI( "Bill to edit: " + billId );
            if( billId == null ) bill = new Bill();
            else bill = DataRepository.getBillById( billId );
            //The key argument here must match that used in the other activity
        }
        if( bill == null ) {
            Toast.makeText( this, "Bill not found!", Toast.LENGTH_LONG).show();
            setResult( RESULT_CANCELED );
            BillDetailsActivity.this.finish();
        } else {
            eDate.setText(JUtil.getDate( bill.getBillDate() ) );

            try {
                sCurrency.setSelection( Currency.getPosition(bill.getCurrency()));
                eAmount.setText( "" + bill.getTotalAmount( bill.getCurrency() ) );
            } catch ( Exception e ) {
                sCurrency.setSelection( 0 );
                eAmount.setText( "" + bill.getTotalAmount( Currency.EUR ) );
            }
//            sCurrency.setSelected();
            eTitle.setText( bill.getTitle() );
            eDescription.setText( bill.getDescription() );
        }
//        eDate.setOnClickListener( view -> {new DatePickerDialog(BillDetailsActivity.this, date, myCal
//                .get(Calendar.YEAR), myCal.get(Calendar.MONTH),
//                myCal.get(Calendar.DAY_OF_MONTH)).show();} );
        eDate.setOnClickListener(view -> AUtil.showDateCalendar( BillDetailsActivity.this, BillDetailsActivity.this, ViewCaller.DEFAULT ) );
        eTitle.setOnFocusChangeListener( (v,hasfocus) -> AUtil.hideKeyBoard(v, this, hasfocus) );
        eDescription.setOnFocusChangeListener( (v,hasfocus) -> AUtil.hideKeyBoard(v, this, hasfocus) );
//        bCancel.setOnFocusChangeListener( (v,hasfocus) -> AUtil.hideKeyBoard(v, this, hasfocus) );
        bCancel.requestFocus();

        payersList = new ArrayList<>();
        for( Person p: DataRepository.getPersons() ) payersList.add( new PeopleChooseListRow( p, bill.isPayer( p ) ) );
        payersAdapter = new PeopleChooseListAdapter(this, payersList );
        lPayers.setAdapter( payersAdapter );
        lPayers.setOnItemClickListener( this );

        peopleList = new ArrayList<>();
        for( Person p: DataRepository.getPersons() ) peopleList.add( new PeopleChooseListRow( p, (billId==null?true:bill.is4Person( p ) ) ) );
        peopleAdapter = new PeopleChooseListAdapter(this, peopleList );
        lPeople.setAdapter( peopleAdapter );
        lPeople.setOnItemClickListener( this );
//        peopleAdapter.sort( (peopleListRow, t1) -> peopleListRow.compareAsc(t1) );
    }

    @OnClick( R.id.bBillDetailsOk )
    public void okClicked() {
        if( payersAdapter.getSelectedPerson() == null ) {
            Toast.makeText(this, R.string.bill_details_no_payer_selected, Toast.LENGTH_SHORT).show();
        } else if( !peopleAdapter.isAtLeastOneSelected() ) {
            Toast.makeText(this, R.string.bill_details_no_contributor_selected, Toast.LENGTH_SHORT).show();
        } else {
            Bill lBill = null;
            if (bill != null) lBill = DataRepository.getBillById(bill.getId());
            Intent data = new Intent();
            data.putExtra("BillId", bill.getId());
            if (lBill != null) { // update bill
                data.putExtra("Action", Action.UPDATE);
                bill.setBillDate( JUtil.getDate( "" + eDate.getText() ) );
                bill.setTitle("" + eTitle.getText());
                bill.setDescription("" + eDescription.getText());
                bill.setPersons( peopleAdapter.getSelectedPersonsSet() );
                bill.getPayments().clear();
                bill.getPayments().add(new Payment(new Cash(JUtil.string2BigDecimal("" + eAmount.getText()), (Currency) sCurrency.getSelectedItem()), JUtil.GetCurrentDate(), DataRepository.getPersonById(payersAdapter.getSelectedPerson().getId())));
                if (DataRepository.updateBill(bill)) setResult(RESULT_OK, data);
                else setResult(RESULT_CANCELED, data);
                AUtil.logI("BillId poprawione: " + bill.getId());
                AUtil.logD("Szczegóły rachunku: " + bill.getId() + " * " + bill.toString());
            } else { // new bill
                data.putExtra("Action", Action.ADD);
                bill.setBillDate(JUtil.GetCurrentDate());
                bill.setTitle("" + eTitle.getText());
                bill.setDescription("" + eDescription.getText());
                bill.setPersons( peopleAdapter.getSelectedPersonsSet() );
                bill.getPayments().add(new Payment(new Cash(JUtil.string2BigDecimal("" + eAmount.getText()), (Currency) sCurrency.getSelectedItem()), JUtil.GetCurrentDate(), DataRepository.getPersonById(payersAdapter.getSelectedPerson().getId())));
                DataRepository.getBills().add(bill);
                AUtil.logI("BillId wstawione: " + bill.getId());
                AUtil.logD("Szczegóły rachunku: " + bill.getId() + " * " + bill.toString());
                setResult(RESULT_OK, data);
            }
            BillDetailsActivity.this.finish();
        }
    }

    @OnClick( R.id.bBillDetailsCancel )
    public void cancelClicked() {
        Intent data = new Intent();
        data.putExtra( "Action", Action.CANCEL );
        data.putExtra( "BillId", bill.getId() );
        setResult( RESULT_OK, data );
        BillDetailsActivity.this.finish();
    }

    @OnTextChanged(R.id.billDetailsTitle)
    void titleChanged(CharSequence charSequence ) {
        bill.setTitle( charSequence.toString() );
    }

    @OnTextChanged(R.id.billDetailsDescription)
    void descriptionChanged(CharSequence charSequence ) {
        bill.setDescription( charSequence.toString() );
    }

    @OnTextChanged(R.id.billDetailsDate)
    void dateChanged(CharSequence charSequence ) {
        Optional<Date> dOptional = null;
        Date d = null;
        try{
            dOptional = Optional.ofNullable( JUtil.getDate( charSequence.toString() ) );
            d = dOptional.orElse( JUtil.now() );
        } catch ( Exception e ) { d = JUtil.now(); }

        if( d != null ) bill.setBillDate( d );
        else {
            AUtil.logI( "Invalid date format: " + charSequence );
            Toast.makeText( this, "Invalid date format!", Toast.LENGTH_LONG).show();
        }
    }

    @OnTextChanged(R.id.billDetailsAmount)
    void amountChanged(CharSequence charSequence ) {
        try {
            Payment p = bill.getPayments().get(0);
            p.setCash( new Cash( JUtil.string2BigDecimal( charSequence.toString() ), (Currency ) sCurrency.getSelectedItem() ) );
        } catch ( Exception e ) {
            AUtil.logI( "Invalid amount format: " + charSequence );
        }
    }

    @OnItemSelected(R.id.sBillDetailsCurrency)
    public void spinnerItemSelected(Spinner spinner, int position) {
        //Currency curr = (Currency) spinner.getSelectedItem();
        Currency curr = (Currency) spinner.getItemAtPosition(position);
        try {
            Payment p = bill.getPayments().get(0);
            p.setCash( new Cash( JUtil.string2BigDecimal( ""+eAmount.getText() ), curr ) );
        } catch ( Exception e ) {
            AUtil.logI( "Invalid currency: " + position );
        }
    }

    @Override
    public void pickDate(Date date, ViewCaller caller) {
        eDate.setText( JUtil.getDate( date ));
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if( adapterView == lPayers ) {
            selectedPayer = (PeopleChooseListRow) adapterView.getItemAtPosition(i);
            selectedPayer.negateChecked();
            if( selectedPayer.isChecked() ) payersAdapter.unselectOthers( selectedPayer );
            payersAdapter.notifyDataSetChanged();
            AUtil.logI("Payer: " + selectedPayer.person.getNickName() );
        } else if( adapterView == lPeople  ) {
            selectedPerson = (PeopleChooseListRow) adapterView.getItemAtPosition(i);
            selectedPerson.negateChecked();
            peopleAdapter.notifyDataSetChanged();
            AUtil.logI("People: " + selectedPerson.person.getNickName() );
        }
    }

//    @Override
//    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//    }
}
