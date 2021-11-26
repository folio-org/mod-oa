package mod_oa.kb

import grails.gorm.MultiTenant


/**
 * mod-oa representation of a BIBFRAME Work
 */
public class Work implements MultiTenant<Work> {

  String id
  String title

  static mapping = {
                   id column:'w_id', generator: 'uuid2', length:36
              version column:'w_version'
                title column:'w_title'
  }

  static constraints = {
          title(nullable:false, blank:false)
  }


}
