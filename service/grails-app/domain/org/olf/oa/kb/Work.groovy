package org.olf.oa.kb

import grails.gorm.MultiTenant
import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue


/**
 * mod-oa representation of a BIBFRAME Work
 */
public class Work implements MultiTenant<Work> {

  String id
  String title

  @CategoryId(value='Global.Yes_No', defaultInternal=true)
  @Defaults(['Yes', 'No'])
  RefdataValue indexedInDOAJ

  @CategoryId(defaultInternal=false)
  @Defaults(['Gold', 'Hybrid'])
  RefdataValue oaStatus

  static hasMany = [
    instances: TitleInstance
  ]

  static mappedBy = [
    instances: 'work'
  ]

  static mapping = {
                   id column:'w_id', generator: 'uuid2', length:36
              version column:'w_version'
                title column:'w_title'
        indexedInDOAJ column: 'w_indexed_in_doaj_fk'
             oaStatus column: 'w_oa_status_fk'
  }

  static constraints = {
            title(nullable:false, blank:false)
    indexedInDOAJ(nullable:true)
         oaStatus(nullable:true)
  }


}
