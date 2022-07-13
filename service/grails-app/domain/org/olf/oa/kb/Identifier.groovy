package org.olf.oa.kb

import grails.gorm.MultiTenant


/**
 *
 */
public class Identifier implements MultiTenant<Identifier> {

  String id
  String value
  IdentifierNamespace ns
  
  static hasMany = [
    occurrences: IdentifierOccurrence
  ]

  static mappedBy = [
    occurrences: 'identifier'
  ]

  static mapping = {
                   id column:'id_id', generator: 'uuid2', length:36
              version column:'id_version'
                value column:'id_value', index:'identifier_value_idx'
                   ns column:'id_ns_fk', index:'identifier_value_idx'
  }

  static constraints = {
          value(nullable:false, blank:false)
             ns(nullable:false, blank:false)
  }


}
