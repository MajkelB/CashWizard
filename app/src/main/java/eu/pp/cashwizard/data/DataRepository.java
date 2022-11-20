package eu.pp.cashwizard.data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import eu.pp.cashwizard.configuration.Conf;
import eu.pp.cashwizard.data.parameter.ParametersHelper;
import eu.pp.cashwizard.data.settlement.SettlementsDao;
import eu.pp.cashwizard.data.settlement.SettlementsHelper;
import eu.pp.cashwizard.dict.Result;
import eu.pp.cashwizard.dict.Sex;
import eu.pp.cashwizard.exceptions.DataException;
import eu.pp.cashwizard.model.Bill;
import eu.pp.cashwizard.dict.Currency;
import eu.pp.cashwizard.model.Cash;
import eu.pp.cashwizard.model.P2PAmount;
import eu.pp.cashwizard.model.Parameter;
import eu.pp.cashwizard.model.Payment;
import eu.pp.cashwizard.model.Person;
import eu.pp.cashwizard.model.PersonAmount;
import eu.pp.cashwizard.model.PersonCurrency;
import eu.pp.cashwizard.model.Settlement;
import eu.pp.cashwizard.tech.DBOperationData;
import eu.pp.cashwizard.tech.ResultCallbackI;
import eu.pp.cashwizard.util.AUtil;
import eu.pp.cashwizard.util.JUtil;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DataRepository implements ResultCallbackI<Settlement> {

    private static Settlement settlement;
    private static Set<Person> persons;
    private static List<Bill> bills;
    private static List<Payment> incomes;
    private static List<Payment> subscriptions;

    static {

        init();

        //test data
        persons.add(new Person("Majkel", "Be", "Majkel", JUtil.now(), Sex.MAN, "Majkel20201024160313.jpg" ) );
        persons.add(new Person("Stefan", "El", "Stefan", JUtil.now(), Sex.MAN ) );
        persons.add(new Person("Józek", "Ce", "Józek", JUtil.now(), Sex.MAN ) );
        persons.add(new Person("Monika", "L", "Monika", JUtil.now(), Sex.WOMAN ) );
        persons.add(new Person("Ludwik", "Sz", "Ludwik", JUtil.now(), Sex.MAN ) );
        persons.add(new Person("Wojtek", "C", "Wojtek", JUtil.now(), Sex.MAN ) );
        persons.add(new Person("Renatka", "P", "Renatka", JUtil.now(), Sex.WOMAN ) );

        bills.add(new Bill(getPersonByName("Majkel"), new BigDecimal(100), Currency.EUR, JUtil.now(), "spozywcze1", "lizaki"));
        bills.add(new Bill(getPersonByName("Stefan"), new BigDecimal(330), Currency.EUR, JUtil.now(), "spozywcze2", "czekolada"));
        bills.add(new Bill(getPersonByName("Józek"), new BigDecimal(200), Currency.EUR, JUtil.now(), "spozywcze3", "hamburger"));
        bills.add(new Bill(getPersonByName("Majkel"), new BigDecimal(30), Currency.EUR, JUtil.now(), "spozywcze4", "suszone jablka"));
        bills.add(new Bill(getPersonByName("Majkel"), new BigDecimal(50), Currency.PLN, JUtil.now(), "spozywcze5", "pizza"));
        bills.add(new Bill(getPersonByName("Ludwik"), new BigDecimal(50), Currency.NOK, JUtil.now(), "spozywcze6", "lody"));

        incomes.add(new Payment(new Cash(new BigDecimal(1000), Currency.EUR), JUtil.now(), getPersonByName("Ludwik")));
        incomes.add(new Payment(new Cash(new BigDecimal(1000), Currency.USD), JUtil.now(), getPersonByName("Ludwik")));
        incomes.add(new Payment(new Cash(new BigDecimal(1000), Currency.PLN), JUtil.now(), getPersonByName("Ludwik")));

        settlement = new Settlement();
//        settlement.setBills( bills );
//        settlement.setPeople( persons );
//        settlement.setIncomes( incomes );


    }

    public DataRepository() {

    }

    public static void init() {
        persons = new HashSet<>();
        bills = new ArrayList<>();
        incomes = new ArrayList<>();
        subscriptions = new ArrayList<>();
    }

    public static Set<Person> getPersons() {
        return persons;
    }

    public static List<Bill> getBills() {
        return bills;
    }

    public static boolean updateBill( Bill b ) {
        if( b == null ) return false;
        Bill b2 = getBillById( b.getId() );
        if( b2 == null ) return false;
        b2.updateBill( b );
        return true;
    }

    public static List<Payment> getIncomes() {
        return incomes;
    }

    public static List<Payment> getSubscriptions() {
        return subscriptions;
    }

    public static Person getPersonByName(String name) {
        for (Person p : persons) {
            if (p.getNickName().equals(name)) {
                return p;
            }
        }
        return null;
    }

    public static Person getPersonById( Long id) {
        for (Person p : persons) {
            if (p.getId().equals( id )) {
                return p;
            }
        }
        return null;
    }

    public static boolean updatePerson( Person p ) {
        if( p == null ) return false;
        Person p2 = getPersonById( p.getId() );
        if( p2 == null ) return false;
        p2.updatePerson( p );
        return true;
    }

    public static boolean removePersonById( Long id ) {
        Iterator<Person> i = persons.iterator();
        Person p;
        boolean res = false;
        while( i.hasNext() ) {
            p = i.next();
            if( p.getId() == id ) {i.remove();res = true;}
        }
        return res;
    }

    public static Bill getBillById( Long id) {
        for (Bill b : bills) {
            if (b.getId().equals( id )) {
                return b;
            }
        }
        return null;
    }

    public static boolean removeBillById( Long id ) {
        Iterator<Bill> i = bills.iterator();
        Bill b;
        boolean res = false;
        while( i.hasNext() ) {
            b = i.next();
            if( b.getId() == id ) {i.remove();res = true;}
        }
        return res;
    }


    public static List<Payment> getPaymentsList4Person(String name) {
        if (name == null) {
            return null;
        }
        Person person = getPersonByName(name);
        if (person == null) {
            return null;
        }
        List<Payment> list = new ArrayList<>();
        for (Bill b : bills) {
            JUtil.add2List(list, b.getPayments4Person(person));
        }
        return null;
    }

    public static List<Bill> getListOfBills(Date from, Date to) {
        List<Bill> list = new ArrayList<>();
        if (from == null && to == null) {
            return JUtil.safeList(bills);
        }
        for (Bill b : JUtil.safeList(bills)) {
            if (JUtil.isWithin(b.getBillDate(), from, to)) {
                list.add(b);
            }
        }
        return list;
    }

    public List<Bill> getBills4Person(Person person) {
        List<Bill> list = new ArrayList<>();
        for (Bill b : JUtil.safeList(bills)) {
            if (b.is4Person(person)) {
                list.add(b);
            }
        }
        return list;
    }

    public static Map<Currency, BigDecimal> getTotalSubscriptions() {
        Map<Currency, BigDecimal> totals = new HashMap<>();
        BigDecimal amount = null;
        for (Payment p : JUtil.safeList(subscriptions)) {
            amount = totals.get(p.getCash().getCurrency());
            if (amount == null) {
                amount = BigDecimal.ZERO;
            }
            amount = amount.add(p.getCash().getAmount());
            totals.put(p.getCash().getCurrency(), amount);
        }
        return totals;
    }

    public static List<Cash> getTotalSubscriptionsList() {
        Map<Currency, BigDecimal> totals = getTotalSubscriptions();
        List<Cash> list = new ArrayList<>();
        totals.forEach((k, v) -> list.add(new Cash(v, k)));
        return list;
    }

    public static Map<Currency, BigDecimal> getTotalBills() {
        Map<Currency, BigDecimal> totals = new HashMap<>();
        BigDecimal amount = null;
        for (Bill b : JUtil.safeList(bills)) {
            for (Payment p : JUtil.safeList(b.getPayments())) {
                amount = totals.get(p.getCash().getCurrency());
                if (amount == null) {
                    amount = BigDecimal.ZERO;
                }
                amount = amount.add(p.getCash().getAmount());
                totals.put(p.getCash().getCurrency(), amount);
            }
        }
        return totals;
    }

    public static List<Cash> getTotalBillsList() {
        Map<Currency, BigDecimal> totals = getTotalBills();
        List<Cash> list = new ArrayList<>();
        totals.forEach((k, v) -> list.add(new Cash(v, k)));
        return list;
    }

    public static Map<Currency, BigDecimal> getTotalIncomes() {
        Map<Currency, BigDecimal> totals = new HashMap<>();
        BigDecimal amount = null;
        for (Payment p : JUtil.safeList(incomes)) {
            amount = totals.get(p.getCash().getCurrency());
            if (amount == null) {
                amount = BigDecimal.ZERO;
            }
            amount = amount.add(p.getCash().getAmount());
            totals.put(p.getCash().getCurrency(), amount);
        }
        return totals;
    }

    public static List<Cash> getTotalIncomesList() {
        Map<Currency, BigDecimal> totals = getTotalIncomes();
        List<Cash> list = new ArrayList<>();
        totals.forEach((k, v) -> list.add(new Cash(v, k)));
        return list;
    }

    public static Map<Currency, BigDecimal> getTotalBalance() {
        Map<Currency, BigDecimal> bills = getTotalBills();
        Map<Currency, BigDecimal> incomes = getTotalIncomes();
        Map<Currency, BigDecimal> balances = new HashMap<>();
        incomes.forEach((k, v) -> {
            BigDecimal a = (balances.get(k) == null ? BigDecimal.ZERO : balances.get(k));
            balances.put(k, a.add(v));
        });
        bills.forEach((k, v) -> {
            BigDecimal a = (balances.get(k) == null ? BigDecimal.ZERO : balances.get(k));
            balances.put(k, a.subtract(v));
        });
        return balances;
    }

    public static List<Cash> getTotalBalanceList() {
        Map<Currency, BigDecimal> totals = getTotalBalance();
        List<Cash> list = new ArrayList<>();
        totals.forEach((k, v) -> list.add(new Cash(v, k)));
        return list;
    }

    public static Set<Currency> getCurrencySet() {
        Map<Currency, BigDecimal> bills = getTotalBills();
        Map<Currency, BigDecimal> incomes = getTotalIncomes();
        Set<Currency> currencySet = new HashSet<>();
        incomes.forEach((k, v) -> currencySet.add(k));
        bills.forEach((k, v) -> currencySet.add(k));
        return currencySet;
    }

    public static Map<PersonCurrency, BigDecimal> getIncomes4Persons() {
        Map<PersonCurrency, BigDecimal> paymentsMap = new HashMap<>();
        Set<Currency> currencySet = getCurrencySet();

        persons.forEach(v -> {
            currencySet.forEach(k -> paymentsMap.put(new PersonCurrency(v, k), BigDecimal.ZERO));
        });

        incomes.forEach(v -> {
            PersonCurrency pc = new PersonCurrency(v.getPerson(), v.getCash().getCurrency());
            BigDecimal a = paymentsMap.get(pc);
            paymentsMap.put(pc, a.add(v.getCash().getAmount()));
        } );
                
        return paymentsMap;
    }
    
    public static Map<PersonCurrency, BigDecimal> getPayments4Persons() {
        Map<PersonCurrency, BigDecimal> paymentsMap = new HashMap<>();
        Set<Currency> currencySet = getCurrencySet();

        persons.forEach(v -> {
            currencySet.forEach(k -> paymentsMap.put(new PersonCurrency(v, k), BigDecimal.ZERO));
        });

        bills.forEach(b -> {
            Set<Person> persons = JUtil.safeSet( b.getPersons() );
            List<Payment> payments = JUtil.safeList( b.getPayments() );
            int cnt = persons.size();
            payments.forEach( k -> {
                persons.forEach( p -> {
                    if( b.is4Person( p ) ) {
                        PersonCurrency pc = new PersonCurrency( p, k.getCash().getCurrency() );
                        BigDecimal a = paymentsMap.get(pc);
                        paymentsMap.put( pc, a.add( k.getCash().getAmount().divide(a, cnt, RoundingMode.CEILING) ) );
                    }
                } );
            } );
        } );
        return paymentsMap;
    }    

    public static Map<PersonCurrency, BigDecimal> getTotalBalance4Persons() {
        Map<PersonCurrency, BigDecimal> paymentsMap = getPayments4Persons();
        Map<PersonCurrency, BigDecimal> incomesMap = getIncomes4Persons();
        Map<PersonCurrency, BigDecimal> totalBalance = new HashMap<>();
        
        incomesMap.forEach( (pc,a) -> totalBalance.put(pc, totalBalance.get(pc).add(a)) );
        paymentsMap.forEach( (pc,a) -> totalBalance.put(pc, totalBalance.get(pc).subtract(a)) );

        return totalBalance;
    }
    
    public static Map<Currency, BigDecimal> getTotalBalance4Person( Person p ) {
        Map<PersonCurrency, BigDecimal> totalBalance = getTotalBalance4Persons();
        Map<Currency, BigDecimal> totalBalance4Person = new HashMap<>();
        
        totalBalance.forEach( (pc,a) -> totalBalance4Person.put( pc.getCurrency(), a)  );
        return totalBalance4Person;
    }

    public static Map<Person, BigDecimal> getBalanceInCurrency( Currency currency ) {
        Map<PersonCurrency, BigDecimal> totalBalance = getTotalBalance4Persons();
        Map<Person, BigDecimal> totalBalance4Currency = new HashMap<>();
        totalBalance.forEach( (pc,a) -> { if( pc.getCurrency().equals( currency ) ) totalBalance4Currency.put( pc.getPerson(), a );  } );
        return totalBalance4Currency;
    }

    private static BigDecimal getTotal( List<PersonAmount> list ) {
        BigDecimal sum = BigDecimal.ZERO;
        list.forEach( pa -> sum.add( pa.getAmount().abs() ));
        return sum;
    }

//    public static List<P2PAmount> count() throws DataException {
//        Map<Person, BigDecimal> totalBalance4Currency = getBalanceInCurrency( Currency.EUR );
//        List<PersonAmount> amounts = new ArrayList<>();
//        List<P2PAmount> cashMoves = new ArrayList<>();
//        boolean end = false;
//        totalBalance4Currency.forEach( (p,a) -> amounts.add( new PersonAmount( p, a )));
//        BigDecimal previousSum = BigDecimal.ZERO, sum = BigDecimal.ZERO;
//        int size = amounts.size();
//        try {
//            while( !end ) {
//                previousSum = sum;
//                sum = getTotal( amounts );
//                if( sum.compareTo( previousSum ) == 0 ) end = true;
//                else {
//                    amounts.sort(new Comparator<PersonAmount>() {
//                        @Override
//                        public int compare(PersonAmount personAmount, PersonAmount t1) {
//                            return personAmount.getAmount().compareTo(t1.getAmount());
//                        }
//                    });
//                    if (amounts.get(0).getAmount().compareTo(BigDecimal.ZERO) == 0 || amounts.get(size - 1).getAmount().compareTo(BigDecimal.ZERO) == 0)
//                        end = true;
//                    else {
//                        if (amounts.get(0).getAmount().abs().compareTo(amounts.get(size - 1).getAmount().abs()) > 0) {
//                            cashMoves.add(new P2PAmount(amounts.get(0).getPerson(), amounts.get(size - 1).getPerson(), amounts.get(0).getAmount().abs()));
//                            amounts.get(0).setAmount(amounts.get(0).getAmount().subtract(amounts.get(size - 1).getAmount().abs()));
//                            amounts.get(size - 1).setAmount(BigDecimal.ZERO);
//                        } else {
//                            cashMoves.add(new P2PAmount(amounts.get(0).getPerson(), amounts.get(size - 1).getPerson(), amounts.get(0).getAmount().abs()));
//                            amounts.get(0).setAmount(BigDecimal.ZERO);
//                            amounts.get(size - 1).setAmount(amounts.get(size - 1).getAmount().add(amounts.get(0).getAmount().abs()));
//                        }
//                    }
//                }
//            }
//        } catch ( Exception e ) {
//            throw new DataException();
//        }
//        return cashMoves;
//    }

    public static void printBalance() {
        getTotalBalance().forEach((k, v) -> AUtil.logI(k + " - " + v));
    }

    public static void printBills() {
        getTotalBills().forEach((k, v) -> AUtil.logI(k + " - " + v));
    }

    public static void printIncomes() {
        getTotalIncomes().forEach((k, v) -> AUtil.logI(k + " - " + v));
    }

    public static Settlement getSettlement() {
        return settlement;
    }

    public static void clearSettlement() {
        settlement = new Settlement();
    }

    public static void loadSettlement() {
        Long id = Conf.getLongProperty( "data.lastSettlementId" );
        if( id != null )
            SettlementsHelper.getSettlementAsync( id, null );
        //else
    }
    public static void saveSettlement() {

        SettlementsHelper.saveSettlement( settlement );
        Parameter p = ParametersHelper.getParameter( Conf.getStringProperty( "data.lastSettlementId" ) );
        p.setValue( "" + settlement.getId() );
        ParametersHelper.updateParameter( p );
    }


    @Override
    public void receiveResult(Result result, DBOperationData<Settlement> data) {

    }

    @Override
    public void receiveProgress(int progress) {

    }
}
