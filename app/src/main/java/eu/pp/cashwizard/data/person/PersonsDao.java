package eu.pp.cashwizard.data.person;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.TypeConverters;
import androidx.room.Update;

import java.util.List;

import eu.pp.cashwizard.data.converters.DateConverter;
import eu.pp.cashwizard.model.Person;

@Dao
@TypeConverters(DateConverter.class)
public interface PersonsDao {

    @Query("SELECT * FROM Person")
    List<Person> getAll();

    @Query("SELECT * FROM Person WHERE id = :id")
    Person getPersonById( Long id );

    @Insert( onConflict = OnConflictStrategy.REPLACE )
    void insert( Person p );

    @Update
    void update( Person p );

    @Delete
    void delete( Person p );

}
