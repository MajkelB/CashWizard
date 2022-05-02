package eu.pp.cashwizard.model;

import androidx.annotation.Nullable;
import androidx.room.Entity;

import eu.pp.cashwizard.dict.Currency;
import eu.pp.cashwizard.util.AUtil;
import eu.pp.cashwizard.util.JUtil;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@Entity(tableName = "Bills")
public class Bill implements Comparable<Bill>, Serializable {

    private Long id;
    private List<Payment> payments;
    private Date billDate;
    private String title;
    private String description;
    private Set<Person> persons;

    private BigDecimal hlpSum;

    public Bill() {
        setId();
        payments = new ArrayList<>();
        persons = new HashSet<>();
    }

    public Bill(List<Payment> payments, Date billDate, String title, String description) {
        setId();
        this.payments = payments;
        this.billDate = billDate;
        this.description = description;
        this.title = title;
    }

    public Bill(Payment payment, Date billDate, String title, String description) {
        setId();
        this.payments = new ArrayList<>();
        payments.add( payment );
        this.billDate = billDate;
        this.description = description;
        this.title = title;
    }
    
    public Bill( Person person, Cash cash, Date billDate, String title, String description) {
        setId();
        this.payments = new ArrayList<>();
        payments.add(new Payment(cash, JUtil.now(), person));
        this.billDate = billDate;
        this.description = description;
        this.title = title;
    }

    public Bill( Person person, BigDecimal amount, Currency currency, Date billDate, String title, String description) {
        setId();
        this.payments = new ArrayList<>();
        payments.add(new Payment( new Cash(amount, currency), JUtil.now(), person));
        this.billDate = billDate;
        this.description = description;
        this.title = title;
    }

    public void updateBill( Bill newData ) {
        this.setPayments( newData.getPayments() );
        this.setBillDate( newData.getBillDate() );
        this.setTitle( newData.getTitle() );
        this.setDescription( newData.getDescription() );
        this.setPersons( newData.getPersons() );
    }

    private void setId() {
        id = JUtil.now().getTime();
        try {
            Thread.sleep( 1 );
        } catch (InterruptedException e) {
        }
    }

    public void addPayment( Payment payment ) {
        if ( this.payments == null ) {
            this.payments = new ArrayList<>();
        }
        payments.add(payment);
    }

    public List<Payment> getPayments4Person( Person person ) {
        List<Payment> list = new ArrayList<>();
        for( Payment p: JUtil.safeList(payments) ) {
            if( p.is4Person(person) ) list.add( p );
        }
        return list;
    }

    // sprawdza czy dla danej osoby byla platnosc w rachunku
    public boolean isPayer( Person p ) {
        if( p == null ) return false;
        if( payments == null ) return true;
        for( Payment pay: JUtil.safeList( payments ) ) {
            if( pay.getPerson().equals( p ) ) return true;
        }
        return false;
    }

    public boolean is4Person( Person p ) {
        if( p == null ) return false;
        if( persons == null ) return true;
        if( persons.contains(p) ) return true;
        return false;
    }

    private void addAmount( BigDecimal amount ) {
        hlpSum = hlpSum.add( amount );
    }

    public BigDecimal getTotalAmount( Currency currency ) {
        BigDecimal sum = BigDecimal.ZERO;
        hlpSum = BigDecimal.ZERO;
//        payments.forEach( p -> {
//            AUtil.logI( "Płatność: " + p.getCash().getAmount() + " " + p.getCash().getCurrency() + "/" + currency );if( p.getCash().getCurrency().equals( currency ) ) { sum.add( p.getCash().getAmount() ); AUtil.logI("Dodaje");} } );
//        AUtil.logI( "Suma: " + sum );
//        toString();
        for( Payment p: JUtil.safeList( getPayments() ) ) {
            if( currency.equals( p.getCash().getCurrency() ) ) sum = sum.add( p.getCash().getAmount() );
        }
        payments.forEach(  p -> { if( p.getCash().getCurrency().equals( currency ) ) { addAmount( p.getCash().getAmount() ); } } );

            return  hlpSum;
    }

    public BigDecimal getTotalAmount() throws Exception{
        return getTotalAmount( getCurrency() );
    }

    public Set<Currency> getCurrencies() {
        Set<Currency> currencies = new HashSet<>();
        payments.forEach( p -> { currencies.add( p.getCash().getCurrency() ); } );
        return currencies;
    }

    public Currency getCurrency() throws Exception {
        Set<Currency> currencies = getCurrencies();
        if( currencies.size() == 1 ) {
            Iterator<Currency> i = currencies.iterator();
            return i.next();
        }
        throw new Exception( "Więcej niż jedna waluta!" );
    }
    public String toString() {
        StringBuilder strBld = new StringBuilder();
        BigDecimal amount;
        int counter = 0;
        strBld.append( "Rachunek: " );
        strBld.append( getId() ).append( " " );
        strBld.append( JUtil.getDate( getBillDate() ) ).append( " " );
        strBld.append( getDescription() ).append( JUtil.getNewLine() );
        for( Payment p: JUtil.safeList( getPayments() ) ) {
            strBld.append( p.toString() ).append( JUtil.getNewLine() );
        }
        strBld.append( "Na kogo: " );
        for( Person p: JUtil.safeSet( getPersons() ) ) {
            strBld.append( p.getNickName() ).append( JUtil.getNewLine() );
        }
        return strBld.toString();
    }

    @Override
    public int compareTo(Bill bill) {
        return 0;
    }

    public int compareToByDate(Bill bill) {
        if( getBillDate() == null ) return 0;
        if( bill.getBillDate() == null ) return 0;
        return getBillDate().compareTo( bill.getBillDate() );
    }
}
