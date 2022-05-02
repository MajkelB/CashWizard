package eu.pp.cashwizard.view.model;

import eu.pp.cashwizard.model.Bill;
import eu.pp.cashwizard.model.Cash;

public class BalanceRow {

    public Cash cash;

    public BalanceRow(Cash cash) {
        this.cash = cash;
    }
}
