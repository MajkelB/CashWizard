package eu.pp.cashwizard.dict;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public enum Currency {
	EUR, USD, PLN, GBP, NOK, XXX;

	public static Currency decode( String str ) {
		if( str == null ) return Currency.XXX;
		for( Currency c: Currency.values() ) {
			if( c.name().equals( str ) ) return c;
		}
		return Currency.XXX;
	}

	public static Currency[] getVisibleValues() {
		return ( Currency[] ) EnumSet.allOf( Currency.class ).stream().filter( i -> !i.equals( Currency.XXX ) ).toArray();
	}

	public static int getPosition( Currency c ) {
		int i=0;
		for( Currency currency: Currency.values() ) {
			if (currency.equals(c)) return i;
			i++;
		}
		return 0;
	}
}

