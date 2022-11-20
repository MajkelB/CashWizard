package eu.pp.cashwizard.dict;

public enum Operation {
    GET, UPDATE, INSERT, DELETE, GET_ALL;

    public static Operation decode( String desc )
    {
        if( desc == null ) return null;

        for( Operation o: Operation.values() ) {
            if( desc.compareTo( o.name() ) == 0 ) return o;
        }
        return null;
    }

};

