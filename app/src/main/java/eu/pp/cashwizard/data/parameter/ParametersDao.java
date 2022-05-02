package eu.pp.cashwizard.data.parameter;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.TypeConverters;
import androidx.room.Update;

import java.util.List;

import eu.pp.cashwizard.data.converters.DateConverter;
import eu.pp.cashwizard.model.Parameter;

@Dao
@TypeConverters(DateConverter.class)
public interface ParametersDao {

    @Query("SELECT * FROM Parameters")
    List<Parameter> getAll();

    @Query("SELECT * FROM Parameters WHERE name = :name")
    Parameter getParameterByName( String name );

    @Insert( onConflict = OnConflictStrategy.REPLACE )
    void insert( Parameter p );

    @Update
    void update( Parameter p );

    @Delete
    void delete( Parameter p );

}
