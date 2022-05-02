package eu.pp.cashwizard;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import eu.pp.cashwizard.data.DataRepository;
import eu.pp.cashwizard.model.Bill;
import eu.pp.cashwizard.model.Cash;
import eu.pp.cashwizard.util.AUtil;
import eu.pp.cashwizard.util.JUtil;
import eu.pp.cashwizard.view.adapters.BalanceAdapter;
import eu.pp.cashwizard.view.model.BalanceRow;
import eu.pp.cashwizard.view.model.BillsListRow;

public class BalancesActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    @BindView( R.id.lBalancesList) ListView lBalancesList;

    ArrayList<BalanceRow> items;
    BalanceAdapter balancesAdapter;
    BalanceRow selectedItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balances);
        ButterKnife.bind( this );
        init();
    }

    private void init() {
//        getSupportActionBar().hide();
        List<Cash> balances = JUtil.safeList( DataRepository.getTotalBalanceList() );
        items = new ArrayList<>();
        for( Cash c: balances ) items.add( new BalanceRow( c ) );

        balancesAdapter = new BalanceAdapter(this, items );
        lBalancesList.setAdapter( balancesAdapter );
        lBalancesList.setOnItemClickListener( this );

        View backgroundImage = findViewById(R.id.balancesBody);
        Drawable background = backgroundImage.getBackground();
        background.setAlpha(80);

    }

    @OnClick( R.id.bIncomesAdd )
    public void addIncome() {

    }

    @OnClick( R.id.bIncomesList )
    public void incomesList() {
        Intent intent = new Intent( BalancesActivity.this, IncomesListActivity.class );
        startActivity( intent );
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

//    @OnClick( R.id.bBalancesOk )
//    public void settingsOk() {
//        AUtil.logD( "Clicked" );
//        BalancesActivity.this.finish();
//    }
//    @OnClick( R.id.bBalancesCancel )
//    public void settingsCancel() {
//        BalancesActivity.this.finish();
//    }

    @OnClick( R.id.bBalancesBack )
    public void settingsCancel() {
        BalancesActivity.this.finish();
    }
}
