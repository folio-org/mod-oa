package org.olf.oa

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

class RequestParty implements MultiTenant<RequestParty> {

  String id

  static mapping = {
                  id column: 'rp_id', generator: 'uuid2', length: 36
  }
  
  static constraints = {
  }

}
