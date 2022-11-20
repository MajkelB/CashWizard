package eu.pp.cashwizard.tech;

import android.util.JsonWriter;

import androidx.work.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.pp.cashwizard.dict.Operation;
import eu.pp.cashwizard.model.JSONConvertable;
import eu.pp.cashwizard.util.JUtil;

public class DBOperationData<T extends JSONConvertable> extends JSONConvertable {
    Operation operation;
    long id;
    String key;
    Date date;
    T singleObject;
    List<T> listOfObjects = new ArrayList<>();

    public DBOperationData() {
    }

    public DBOperationData( Operation operation ) {
        this.operation = operation;
    }

    public DBOperationData( Operation operation, long id ) {
        this.operation = operation;
        this.id = id;
    }
    public DBOperationData( Operation operation, Long id ) {
        this.operation = operation;
        this.id = id;
    }

    public DBOperationData( Operation operation, String key) {
        this.operation = operation;
        this.key = key;
    }

    public DBOperationData(Operation operation, T singleObject) {
        this.operation = operation;
        this.singleObject = singleObject;
    }

    public DBOperationData(Operation operation, List<T> listOfObjects) {
        this.operation = operation;
        this.listOfObjects = listOfObjects;
    }

    public Data toWorkData() {
        Map<String, Object> dataMap = new HashMap<>();
//        dataMap.put( "operation", operation.name() );
//        if( operation.equals( Operation.GET ) || operation.equals( Operation.DELETE ) ) dataMap.put( "kay", key );
//        if( operation.equals( Operation.UPDATE ) || operation.equals( Operation.INSERT ) ) dataMap.put( "singleObject", (String) singleObject.toJson() );
//        if( operation.equals( Operation.GET_ALL ) ) dataMap.put( "listOfObjects", (String) JUtil.toJson4List( listOfObjects ) );
//        androidx.work.Data workData = new androidx.work.Data.Builder().putAll( dataMap ).build();
        dataMap.put( "data", this.toJson() );
        androidx.work.Data workData = new androidx.work.Data.Builder().putAll( dataMap ).build();
        return workData;
    }

    public DBOperationData fromWorkData( Data data ) {
        Map<String, Object> dataMap = data.getKeyValueMap();
        Operation operation = Operation.decode( (String) dataMap.get( "operation" ) );
        DBOperationData<T> operationData = null;
        switch ( operation ) {
            case GET:
                T obj = (T) dataMap.get( "singleObject" );
                operationData = new DBOperationData( operation, obj );
                break;
            case GET_ALL:
                List<T> objects = (List<T>) dataMap.get( "listOfObjects" );
                operationData = new DBOperationData( operation, objects );
                break;
            case INSERT: break;
            case UPDATE: break;
            case DELETE: break;
            default:break;
        }
        return operationData;
    }

    public boolean isInsert() {
        if( operation.equals( Operation.INSERT ) ) return true;
        return false;
    }

    public boolean isUpdate() {
        if( operation.equals( Operation.UPDATE ) ) return true;
        return false;
    }

    public boolean isDelete() {
        if( operation.equals( Operation.DELETE ) ) return true;
        return false;
    }

    public boolean isGet() {
        if( operation.equals( Operation.GET ) ) return true;
        return false;
    }

    public boolean isGetAll() {
        if( operation.equals( Operation.GET_ALL ) ) return true;
        return false;
    }

    public String getOperationName() {
        if( operation == null ) return "xxx";
        return operation.name();
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public T getSingleObject() {
        return singleObject;
    }

    public void setSingleObject(T singleObject) {
        this.singleObject = singleObject;
    }

    public List<T> getListOfObjects() {
        return listOfObjects;
    }

    public void setListOfObjects(List<T> listOfObjects) {
        this.listOfObjects = listOfObjects;
    }

    public String toJson() {
        return this.toJson();
    }

    public DBOperationData<T> fromJson2This( Class<DBOperationData<T>> clazz, String json ) {
        DBOperationData<T> operationData = JUtil.fromJson( clazz, json );
        this.operation = operationData.operation;
        this.id = operationData.id;
        this.key = operationData.key;
        this.date = operationData.date;
        this.singleObject = operationData.singleObject;
        this.listOfObjects = operationData.listOfObjects;
        return operationData;
    }

}
