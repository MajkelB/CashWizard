package eu.pp.cashwizard.data.settlement;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.List;

import eu.pp.cashwizard.data.parameter.ParametersHelper;
import eu.pp.cashwizard.model.Parameter;
import eu.pp.cashwizard.model.Settlement;
import eu.pp.cashwizard.tech.DBOperationData;
import eu.pp.cashwizard.util.AUtil;

public class SettlementsWorker extends Worker {

    DBOperationData<Settlement> mOperation;
    private static final String PROGRESS = "PROGRESS";

    public SettlementsWorker(@NonNull Context context, @NonNull WorkerParameters workerParams ) {
        super(context, workerParams);
        mOperation = new DBOperationData<>().fromWorkData( workerParams.getInputData() );
    }

    @NonNull
    @Override
    public Result doWork() {
        Settlement s;
        List<Settlement> settlementList;
        //Result result = new Result();
        setProgressAsync(new Data.Builder().putInt(PROGRESS, 0).build());
        if( mOperation.isInsert() ) SettlementsHelper.saveSettlement( (Settlement) mOperation.getSingleObject() );
        if( mOperation.isUpdate() ) SettlementsHelper.updateSettlement( (Settlement) mOperation.getSingleObject() );
        if( mOperation.isDelete() ) SettlementsHelper.deleteSettlement( (Settlement) mOperation.getSingleObject() );
        if( mOperation.isGet() ) {
            s = SettlementsHelper.getSettlement( mOperation.getId() );
        }
        if( mOperation.isGetAll() ) {
            settlementList = SettlementsHelper.getAllSettlements();
        }
        setProgressAsync(new Data.Builder().putInt(PROGRESS, 100).build());
        AUtil.logI( "Settlement job done: " + mOperation.getOperationName()  );
        return Result.success();
    }

}
