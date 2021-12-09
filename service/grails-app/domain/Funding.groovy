package org.olf.oa

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

class Funding implements MultiTenant<Funding> {
  String id

  @CategoryId(defaultInternal=true)
  @Defaults(['Funder 1'])
  RefdataValue funder

  @CategoryId(defaultInternal=true)
  @Defaults(['Research', 'Publication'])
  RefdataValue aspectFunded  
  
  Date lastUpdated

  static belongsTo = [
    owner: PublicationRequest
  ]

  static mapping = {
              id column: 'f_id', generator: 'uuid2', length: 36
     lastUpdated column: 'f_last_updated'
          funder column: 'f_funder_fk'
    aspectFunded column: 'f_aspect_funded'
           owner column: 'f_owner_fk'
  }
  
  static constraints = {
     lastUpdated nullable: true
          funder nullable: true
    aspectFunded nullable: true
    owner(nullable:false, blank:false);
  }

}
