package eu.pp.cashwizard.data.parameter;

import android.content.Context;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import eu.pp.cashwizard.dict.Operation;
import eu.pp.cashwizard.model.Parameter;
import eu.pp.cashwizard.tech.DBOperationData;
import eu.pp.cashwizard.util.AUtil;
import eu.pp.cashwizard.util.JUtil;

public class ParametersWorker extends Worker {

    DBOperationData<Parameter> mOperation;
    private static final String PROGRESS = "PROGRESS";

    public ParametersWorker(@NonNull Context context, @NonNull WorkerParameters workerParams ) {
        super(context, workerParams);
        mOperation = new DBOperationData<>().fromWorkData( workerParams.getInputData() );
    }

    @NonNull
    @Override
    public Result doWork() {

        //Result result = new Result();
        String resultData = null;
        setProgressAsync(new Data.Builder().putInt(PROGRESS, 0).build());
        if( mOperation.isInsert() ) ParametersHelper.saveParameter( (Parameter) mOperation.getSingleObject() );
        if( mOperation.isUpdate() ) ParametersHelper.updateParameter( (Parameter) mOperation.getSingleObject() );
        if( mOperation.isDelete() ) ParametersHelper.deleteParameter( (Parameter) mOperation.getSingleObject() );
        if( mOperation.isGet() ) {
            Parameter p = ParametersHelper.getParameter( mOperation.getKey() );
            resultData = ( p==null?null:p.toJson() );
        }
        if( mOperation.isGetAll() ) {
            List<Parameter> listOfParameters = ParametersHelper.getAllParameters();
            resultData = JUtil.toJson4List( listOfParameters );
        }
        setProgressAsync(new Data.Builder().putInt(PROGRESS, 100).putString( "DATA", resultData ).build());
        AUtil.logI( "Parameter job done: " + mOperation.getOperationName()  );
        return Result.success();
    }

}
