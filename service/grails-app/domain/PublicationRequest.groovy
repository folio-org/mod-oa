package org.olf.oa

import grails.gorm.MultiTenant

import java.time.LocalDate

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

class PublicationRequest implements MultiTenant<PublicationRequest> {

  String id

  LocalDate requestDate

  @CategoryId(defaultInternal=true)
  @Defaults(['New', 'Requested'])
  RefdataValue requestStatus

  static mapping = {
    id column: 'pr_id', generator: 'uuid2', length: 36
    requestDate column: 'pr_request_date'
    requestStatus column: 'pr_request_status'
  }
  
  static constraints = {
    requestDate nullable: true
    requestStatus nullable: true
  }
}