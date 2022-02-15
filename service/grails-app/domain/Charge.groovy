package org.olf.oa

import grails.gorm.MultiTenant

import org.olf.oa.finance.MonetaryValue
import org.olf.oa.finance.ExchangeRate

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

class Charge implements MultiTenant<Charge> {

  String id

  MonetaryValue amount // The amount entered by the user in a defined currency
  ExchangeRate exchangeRate
  /* The idea here is that we might want to calculate localAmount not from static rate but from some API */

  String description

  @CategoryId(defaultInternal=false)
  @Defaults(['APC'])
  RefdataValue category

  BigDecimal discount = BigDecimal.ZERO
  @CategoryId(defaultInternal=true)
  @Defaults(['percentage', 'subtracted'])
  RefdataValue discountType

  PublicationRequest owner

  // The UUID of a selected invoice pertaining to this charge
  String invoiceReference

  // The UUID of a selected invoice line item pertaining to this charge
  String invoiceLineItemReference

  static transients = ['localAmount']

  //TODO add Payer after user consultation, whether controlled field or free text

  MonetaryValue getLocalAmount(ExchangeRate overwriteExchangeRate = null) {
    MonetaryValue localAmount

    ExchangeRate finalExchangeRate = exchangeRate.coefficient ? exchangeRate : overwriteExchangeRate

    if (finalExchangeRate && finalExchangeRate.coefficient) {
      if (finalExchangeRate.fromCurrency == amount.baseCurrency) {
        localAmount = new MonetaryValue([
          value: amount.value.multiply(finalExchangeRate.coefficient),
          baseCurrency: finalExchangeRate?.toCurrency
        ])
      } else {
        throw new RuntimeException("Cannot exchange currencies. Amount baseCurrency (${amount.baseCurrency}) is not equal to exchangeRate fromCurrency (${finalExchangeRate.fromCurrency})")
      }
    } else {
      throw new RuntimeException("No exchange rate provided")
    }
    
    localAmount
  }

  def beforeValidate() {
    if (!exchangeRate.fromCurrency) {
      exchangeRate.fromCurrency = amount.baseCurrency
    }
  }

  
  static mapping = {
                          id column: 'ch_id', generator: 'uuid2', length: 36
                      amount column: 'ch_amount_fk'
                exchangeRate column: 'ch_exchange_rate_fk'
                 description column: 'ch_description'
                    category column: 'ch_category_fk'
                    discount column: 'ch_discount'
                discountType column: 'ch_discount_type_fk'
                       owner column: 'ch_owner_fk'
            invoiceReference column: 'ch_invoice_reference'
    invoiceLineItemReference column: 'ch_invoice_line_item_reference'
  }
  
  static constraints = {
                      amount(nullable: false)
                exchangeRate(nullable: true)
                 description(nullable: true)
                    category(nullable: true)
                    discount(nullable: true)
                discountType(nullable: true)
            invoiceReference(nullable: true)
    invoiceLineItemReference(nullable: true)
  }

}
