package eu.pp.cashwizard.dict;

public enum Sex {
    MAN("Mężczyzna","Man" ), WOMAN("Kobieta","Woman" );

    private String description;
    private String englishDescription;

    private Sex(String description, String englishDescription ) {
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

    public boolean isMale() {
        if( this == Sex.MAN ) return true;
        else return false;
    }

    public boolean isFemale() {
        if( this == Sex.WOMAN ) return true;
        else return false;
    }

    public static Sex decodeDescription(String desc )
    {
        if( desc == null ) return null;

        for( Sex s: Sex.values() ) {
            if( desc.compareTo( s.getDescription() ) == 0 ) return s;
        }

        return null;
    }

    public static Sex decode(String desc )
    {
        if( desc == null ) return null;

        for( Sex s: Sex.values() ) {
            if( desc.compareTo( s.getDescription() ) == 0 ) return s;
        }

        return null;
    }

    public static Sex decodeEnglishDescription(String desc )
    {
        if( desc == null ) return null;

        for( Sex s: Sex.values() ) {
            if( desc.compareTo( s.getEnglishDescription() ) == 0 ) return s;
        }

        return null;
    }

    public static int getPosition( Sex s ) {
        int i=0;
        for( Sex sex: Sex.values() ) {
            if (sex.equals(s)) return i;
            i++;
        }
        return 0;
    }

}

