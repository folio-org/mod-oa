package org.olf.oa

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

class Party implements MultiTenant<Party> {

  String id

  String title

  String familyName

  String givenNames

  String fullName

  String orcidId

  String mainEmail

  String phone
    
  String mobile

  static hasMany = [
    requestParty: RequestParty,
    alternateEmails: AlternateEmailAddress
  ]

  static mappedBy = [
    requestParty: 'partyOwner',
    alternateEmails: 'owner'
  ]

  static mapping = {
                    id column: 'p_id', generator: 'uuid2', length: 36
                 title column: 'p_title'
            familyName column: 'p_family_name'
            givenNames column: 'p_given_names'
              fullName column: 'p_full_name'
               orcidId column: 'p_orcid_id'
             mainEmail column: 'p_main_email'
                 phone column: 'p_phone'
                mobile column: 'p_mobile'
       alternateEmails cascade: 'all-delete-orphan'
  }
  
  static constraints = {
           title nullable: true
      familyName nullable: true
      givenNames nullable: true
       fullName (nullable:true, bindable: false)
        orcidId (nullable: true, unique: true)
      mainEmail (nullable: true, unique: true)
           phone nullable: true
          mobile nullable: true
  }

  def beforeValidate() {
    fullName = [givenNames, familyName].join(" ")
  }
}
