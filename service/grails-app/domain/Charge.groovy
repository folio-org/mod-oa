package org.olf.oa

import grails.gorm.MultiTenant

import org.olf.oa.finance.MonetaryValue

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

class Charge implements MultiTenant<Charge> {

  String id

  MonetaryValue amount // The amount entered by the user in a defined currency
  BigDecimal exchangeRate = BigDecimal.ZERO // 0 treated as non existent NUMBER(5,10) maybe level of precision?
  /* The idea here is that we might want to calculate localAmount not from static rate but from some API */

  String description

  @CategoryId(defaultInternal=false)
  @Defaults(['APC'])
  RefdataValue category

  BigDecimal discount
  @CategoryId(defaultInternal=true)
  @Defaults(['percentage', 'subtracted'])
  RefdataValue discountType

  PublicationRequest owner

  static transients = ['localAmount']

  //TODO add Payer after user consultation, whether controlled field or free text

  MonetaryValue getLocalAmount(String localCurrencyCode, BigDecimal overwriteExchangeRate = BigDecimal.ZERO) {
    BigDecimal finalExchangeRate = overwriteExchangeRate ?: exchangeRate;

    if (finalExchangeRate) {
      MonetaryValue localAmount = new MonetaryValue([value: amount.value.multiply(finalExchangeRate)])
      localAmount.setBaseCurrency(localCurrencyCode)
    } else {
      throw new RuntimeException("No exchange rate provided")
    }
    
    localAmount
  }

  
  static mapping = {
               id column: 'ch_id', generator: 'uuid2', length: 36
           amount column: 'ch_amount_fk'
     exchangeRate column: 'ch_exchange_rate'
      description column: 'ch_description'
         category column: 'ch_category_fk'
         discount column: 'ch_discount'
     discountType column: 'ch_discount_type_fk'
            owner column: 'ch_owner_fk'
  }
  
  static constraints = {
          amount(nullable: false)
    exchangeRate(nullable: true)
     description(nullable: true)
        category(nullable: true)
        discount(nullable: true)
    discountType(nullable: true)

  }

}