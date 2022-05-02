package eu.pp.cashwizard.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.List;

import eu.pp.cashwizard.R;
import eu.pp.cashwizard.dict.Currency;
import eu.pp.cashwizard.util.JUtil;
import eu.pp.cashwizard.view.model.BalanceRow;
import eu.pp.cashwizard.view.model.BillsListRow;
import lombok.NonNull;

public class BalanceAdapter extends ArrayAdapter<BalanceRow> {

    public BalanceAdapter(@NonNull Context context, @NonNull List<BalanceRow> balances ) {
        super(context, 0, balances );
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BalanceRow lr = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_balances, parent, false);
        }
        TextView num = (TextView) convertView.findViewById(R.id.lrBalanceNr);
        TextView amount = (TextView) convertView.findViewById(R.id.lrBallanceAmount);
        TextView currency = (TextView) convertView.findViewById(R.id.lrBalanceCurrency);

        num.setText( "" + ( position + 1 ) );
        amount.setText( "" + lr.cash.getAmount() );
        currency.setText( "" + lr.cash.getCurrency() );

        return convertView;

    }
}
