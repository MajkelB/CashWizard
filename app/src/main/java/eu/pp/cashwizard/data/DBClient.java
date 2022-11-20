package eu.pp.cashwizard.data;

import android.content.Context;
import android.graphics.Path;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.Optional;

import eu.pp.cashwizard.configuration.Conf;

public class DBClient {

    private static Context mCtx;
    private static DBClient mInstance;

    private static DB mDB;

    private DBClient( Context ctx ) {
        mCtx = ctx;
        mDB = Room.databaseBuilder( ctx, DB.class, Conf.DB_FILE_NAME ).addMigrations( MIGRATION_1_2 ).build();
//        mDB = Room.databaseBuilder( ctx, DB.class, Conf.DB_FILE_NAME ).addMigrations( MIGRATION_0_1 ).build();
    }

//    public static void init( Context ctx ) {
//
//    }

    public static synchronized DBClient getInstance( Context ctx) {
        if( mInstance == null ) {
            mInstance = new DBClient(ctx);
        }
        return mInstance;
    }

    public static DB getAppDatabase() {
        return mDB;
    }

    private static Migration MIGRATION_0_1 = new Migration(0,1) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
        }
    };

    private static Migration MIGRATION_1_2 = new Migration(1,2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            //mCtx.deleteDatabase(Conf.DB_FILE_NAME );
        }
    };

}
