package eu.pp.cashwizard.dict;

public enum Result {
	OK, ERROR, UNKNOWN;

	public static Result decode(String str ) {
		if( str == null ) return Result.UNKNOWN;
		for( Result c: Result.values() ) {
			if( c.name().equals( str ) ) return c;
		}
		return Result.UNKNOWN;
	}
}

