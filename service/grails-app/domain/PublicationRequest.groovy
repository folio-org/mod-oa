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
  @Defaults(['New', 'Rejected'])
  RefdataValue requestStatus

  @CategoryId(defaultInternal=true)
  @Defaults(['Rejected'])
  RefdataValue rejectionReason

  static hasMany = [
    externalRequestIds: ExternalRequestId
  ]

  static mapping = {
                 id column: 'pr_id', generator: 'uuid2', length: 36
        requestDate column: 'pr_request_date'
      requestStatus column: 'pr_request_status'
      requestNumber column: 'pr_request_number'
       dateModified column: 'pr_date_modified'
    rejectionReason column: 'pr_rejection_reason'
  }
  
  static constraints = {
        requestDate nullable: true
      requestStatus nullable: true
      requestNumber nullable: true
       dateModified nullable: true
    rejectionReason nullable: true
  }
}
