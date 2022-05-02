package eu.pp.cashwizard.data.settlement;

import android.content.Context;

import androidx.lifecycle.Observer;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import java.util.List;

import eu.pp.cashwizard.data.DBClient;
import eu.pp.cashwizard.data.parameter.ParameterWorker;
import eu.pp.cashwizard.dict.Operation;
import eu.pp.cashwizard.dict.Result;
import eu.pp.cashwizard.model.Parameter;
import eu.pp.cashwizard.model.Settlement;
import eu.pp.cashwizard.tech.DBOperationData;
import eu.pp.cashwizard.tech.ResultCallbackI;
import eu.pp.cashwizard.util.AUtil;
import eu.pp.cashwizard.util.JUtil;

public class SettlementsHelper {

    private static SettlementsDao mSettlementsDao;
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

    private static SettlementsDao getSettlementsDao() {
        if( mSettlementsDao == null )
            mSettlementsDao = DBClient.getInstance( mCtx ).getAppDatabase().settlementsDao();
        return mSettlementsDao;
    }

    public static Settlement getSettlement( long id ) { return getSettlementsDao().getSettlementById( id ); }
    public static void getSettlementAsync( long id ) {
        getSettlementAsync( id, null );
    }
    public static void getSettlementAsync( long id, ResultCallbackI resultReceiver) {
        asyncOperation( new DBOperationData<>( Operation.GET, id ), resultReceiver );
    }
    public static void saveSettlement( Settlement s ) {
        getSettlementsDao().insert( s );
    }
    public static void saveSettlementAsync( Settlement s ) {
        saveSettlementAsync( s, null );
    }
    public static void saveSettlementAsync( Settlement s, ResultCallbackI resultReceiver ) {
        asyncOperation( new DBOperationData<>( Operation.INSERT, s ), resultReceiver );
    }
    public static void updateSettlement( Settlement s ) {
        getSettlementsDao().update( s );
    }
    public static void updateSettlementAsync( Settlement s ) {
        updateSettlementAsync( s, null );
    }
    public static void updateSettlementAsync( Settlement s, ResultCallbackI resultReceiver ) {
        asyncOperation( new DBOperationData<>( Operation.UPDATE, s ), resultReceiver );
    }
    public static void deleteSettlement(Settlement s ) {
        getSettlementsDao().delete( s );
    }
    public static void deleteSettlementAsync( Settlement s ) {
        deleteSettlementAsync( s, null );
    }
    public static void deleteSettlementAsync( Settlement s, ResultCallbackI resultReceiver ) {
        asyncOperation( new DBOperationData<>( Operation.DELETE, s ), resultReceiver );
    }
    public static List<Settlement> getAllSettlements() {
        return getSettlementsDao().getAll();
    }
    public static void getAllSettlementsAsync( String id ) {
        getAllSettlementsAsync( id, null );
    }
    public static void getAllSettlementsAsync(String id, ResultCallbackI resultReceiver) {
        asyncOperation( new DBOperationData<>( Operation.GET_ALL, id ), resultReceiver );
    }

    public static void asyncOperation( DBOperationData<Settlement> data, ResultCallbackI resultReceiver ) {

        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder( SettlementsWorker.class ).setInputData( data.toWorkData() ).build();
        mWorkManager.enqueue( workRequest );

        mWorkManager.getWorkInfoByIdLiveData( workRequest.getId()).observe(  null, new Observer<WorkInfo>() {
            @Override
            public void onChanged(WorkInfo workInfo) {
                if( resultReceiver != null ) {
                    if( workInfo.getState().isFinished() ) {
                        resultReceiver.receiveResult(Result.OK, DBOperationData.fromWorkData( workInfo.getOutputData() ) );
                    }
                    else resultReceiver.receiveProgress( workInfo.getProgress().getInt( "PROGRESS", 0 ) );
                }
            }
        });
    }

    public static void printSettlements() {
        AUtil.logI( "**************************** Settlements ****************************" );
        for( Settlement s: JUtil.safeList( getAllSettlements() ) ) {
            AUtil.logI( s.toString() );
        }
        AUtil.logI( "********************************************************************" );
    }
}
