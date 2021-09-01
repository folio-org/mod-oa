package org.olf.oa

import grails.gorm.MultiTenant

import java.util.Date

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

class PublicationRequest implements MultiTenant<PublicationRequest> {

  String id

  String requestNumber

  Date requestDate

  Date dateModified

  @CategoryId(defaultInternal=true)
  @Defaults(['New', 'Requested'])
  RefdataValue requestStatus

  @CategoryId(defaultInternal=true)
  @Defaults(['Rejected'])
  RefdataValue rejectionReason

  static mapping = {
    id column: 'pr_id', generator: 'uuid2', length: 36
    requestNumber column: 'pr_request_number'
    requestDate column: 'pr_request_date'
    dateModified column: 'pr_date_modified'
    requestStatus column: 'pr_request_status'
    rejectionReason column: 'pr_rejection_reason'
  }
  
  static constraints = {
    requestNumber nullable: true
    requestDate nullable: true
    dateModified nullable: true
    requestStatus nullable: true
    rejectionReason nullable: true
  }
}