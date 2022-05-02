package eu.pp.cashwizard.tech;

import androidx.work.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.pp.cashwizard.dict.Operation;
import eu.pp.cashwizard.model.Parameter;

public class DBOperationData<T> {
    Operation operation;
    long id;
    String name;
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

    public DBOperationData( Operation operation, String name) {
        this.operation = operation;
        this.name = name;
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
        dataMap.put( "operation" , operation );
        if( operation.equals( Operation.GET ) || operation.equals( Operation.DELETE ) ) dataMap.put( "name", name );
        if( operation.equals( Operation.UPDATE ) || operation.equals( Operation.INSERT ) ) dataMap.put( "singleObject", singleObject );
        if( operation.equals( Operation.GET_ALL ) ) dataMap.put( "listOfObjects", singleObject );
        androidx.work.Data workData = new androidx.work.Data.Builder().putAll( dataMap ).build();
        return workData;
    }

    public static DBOperationData fromWorkData( Data data ) {
        Map<String, Object> dataMap = data.getKeyValueMap();
        Operation operation = (Operation) dataMap.get( "operation" );
        DBOperationData operationData = null;
        switch ( operation ) {
            case GET:
                Parameter parameter = (Parameter) dataMap.get( "singleObject" );
                operationData = new DBOperationData( operation, parameter );
                break;
            case GET_ALL:
                List<Parameter> parameters = (List<Parameter>) dataMap.get( "listOfObjects" );
                operationData = new DBOperationData( operation, parameters );
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
