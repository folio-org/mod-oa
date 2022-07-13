package org.olf.oa.workflow;

import com.ibm.icu.text.Normalizer2

import grails.compiler.GrailsCompileStatic
import grails.gorm.MultiTenant;
import grails.gorm.annotation.Entity;

@GrailsCompileStatic
class ChecklistItemDefinition implements MultiTenant<ChecklistItemDefinition> {
  private static final Normalizer2 normalizer = Normalizer2.NFKDInstance

  String id
  Date dateCreated
  Date lastUpdated

  String description

  String name // Same model as customProperties, use name as non-changing unique slug
  String label // This can then be changed at will by the user

  // Used for ordering. Larger weight values sink.
  int weight = 0

  public static String normValue ( String string ) {
    // Remove all diacritics and substitute for compatibility
    normalizer.normalize( string.trim() ).replaceAll(/\p{M}/, '').replaceAll(/\s+/, '_').toLowerCase()
  }
  
  private static String tidyLabel ( String string ) {
    string.trim().replaceAll(/\s{2,}/, ' ')
  }
  
  void setName (String name) {
    this.name = normValue( name )
  }
  
  void setLabel (String label) {
    this.label = tidyLabel( label )
    if (this.name == null) {
      this.setName( label )
    }
  }
  
  static mapping = {
    tablePerHierarchy false

    id            column: 'clid_id', generator: 'uuid2', length: 36
    dateCreated   column: 'clid_date_created'
    lastUpdated   column: 'clid_last_updated'
    description   column: 'clid_description'
    name          column: 'clid_name'
    label         column: 'clid_label'
    weight        column: 'clid_weight'
  }
  
  static constraints = {
    dateCreated     (nullable: true)
    lastUpdated     (nullable: true)
    name            (nullable: false, blank: false, unique: true)
    description     (nullable: true, blank: false)
    label           (nullable: false, blank: false)
  }
}