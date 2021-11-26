package mod_oa.kb

import javax.persistence.Transient
import org.hibernate.FetchMode
import org.hibernate.sql.JoinType
import org.olf.erm.Entitlement
import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.RefdataValue
import com.k_int.web.toolkit.refdata.Defaults

import grails.gorm.MultiTenant

/**
 * mod-oa representation of a BIBFRAME instance
 */
public class TitleInstance extends ErmResource implements MultiTenant<TitleInstance> {

  // For grouping sibling title instances together - EG Print and Electronic editions of the same thing
  Work work
  
  // Journal/Book/...
  @CategoryId(defaultInternal=false)
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

  static mapping = {
                          work column: 'ti_work_fk'
                          type column: 'ti_type_fk'
                       subType column: 'ti_subtype_fk'
               publicationType column: 'ti_publication_type_fk'
                         title column: 'ti_title'
  }

  static constraints = {
            name (nullable:false, blank:false)
            work (nullable:true, blank:false)
  }
  
}
