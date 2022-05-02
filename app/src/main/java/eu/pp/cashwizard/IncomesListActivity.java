package eu.pp.cashwizard;

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
import eu.pp.cashwizard.data.DataRepository;
import eu.pp.cashwizard.model.Bill;
import eu.pp.cashwizard.model.Payment;
import eu.pp.cashwizard.util.JUtil;
import eu.pp.cashwizard.view.adapters.BillsListAdapter;
import eu.pp.cashwizard.view.adapters.IncomesListAdapter;
import eu.pp.cashwizard.view.model.BillsListRow;
import eu.pp.cashwizard.view.model.PaymentsListRow;

public class IncomesListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    @BindView( R.id.lIncomesList ) ListView lIncomesList;

    ArrayList<PaymentsListRow> items;
    IncomesListAdapter incomesAdapter;
    PaymentsListRow selectedItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incomes_list);
        ButterKnife.bind( this );
        init();
    }

    private void init() {
//        getSupportActionBar().hide();
        List<Payment> payments = JUtil.safeList( DataRepository.getIncomes() );
        items = new ArrayList<>();
        for( Payment p: payments ) items.add( new PaymentsListRow( p ) );

        incomesAdapter = new IncomesListAdapter(this, items );
        lIncomesList.setAdapter( incomesAdapter );
        lIncomesList.setOnItemClickListener( this );

        View backgroundImage = findViewById(R.id.incomesListBody);
        Drawable background = backgroundImage.getBackground();
        background.setAlpha(80);

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
