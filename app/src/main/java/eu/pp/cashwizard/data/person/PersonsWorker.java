package eu.pp.cashwizard.data.person;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import eu.pp.cashwizard.data.parameter.ParametersHelper;
import eu.pp.cashwizard.model.Parameter;
import eu.pp.cashwizard.model.Person;
import eu.pp.cashwizard.tech.DBOperationData;
import eu.pp.cashwizard.util.AUtil;

public class PersonsWorker extends Worker {

    DBOperationData<Person> mOperation;
    private static final String PROGRESS = "PROGRESS";

    public PersonsWorker(@NonNull Context context, @NonNull WorkerParameters workerParams ) {
        super(context, workerParams);
        mOperation = DBOperationData.fromWorkData( workerParams.getInputData() );
    }

    @NonNull
    @Override
    public Result doWork() {
        Parameter p;
        //Result result = new Result();
        setProgressAsync(new Data.Builder().putInt(PROGRESS, 0).build());
        if( mOperation.isInsert() ) PersonsHelper.savePerson( (Person) mOperation.getSingleObject() );
        if( mOperation.isUpdate() ) PersonsHelper.updatePerson( (Person) mOperation.getSingleObject() );
        if( mOperation.isDelete() ) PersonsHelper.deletePerson( (Person) mOperation.getSingleObject() );
        if( mOperation.isGet() ) {
            p = ParametersHelper.getParameter( mOperation.getName() );
        }
        setProgressAsync(new Data.Builder().putInt(PROGRESS, 100).build());
        AUtil.logI( "Person job done: " + mOperation.getOperationName()  );
        return Result.success();
    }

}
