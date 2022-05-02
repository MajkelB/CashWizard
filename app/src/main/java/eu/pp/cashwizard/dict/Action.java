package eu.pp.cashwizard.dict;

public enum Action {
	ADD, UPDATE, REMOVE, CANCEL, NEW, BACK, UNKNOWN;

	public static Action decode( String str )
	{
		if( str == null ) return Action.UNKNOWN;

		for( Action a: Action.values() ) {
			if( str.compareTo( a.name() ) == 0 ) return a;
		}

		return Action.UNKNOWN;
	}
}

