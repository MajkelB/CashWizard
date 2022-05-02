package eu.pp.cashwizard.data.settlement;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.TypeConverters;
import androidx.room.Update;

import java.util.List;

import eu.pp.cashwizard.data.converters.DateConverter;
import eu.pp.cashwizard.model.Settlement;

//@TypeConverters(DateConverter.class)
@Dao
public interface SettlementsDao {

    @Query("SELECT * FROM Settlements")
    List<Settlement> getAll();

    @Query("SELECT * FROM Settlements WHERE id = :id")
    Settlement getSettlementById( long id );

    @Insert( onConflict = OnConflictStrategy.REPLACE )
    void insert( Settlement s );

    @Update
    void update( Settlement s );

    @Delete
    void delete( Settlement s );

}
