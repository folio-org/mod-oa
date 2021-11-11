package org.olf.oa

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

class Funder implements MultiTenant<Funder> {

  String id

  @CategoryId(defaultInternal=true)
  @Defaults(['Funder 1'])
  RefdataValue name

  @CategoryId(defaultInternal=true)
  @Defaults(['Research', 'Publication'])
  RefdataValue aspectFunded  
  
  Date lastUpdated

  static mapping = {
                  id column: 'f_id', generator: 'uuid2', length: 36
        lastUpdated column: 'f_last_updated'
                name column: 'f_name'
        aspectFunded column: 'f_aspect_funded'
  }
  
  static constraints = {
        lastUpdated nullable: true
                name nullable: true
        aspectFunded nullable: true
  }

}
