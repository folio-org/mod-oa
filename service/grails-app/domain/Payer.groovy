package org.olf.oa

import grails.gorm.MultiTenant
import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

class Payer implements MultiTenant<Payer> {

  String id

  @CategoryId(defaultInternal=true)
  @Defaults(['Library', 'DFG', 'Author'])
  RefdataValue payerName

  BigDecimal payerAmount

  String payerNote

  static belongsTo = [
    owner: PublicationRequest
  ]

  static mapping = {
                  id column: 'cpy_id', generator: 'uuid2', length: 36
               owner column: 'cpy_owner_fk'
               payer column: 'cpy_payer'
         payerAmount column: 'cpy_payer_amount'
           payerNote column: 'cpy_payer_note'
  }
  
  static constraints = {
              owner(nullable:false, blank:false);
              payer nullable: false
        payerAmount nullable: true
          payerNote nullable: true
  }   
}