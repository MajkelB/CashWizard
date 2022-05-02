package eu.pp.cashwizard.view.model;

import eu.pp.cashwizard.model.Person;

public class PeopleListRow implements Comparable<PeopleListRow> {

    public Person person;

    public PeopleListRow(Person person) {
        this.person = person;
    }

    @Override
    public int compareTo(PeopleListRow peopleListRow) {
        return this.person.compareToByNickName( peopleListRow.person );
    }

    public int compareAsc(PeopleListRow peopleListRow) {
        return compareTo( peopleListRow );
    }

    public int compareDesc(PeopleListRow peopleListRow) {
        return (-1)*compareTo( peopleListRow );
    }


}
