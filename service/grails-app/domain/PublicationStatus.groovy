package org.olf.oa

import grails.gorm.MultiTenant
import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

import java.time.LocalDate

class PublicationStatus implements MultiTenant<PublicationStatus> {

  String id

  @CategoryId(defaultInternal=true)
  @Defaults(['Submitted'])
  RefdataValue publicationStatus

  LocalDate statusDate

  String statusNote

  static belongsTo = [
    owner: PublicationRequest
  ]

  static mapping = {
                 id column: 'ps_id', generator: 'uuid2', length: 36
              owner column: 'ps_owner_fk'
  publicationStatus column: 'ps_publication_status'
         statusDate column: 'ps_status_date'
         statusNote column: 'ps_status_note'
  }
  
  static constraints = {
              owner(nullable:false, blank:false);
  publicationStatus nullable: true
         statusDate nullable: true
         statusNote nullable: true
  }   
}
