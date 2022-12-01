package org.olf.oa

import grails.gorm.MultiTenant
import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

class Payer implements MultiTenant<Payer> {

  String id

  @CategoryId(defaultInternal=false)
  @Defaults(['Library', 'DFG', 'Author'])
  RefdataValue payer

  BigDecimal payerAmount

  String payerNote

  static belongsTo = [
    owner: Charge
  ]

  static mapping = {
                  id column: 'cpy_id', generator: 'uuid2', length: 36
               owner column: 'cpy_owner_fk'
             version column: 'cpy_version'
               payer column: 'cpy_payer_fk'
         payerAmount column: 'cpy_payer_amount'
           payerNote column: 'cpy_payer_note'
  }
  
  static constraints = {
              owner(nullable:false, blank:false);
              payer nullable: false
        payerAmount nullable: false
          payerNote nullable: true
  }   
}
