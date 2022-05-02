package eu.pp.cashwizard.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import eu.pp.cashwizard.util.JUtil;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
//@AllArgsConstructor
@Getter
@Setter
@Entity(tableName = "Settlements")
public class Settlement implements Serializable {

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name="Id")
    @NonNull
    Long id;

    @ColumnInfo(name="Description")
    String description;

    @ColumnInfo(name="DateFrom")
    Date dateFrom;

    @ColumnInfo(name="DateTo")
    Date dateTo;


//    Set<Person> people;
//    List<Bill> bills;
//    List<Payment> incomes;

    public Settlement() {
        setId();
    }

    private void setId() {
        id = JUtil.now().getTime();
        try {
            Thread.sleep( 1 );
        } catch (InterruptedException e) {
        }
    }

    @Override
    public String toString() {
        return "Settlement{" +
                "id=" + id +
                ", dateFrom=" + dateFrom +
                ", dateTo=" + dateTo +
                ", description='" + description + '\'' +
                '}';
    }

    @NonNull
    public Long getId() {
        return id;
    }

    public void setId(@NonNull Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }
}
