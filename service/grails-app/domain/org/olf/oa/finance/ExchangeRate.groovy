package org.olf.oa.finance

import grails.gorm.MultiTenant

class ExchangeRate implements MultiTenant<ExchangeRate> {
  String id

  Currency fromCurrency
  Currency toCurrency
  BigDecimal coefficient = BigDecimal.ZERO
  
  static mapping = {
    columns:{
                  id column:'id', generator: 'uuid2', length: 36
      'fromCurrency' column:'from_currency'
        'toCurrency' column:'to_currency'
       'coefficient' column:'coefficient'

    }
  }

  static constraints = {
    fromCurrency(nullable: false)
    toCurrency(nullable: false)
    coefficient(nullable: false, blank:false)
  }


}