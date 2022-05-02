package eu.pp.cashwizard.view.model;

import eu.pp.cashwizard.model.Person;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PeopleChooseListRow extends PeopleListRow {

    public boolean checked;

    public PeopleChooseListRow(Person person) {
        super(person);
        checked = false;
    }

    public PeopleChooseListRow(Person person, boolean checked) {
        super(person);
        this.checked = checked;
    }

    public void negateChecked() {
        checked = !checked;
    }


}
