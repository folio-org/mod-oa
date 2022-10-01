package org.olf.oa.kb

import javax.persistence.Transient
import org.hibernate.FetchMode
import org.hibernate.sql.JoinType
import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.RefdataValue
import com.k_int.web.toolkit.refdata.Defaults

import grails.gorm.MultiTenant

/**
 * mod-oa representation of a BIBFRAME instance
 */
public class TitleInstance implements MultiTenant<TitleInstance> {

  String id

  // For grouping sibling title instances together - EG Print and Electronic editions of the same thing
  Work work
  
  // Journal/Book/...
  @CategoryId(defaultInternal=false)
  @Defaults(['Book', 'Journal'])
  RefdataValue publicationType

  // serial / monograph system
  @CategoryId(defaultInternal=true)
  @Defaults(['Monograph', 'Serial'])
  RefdataValue type

  // Print/Electronic
  @CategoryId(defaultInternal=true)
  @Defaults(['Print', 'Electronic'])
  RefdataValue subType

  String title

  static hasMany = [
    identifiers: IdentifierOccurrence
  ]

  static mappedBy = [
    identifiers: 'title'
  ]
  
  Set<IdentifierOccurrence> identifiers

  static mapping = {
                            id column: 'ti_id', generator: 'uuid2', length:36
                       version column: 'ti_version'
                          work column: 'ti_work_fk'
                          type column: 'ti_type_fk', fetch: 'join'
                       subType column: 'ti_subtype_fk', fetch: 'join'
               publicationType column: 'ti_publication_type_fk', fetch: 'join'
                         title column: 'ti_title'
  }

  static constraints = {
              title (nullable:false, blank:false)
               work (nullable:true)
    publicationType (nullable:true)
               type (nullable:true)
            subType (nullable:true)
  
  }
  
}
