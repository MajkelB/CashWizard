package eu.pp.cashwizard.tech;

import eu.pp.cashwizard.dict.Result;
import eu.pp.cashwizard.model.JSONConvertable;

public interface ResultCallbackI<T extends JSONConvertable> {

    void receiveResult( Result result, DBOperationData<T> data );
    void receiveProgress( int progress );

}
