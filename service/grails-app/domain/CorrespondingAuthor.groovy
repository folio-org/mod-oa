package org.olf.oa

import grails.gorm.MultiTenant

class CorrespondingAuthor implements MultiTenant<CorrespondingAuthor> {

    String id

    String title

    String familyName

    String givenNames

    String orcidId

    String mainEmail

    String phone
    
    String mobile

   static belongsTo = [
    owner: PublicationRequest
  ]

  static mapping = {
    id column: 'ca_id', generator: 'uuid2', length: 36
    owner column: 'ca_owner_fk'
    title column: 'ca_title'
    familyName column: 'ca_family_name'
    givenNames column: 'ca_given_names'
    orcidId column: 'ca_orcid_id'
    mainEmail column: 'ca_main_email'
    phone column: 'ca_phone'
    mobile column: 'ca_mobile'
  }
  
  static constraints = {
    owner(nullable:false, blank:false);
    title nullable: true
    familyName nullable: true
    givenNames nullable: true
    orcidId nullable: true
    mainEmail nullable: true
    phone nullable: true
    mobile nullable: true
  }
}

