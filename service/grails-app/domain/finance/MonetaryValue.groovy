package org.olf.oa.finance

import grails.gorm.MultiTenant

import java.math.RoundingMode

import javax.money.CurrencyUnit
import javax.money.Monetary
import javax.money.CurrencyQuery
import javax.money.CurrencyQueryBuilder

import org.javamoney.moneta.Money

class MonetaryValue implements MultiTenant<MonetaryValue> {
  static transients = [ "money", "currencyUnit" ]

  String id

  Currency baseCurrency
  BigDecimal value = new BigDecimal("0.00");
  
  static mapping = {
    columns:{
                  id column:'id', generator: 'uuid2', length: 36
      'baseCurrency' column:'basecurrency'
             'value' column:'monval'
    }
  }

  static constraints = {
    baseCurrency(nullable: false)
           value(nullable: false, blank:false, scale:2)
  }

  public void add (String value) {
    if (value) {
      Money mv = parse(value)
      if (mv.currency && mv.currency.currencyCode != currencyUnit.currencyCode) {
        throw new IllegalArgumentException ("Can not add 2 monetary values in different currencies.")
      }
      
      this.value = this.value.add(mv.number)
      ensureScale()
    }
  }
  
  public void divide(String value) {
    if (value) {
      Money mv = parse(value)
      if (mv.currency && mv.currency.currencyCode != currencyUnit.currencyCode) {
        throw new IllegalArgumentException ("Can not divide 2 monetary values in different currencies.")
      }
      this.value = this.value.divide(mv.number)
      ensureScale()
    }
  }
  
  CurrencyUnit getCurrencyUnit() {
    CurrencyQuery query = CurrencyQueryBuilder.of().setCurrencyCodes(baseCurrency.currencyCode).build()
    return Monetary.getCurrency(query);
  }
  
  Money getMoney() {
    Money.of(value, currencyUnit)
  }
  
  public void multiply(String value) {
    if (value) {
      Money mv = parse(value)
      if (mv.currency && mv.currency.currencyCode != currencyUnit.currencyCode) {
        throw new IllegalArgumentException ("Can not multiply 2 monetary values in different currencies.")
      }
      this.value = this.value.multiply(mv.number)
      ensureScale()
    }
  }
  
  private Money parse(String value) {
    value = value.trim().toUpperCase()
    if (value =~ /^(\d|\.|\+|\-)/ ) {
      // Starts with a value only. Assume the same currency as this object
      value = "${baseCurrency.currencyCode} ${value}"
    }
    // Then just run through the Joda parser.
    Money.parse(value).withCurrencyScale(RoundingMode.HALF_UP)
  }
  
  public void setBaseCurrency (Currency baseCurrency) {
    this.baseCurrency = baseCurrency;
    ensureScale()
  }
  
  private void ensureScale () {
    value = value.setScale(currencyUnit.decimalPlaces, RoundingMode.HALF_UP)
  }
  
  public void setBaseCurrency (String currencyCode) {
    setBaseCurrency(Currency.getInstance(currencyCode))
  }

  public fromString (String str) {
    // Try and parse a monetary value from a string.
    Money m = parse(str)

    if (m) {
      this.baseCurrency = Currency.getInstance(m.getCurrency().code)
      this.value = m.number
    }
  }

  public void subtract (String value) {
    if (value) {
      Money mv = parse(value)
      if (mv.currency && mv.currency.currencyCode != currencyUnit.currencyCode) {
        throw new IllegalArgumentException ("Can not subtract 2 monetary values in different currencies.")
      }
      this.value = this.value.subtract(mv.number)
      ensureScale()
    }
  }
  
  public String toString() {
    formatter.print(money)
  }
}