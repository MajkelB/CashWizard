package eu.pp.cashwizard.dict;

public enum AnswerType {
    YES("Tak","Yes" ),
    NO("Nie","No" ),
    OK("Ok","Ok" );

    private String description;
    private String englishDescription;

    private AnswerType(String description, String englishDescription ) {
        this.description = description;
        this.englishDescription = englishDescription;
    }

    public String getName() { return name(); }
    public String getDescription() {
        return description;
    }
    public String getEnglishDescription() {
        return englishDescription;
    }

    public static AnswerType decodeDescription(String desc )
    {
        if( desc == null ) return null;

        for( AnswerType at: AnswerType.values() ) {
            if( desc.compareTo( at.getDescription() ) == 0 ) return at;
        }

        return null;
    }

    public static AnswerType decode(String desc )
    {
        if( desc == null ) return null;

        for( AnswerType at: AnswerType.values() ) {
            if( desc.compareTo( at.getDescription() ) == 0 ) return at;
        }

        return null;
    }

    public static AnswerType decodeEnglishDescription(String desc )
    {
        if( desc == null ) return null;

        for( AnswerType at: AnswerType.values() ) {
            if( desc.compareTo( at.getEnglishDescription() ) == 0 ) return at;
        }

        return null;
    }

    public static int getPosition( AnswerType s ) {
        int i=0;
        for( AnswerType at: AnswerType.values() ) {
            if (at.equals(s)) return i;
            i++;
        }
        return 0;
    }

    public boolean isYes() {
        if( name().equals( "YES" ) ) return true;
        return false;
    }

    public boolean isNo() {
        if( name().equals( "NO" ) ) return true;
        return false;
    }

    public boolean isOk() {
        if( name().equals( "OK" ) ) return true;
        return false;
    }

}

