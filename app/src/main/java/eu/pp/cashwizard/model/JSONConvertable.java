package eu.pp.cashwizard.model;

import eu.pp.cashwizard.util.JUtil;

public class JSONConvertable {

    public String toJson() {
        return JUtil.toJson( this );
    }

    public <T> T fromJson(  Class<T> clazz, String s ) { return JUtil.fromJson( clazz, s ); }
}
