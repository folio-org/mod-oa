package org.olf.oa

import grails.gorm.MultiTenant

import org.olf.oa.finance.MonetaryValue
import org.olf.oa.finance.ExchangeRate

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

class Charge implements MultiTenant<Charge> {
  private static BigDecimal ONE_HUNDRED = new BigDecimal(100L)
  private static BigDecimal ONE = new BigDecimal(1L)

  String id

  MonetaryValue amount // The amount entered by the user in a defined currency
  ExchangeRate exchangeRate
  /* The idea here is that we might want to calculate localAmount not from static rate but from some API */

  String description

  @CategoryId(defaultInternal=false)
  @Defaults(['APC', 'BPC'])
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

  static transients = ['estimatedInvoicePrice', 'estimatedPrice']

  @CategoryId(defaultInternal=true)
  @Defaults(['Library', 'DFG', 'Author'])
  RefdataValue payer

  String discountNote
  BigDecimal tax
  String payerNote

  @CategoryId(defaultInternal=true)
  @Defaults(['Expected', 'Invoiced'])
  RefdataValue chargeStatus

  BigDecimal getTaxMultiplicand() {
    return tax.divide(ONE_HUNDRED).add(ONE)
  }

  //TODO add Payer after user consultation, whether controlled field or free text
  MonetaryValue getEstimatedInvoicePrice() {
    MonetaryValue estimatedPrice
    BigDecimal value
    
    if (discountType.value == 'percentage') {
      // PERCENTAGE DISCOUNT
      // Value = (value - (value*(discount/100)))*(1+(tax/100))
      value = amount.value.subtract((amount.value.multiply(discount.divide(ONE_HUNDRED)))).multiply(getTaxMultiplicand())
    } else {
      // STATIC DISCOUNT
      // Value = (value - discount)*(1+(tax/100))
      value = amount.value.subtract(discount).multiply(getTaxMultiplicand())
    }
    
    estimatedPrice = new MonetaryValue([
      value: value,
      baseCurrency: amount?.baseCurrency
    ])
    
    estimatedPrice
  }

  MonetaryValue getEstimatedPrice(ExchangeRate overwriteExchangeRate = null) {
    MonetaryValue estimatedPrice
    ExchangeRate finalExchangeRate = overwriteExchangeRate ?: (
      exchangeRate.coefficient ? exchangeRate : null
    )

    if (finalExchangeRate && finalExchangeRate.coefficient) {
      if (finalExchangeRate.fromCurrency == amount.baseCurrency) {
        estimatedPrice = new MonetaryValue([
          value: getEstimatedInvoicePrice().value.multiply(finalExchangeRate.coefficient),
          baseCurrency: finalExchangeRate?.toCurrency
        ])

      } else {
        throw new RuntimeException("Cannot exchange currencies. Amount baseCurrency (${amount.baseCurrency}) is not equal to exchangeRate fromCurrency (${finalExchangeRate.fromCurrency})")
      }
    } else {
      throw new RuntimeException("No exchange rate provided")
    }

    estimatedPrice
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
                       payer column: 'ch_payer_fk'
                discountNote column: 'ch_discount_note'
                         tax column: 'ch_tax'
                   payerNote column: 'ch_payer_note'
                chargeStatus column: 'ch_charge_status_fk'
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
                       payer(nullable: true)
                discountNote(nullable: true)
                         tax(nullable: true)
                   payerNote(nullable: true)
                chargeStatus(nullable: true)
  }

}
