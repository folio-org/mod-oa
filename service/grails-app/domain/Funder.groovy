package org.olf.oa

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

class Funder implements MultiTenant<Funder> {

  String id
  String name
  Date dateModified

  static mapping = {
                  id column: 'f_id', generator: 'uuid2', length: 36
        dateModified column: 'f_date_modified'
                name column: 'f_name'
  }
  
  static constraints = {
        dateModified nullable: true
                name nullable: true
  }

}
