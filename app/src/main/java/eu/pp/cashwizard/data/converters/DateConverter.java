package eu.pp.cashwizard.data.converters;

import androidx.room.TypeConverter;

import java.util.Date;
import java.util.Optional;

public class DateConverter {

    @TypeConverter
    public static Date toDate( Long timestamp ) {
        return timestamp == null ? null: new Date( timestamp );
    }

    @TypeConverter
    public static Long toTimestamp( Date date ) {
        return date == null ? null : date.getTime();
//        return Optional.of( date.getTime() ).orElse( null );
    }

}
