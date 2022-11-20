package eu.pp.cashwizard.data.person;

import android.content.Context;

import androidx.lifecycle.Observer;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import java.util.List;

import eu.pp.cashwizard.data.DBClient;
import eu.pp.cashwizard.dict.Operation;
import eu.pp.cashwizard.dict.Result;
import eu.pp.cashwizard.model.Person;
import eu.pp.cashwizard.tech.DBOperationData;
import eu.pp.cashwizard.tech.ResultCallbackI;
import eu.pp.cashwizard.util.AUtil;
import eu.pp.cashwizard.util.JUtil;

public class PersonsHelper {

    private static PersonsDao mPersonsDao;
    private static Context mCtx;

    private static WorkManager mWorkManager = null;

    public static boolean init( Context ctx ) {
        if( ctx != null ) {
            mCtx = ctx;
            mWorkManager = WorkManager.getInstance( mCtx );
            return true;
        } else {
            return false;
        }
    }

    private static PersonsDao getPersonsDao() {
        if( mPersonsDao == null )
            mPersonsDao = DBClient.getInstance( mCtx ).getAppDatabase().personsDao();
        return mPersonsDao;
    }

    public static Person getPerson(Long id ) {
        return getPersonsDao().getPersonById( id );
    }
    public static void getPersonAsync( Long id, ResultCallbackI resultReceiver ) {
        asyncOperation( new DBOperationData<>( Operation.GET, id ), resultReceiver );
    }

    public static List<Person> getAllPersons() {
        return getPersonsDao().getAll();
    }

    public static void getAllPersonsAsync( String name, ResultCallbackI resultReceiver ) {
        asyncOperation( new DBOperationData<>( Operation.GET_ALL ), resultReceiver );
    }

    public static void savePerson( Person p ) {
        getPersonsDao().insert( p );
    }
    public static void savePersonAsync( Person p ) {
        savePersonAsync( p, null );
    }
    public static void savePersonAsync( Person p, ResultCallbackI resultReceiver ) {
        asyncOperation( new DBOperationData<>( Operation.INSERT, p ), resultReceiver );
    }

    public static void updatePerson( Person p ) {
        getPersonsDao().update( p );
    }
    public static void updatePersonAsync( Person p ) {
        updatePersonAsync( p, null );
    }
    public static void updatePersonAsync( Person p, ResultCallbackI resultReceiver ) {
        asyncOperation( new DBOperationData<>( Operation.UPDATE, p ), resultReceiver );
    }

    public static void deletePerson( Person p ) {
        getPersonsDao().delete( p );
    }
    public static void deletePersonAsync( Person p ) {
        deletePersonAsync( p, null);
    }
    public static void deletePersonAsync( Person p, ResultCallbackI resultReceiver ) {
        asyncOperation( new DBOperationData<>( Operation.DELETE, p ), resultReceiver );
    }

    public static void asyncOperation( DBOperationData<Person> data, ResultCallbackI resultReceiver ) {

        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder( PersonsWorker.class ).setInputData( data.toWorkData() ).build();
        mWorkManager.enqueue( workRequest );

        mWorkManager.getWorkInfoByIdLiveData( workRequest.getId()).observe(  null, new Observer<WorkInfo>() {
            @Override
            public void onChanged(WorkInfo workInfo) {
                if( resultReceiver != null ) {
                    if( workInfo.getState().isFinished() ) {
                        resultReceiver.receiveResult(Result.OK, new DBOperationData().fromWorkData( workInfo.getOutputData() ) );
                    }
                    else resultReceiver.receiveProgress( workInfo.getProgress().getInt( "PROGRESS", 0 ) );
                }
            }
        });
    }

    public static void pringPersons() {
        AUtil.logI( "**************************** Parameters ****************************" );
        for( Person p: JUtil.safeList( getAllPersons() ) ) {
            AUtil.logI( p.toString() );
        }
        AUtil.logI( "********************************************************************" );
    }
}
