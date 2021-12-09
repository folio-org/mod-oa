package org.olf.oa

import grails.gorm.MultiTenant

class ExternalRequestId implements MultiTenant<ExternalRequestId> {

  String id

  String externalId

  static belongsTo = [
    owner: PublicationRequest
  ]

  static mapping = {
    id column: 'eri_id', generator: 'uuid2', length: 36
    owner column: 'eri_owner_fk'
    externalId column: 'eri_external_id'
  }
  
  static constraints = {
    owner(nullable:false, blank:false);
    externalId nullable: true
  }   
}

