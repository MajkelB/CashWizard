package eu.pp.cashwizard.data.parameter;

import android.content.Context;

import androidx.lifecycle.Observer;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import java.util.List;

import eu.pp.cashwizard.data.DBClient;
import eu.pp.cashwizard.dict.Operation;
import eu.pp.cashwizard.dict.Result;
import eu.pp.cashwizard.model.Parameter;
import eu.pp.cashwizard.tech.DBOperationData;
import eu.pp.cashwizard.tech.ResultCallbackI;
import eu.pp.cashwizard.util.AUtil;
import eu.pp.cashwizard.util.JUtil;

public class ParametersHelper {

    private static ParametersDao mParametersDao;
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

    private static ParametersDao getParametersDao() {
        if( mParametersDao == null )
            mParametersDao = DBClient.getInstance( mCtx ).getAppDatabase().parametersDao();
        return mParametersDao;
        //return Optional.of( mParametersDao ).orElse(mParametersDao=DBClient.getInstance(mCtx).getAppDatabase().parametersDao());
    }

    public static Parameter getParameter( String name ) {
        return getParametersDao().getParameterByName( name );
    }
    public static void getParameterAsync( String name, ResultCallbackI resultReceiver ) {
        asyncOperation( new DBOperationData<>( Operation.GET, name ), resultReceiver );
    }

    public static List<Parameter> getAllParameters() {
        return getParametersDao().getAll();
    }

    public static void getAllParametersAsync( String name, ResultCallbackI resultReceiver ) {
        asyncOperation( new DBOperationData<>( Operation.GET_ALL ), resultReceiver );
    }

    public static void saveParameter( Parameter p ) {
        getParametersDao().insert( p );
    }
    public static void saveParameterAsync( Parameter p ) {
        saveParameterAsync( p, null );
    }
    public static void saveParameterAsync( Parameter p, ResultCallbackI resultReceiver ) {
        asyncOperation( new DBOperationData<>( Operation.INSERT, p ), resultReceiver );
    }

    public static void updateParameter( Parameter p ) {
        getParametersDao().update( p );
    }
    public static void updateParameterAsync( Parameter p ) {
        updateParameterAsync( p, null );
    }
    public static void updateParameterAsync( Parameter p, ResultCallbackI resultReceiver ) {
        asyncOperation( new DBOperationData<>( Operation.UPDATE, p ), resultReceiver );
    }

    public static void deleteParameter( Parameter p ) {
        getParametersDao().delete( p );
    }
    public static void deleteParameterAsync( Parameter p ) {
        deleteParameterAsync( p, null);
    }
    public static void deleteParameterAsync( Parameter p, ResultCallbackI resultReceiver ) {
        asyncOperation( new DBOperationData<>( Operation.DELETE, p ), resultReceiver );
    }

    public static void asyncOperation( DBOperationData<Parameter> data, ResultCallbackI resultReceiver ) {

        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder( ParametersWorker.class ).setInputData( data.toWorkData() ).build();
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

//    public static void test(Operation operation, DBOperationData<Parameter> data, ResultCallbackI resultReceiver, Context ctx ) {
//        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(ParameterWork.class).build();
//        mWorkManager.enqueue(workRequest);
//        //Lifecycle lf = null;
//        //Activity a = new Activity();
//        mWorkManager.getWorkInfoByIdLiveData(workRequest.getId()).observe( (LifecycleOwner) mCtx /*a.get*/, new Observer<WorkInfo>() {
//            @Override
//            public void onChanged(WorkInfo workInfo) {
//                resultReceiver.receiveResult(  );
//            }
//        });
//    }
//    public static void asyncOperation(Operation operation, DBOperationData<Parameter> data, ResultCallbackI resultReceiver ) {
//        new AsyncTask<Void, Void, Object>() {
//
//            @Override
//            protected Object doInBackground(Void... voids) {
//                switch ( operation ) {
//                    case GET: return getParametersDao().getParameterByName( data.getName() );
//                    case GET_ALL: return getParametersDao().getAll();
//                    case INSERT: getParametersDao().insert( data.getSingleObject() ); break;
//                    case UPDATE: getParametersDao().update( data.getSingleObject() ); break;
//                    case DELETE: getParametersDao().delete( data.getSingleObject() ); break;
//                }
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Object res) {
//                if( resultReceiver != null ) {
//                    List<Parameter> resultData = null;
//                    switch (operation) {
//                        case GET: resultReceiver.receiveResult(Result.OK, operation.name(), new DBOperationData<Parameter>( (Parameter) res) );
//                            break;
//                        case GET_ALL:
//                            resultReceiver.receiveResult(Result.OK, operation.name(), new DBOperationData<Parameter>( (List<Parameter>) res ) );
//                            break;
//                        case INSERT:
//                            resultReceiver.receiveResult(Result.OK, operation.name(), null );
//                            break;
//                        case UPDATE:
//                            resultReceiver.receiveResult(Result.OK, operation.name(), null );
//                            break;
//                        case DELETE:
//                            resultReceiver.receiveResult(Result.OK, operation.name(), null );
//                            break;
//                    }
//                }
//            }
//        }.execute();
//    }

    public static void printParameters() {
        AUtil.logI( "**************************** Parameters ****************************" );
        for( Parameter p: JUtil.safeList( getAllParameters() ) ) {
            AUtil.logI( p.getName() + " = " + p.getValue() );
        }
        AUtil.logI( "********************************************************************" );
    }
}
