package eu.pp.cashwizard.data.parameter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import eu.pp.cashwizard.dict.Operation;
import eu.pp.cashwizard.model.Parameter;
import eu.pp.cashwizard.tech.DBOperationData;
import eu.pp.cashwizard.util.AUtil;

public class ParameterWorker extends Worker {

    DBOperationData<Parameter> mOperation;
    private static final String PROGRESS = "PROGRESS";

    public ParameterWorker(@NonNull Context context, @NonNull WorkerParameters workerParams ) {
        super(context, workerParams);
        mOperation = DBOperationData.fromWorkData( workerParams.getInputData() );
    }

    @NonNull
    @Override
    public Result doWork() {
        Parameter p;
        //Result result = new Result();
        setProgressAsync(new Data.Builder().putInt(PROGRESS, 0).build());
        if( mOperation.isInsert() ) ParametersHelper.saveParameter( (Parameter) mOperation.getSingleObject() );
        if( mOperation.isUpdate() ) ParametersHelper.updateParameter( (Parameter) mOperation.getSingleObject() );
        if( mOperation.isDelete() ) ParametersHelper.deleteParameter( (Parameter) mOperation.getSingleObject() );
        if( mOperation.isGet() ) {
            p = ParametersHelper.getParameter( mOperation.getName() );
        }
        setProgressAsync(new Data.Builder().putInt(PROGRESS, 100).build());
        AUtil.logI( "Parameter job done: " + mOperation.getOperationName()  );
        return Result.success();
    }

}
