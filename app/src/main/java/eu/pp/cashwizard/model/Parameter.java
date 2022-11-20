package eu.pp.cashwizard.model;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
//@NoArgsConstructor
@AllArgsConstructor
@Entity(tableName = "Parameters")
public class Parameter extends JSONConvertable implements Serializable {

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name="Name")
    @NonNull
    String name;

    @ColumnInfo(name="Value")
    String value;

    @ColumnInfo(name="Description")
    String description;

    @Ignore
    public Parameter(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public Parameter() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
