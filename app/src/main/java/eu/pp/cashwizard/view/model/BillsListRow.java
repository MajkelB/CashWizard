package eu.pp.cashwizard.view.model;

import eu.pp.cashwizard.model.Bill;

public class BillsListRow implements Comparable<BillsListRow> {

    public Bill bill;

    public BillsListRow( Bill bill ) {
        this.bill = bill;
    }


    @Override
    public int compareTo(BillsListRow billsListRow) {
        return this.bill.compareToByDate( billsListRow.bill );
    }

    public int compareAsc(BillsListRow billsListRow) {
        return compareTo( billsListRow );
    }

    public int compareDesc(BillsListRow billsListRow) {
        return (-1)*compareTo( billsListRow );
    }

}
