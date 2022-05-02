package eu.pp.cashwizard.data;

import android.content.Context;

import androidx.annotation.NonNull;
//import androidx.work.Worker;
//import androidx.work.WorkerParameters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import eu.pp.cashwizard.data.settlement.SettlementsHelper;

public class DBWorker { //extends Worker {


    ExecutorService executorService = new ThreadPoolExecutor(1, 1, 1L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>() );

//    public DBWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
//        super(context, workerParams);
//    }


//    public void doJob() {
//
//        executorService.execute(() -> {
////            SettlementsHelper.saveSettlement( DataRepository.getSettlement() );
//        });
//
//    }

//    @NonNull
//    @Override
//    public Result doWork() {
//        return null;
//    }

//    executorService.shutdown();

}
