/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.pp.cashwizard.model;

import java.math.BigDecimal;
import java.util.Date;

import eu.pp.cashwizard.dict.Currency;
import eu.pp.cashwizard.util.JUtil;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Majkel
 */
@Getter
@Setter
public class Payment {
    
    Cash cash;
    Date date;
    Person person;

    public Payment(Cash cash, Date date, Person person ) {
        this.cash = cash;
        this.date = date;
        this.person = person;
    }
    
    public boolean is4Person( Person p ) {
        if( p == null ) return false;
        if( person != null && person.equals(p) ) return true;
        return false;
    }

    public String toString() {
        return JUtil.getDate( date ) + " " + person.getNickName() + " " + cash.toString();
    }
    
    
}
