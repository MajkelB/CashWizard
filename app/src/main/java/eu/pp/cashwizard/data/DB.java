package eu.pp.cashwizard.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import eu.pp.cashwizard.data.person.PersonsDao;
import eu.pp.cashwizard.data.converters.DateConverter;
import eu.pp.cashwizard.data.parameter.ParametersDao;
import eu.pp.cashwizard.data.settlement.SettlementsDao;
import eu.pp.cashwizard.model.Parameter;
import eu.pp.cashwizard.model.Person;
import eu.pp.cashwizard.model.Settlement;

@Database(entities = {Parameter.class, Settlement.class, Person.class}, version = 2, exportSchema = true  )
@TypeConverters(DateConverter.class)
public abstract class DB extends RoomDatabase {


//    public abstract class MyDatabase extends RoomDatabase {
//
//        public static final String DB_NAME = "cashWizardDB";
////        public static final String TABLE_NAME_TODO = "todo";
//    }

    public abstract ParametersDao parametersDao();
    public abstract SettlementsDao settlementsDao();
    public abstract PersonsDao personsDao();
}
