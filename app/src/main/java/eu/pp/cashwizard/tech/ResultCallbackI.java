package eu.pp.cashwizard.tech;

import eu.pp.cashwizard.dict.Result;

public interface ResultCallbackI<T> {

    void receiveResult( Result result, DBOperationData<T> data );
    void receiveProgress( int progress );

}
