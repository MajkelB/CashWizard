package eu.pp.cashwizard.dict;

public enum YesNo {
    Y("Tak","Yes",true), N("Nie","No",false);

    private String description;
    private String englishDescription;
    private boolean booleanValue;

    private YesNo( String description, String englishDescription, boolean booleanValue ) {
        this.description = description;
        this.englishDescription = englishDescription;
        this.booleanValue = booleanValue;
    }

    public String getName() { return name(); }
    public String getDescription() {
        return description;
    }
    public String getEnglishDescription() {
        return englishDescription;
    }
    public boolean isTrue() { return booleanValue; }

    public static YesNo decodeDescription( String desc )
    {
        if( desc == null ) return null;

        for( YesNo yesNo: YesNo.values() ) {
            if( desc.compareTo( yesNo.getDescription() ) == 0 ) return yesNo;
        }

        return null;
    }

    public static YesNo decode( String desc )
    {
        if( desc == null ) return null;

        for( YesNo yesNo: YesNo.values() ) {
            if( desc.compareTo( yesNo.getName() ) == 0 ) return yesNo;
        }

        return null;
    }

    public static YesNo decodeEnglishDescription( String desc )
    {
        if( desc == null ) return null;

        for( YesNo yesNo: YesNo.values() ) {
            if( desc.compareTo( yesNo.getEnglishDescription() ) == 0 ) return yesNo;
        }

        return null;
    }

    public static YesNo decode( boolean val ) {
        if( val ) return YesNo.Y;
        else return YesNo.N;
    }

}

