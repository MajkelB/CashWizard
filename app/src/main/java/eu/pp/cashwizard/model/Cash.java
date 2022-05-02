/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.pp.cashwizard.model;

import eu.pp.cashwizard.dict.Currency;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Majkel
 */
@Getter
@Setter
public class Cash {
    
    BigDecimal amount;
    Currency currency;

    public Cash(BigDecimal amount, Currency currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public String toString() {
        return "" + amount + " " + currency;
    }
}
