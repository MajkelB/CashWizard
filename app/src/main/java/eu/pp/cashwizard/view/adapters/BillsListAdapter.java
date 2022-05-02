package eu.pp.cashwizard.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.List;

import eu.pp.cashwizard.R;
import eu.pp.cashwizard.dict.Currency;
import eu.pp.cashwizard.util.AUtil;
import eu.pp.cashwizard.util.JUtil;
import eu.pp.cashwizard.view.adapters.other.UniversalListCallback;
import eu.pp.cashwizard.view.adapters.other.ViewCaller;
import eu.pp.cashwizard.view.model.BillsListRow;
import lombok.NonNull;

public class BillsListAdapter extends ArrayAdapter<BillsListRow> {

    UniversalListCallback callback;

    public BillsListAdapter(@NonNull Context context, @NonNull List<BillsListRow> bills, UniversalListCallback callback ) {
        super(context, 0, bills );
        this.callback = callback;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BillsListRow lr = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_bills_list, parent, false);
        }
        TextView num = (TextView) convertView.findViewById(R.id.lrBillsNr);
        TextView date = (TextView) convertView.findViewById(R.id.lrBillDate);
        TextView title = (TextView) convertView.findViewById(R.id.lrBillTitle);
        TextView amount = (TextView) convertView.findViewById(R.id.lrBillAmount);
        TextView currency = (TextView) convertView.findViewById(R.id.lrBillCurrency);
        ImageButton removeBillButton = (ImageButton) convertView.findViewById( R.id.bBillRemove );

        num.setOnClickListener( view -> { callback.updateActivity( position, ViewCaller.LIST_ROW ); } );
        date.setOnClickListener( view -> { callback.updateActivity( position, ViewCaller.LIST_ROW ); } );
        title.setOnClickListener( view -> { callback.updateActivity( position, ViewCaller.LIST_ROW ); } );
        amount.setOnClickListener( view -> { callback.updateActivity( position, ViewCaller.LIST_ROW ); } );
        currency.setOnClickListener( view -> { callback.updateActivity( position, ViewCaller.LIST_ROW ); } );
        removeBillButton.setOnClickListener( view -> { callback.updateActivity( position, ViewCaller.BUTTON_DELETE ); } );
        num.setText( "" + (position + 1) );
        date.setText( JUtil.getDate( lr.bill.getBillDate() ) );
        title.setText( JUtil.prepareString( lr.bill.getTitle(), 1, 30 ) );
        try {
            amount.setText( "" + lr.bill.getTotalAmount() );
            currency.setText( "" + lr.bill.getCurrency() );
        } catch (Exception e ) {
            amount.setText( "" + lr.bill.getTotalAmount( Currency.EUR ) );
            currency.setText( "" + Currency.EUR );
        }


        return convertView;

    }
}
