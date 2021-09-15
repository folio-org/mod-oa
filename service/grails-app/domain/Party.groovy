package org.olf.oa

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

class Party implements MultiTenant<Party> {

  String id

  static mapping = {
                  id column: 'p_id', generator: 'uuid2', length: 36
  }
  
  static constraints = {
  }

}
