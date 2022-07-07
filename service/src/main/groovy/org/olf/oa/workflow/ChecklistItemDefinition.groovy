package org.olf.oa.workflow;

import grails.gorm.MultiTenant;
import grails.gorm.annotation.Entity;

@Entity
class ChecklistItemDefinition implements MultiTenant<ChecklistItemDefinition> {

  String id
  Date dateCreated
  Date lastUpdated

  String description

  String name // Same model as customProperties, use name as non-changing unique slug
  String label // This can then be changed at will by the user

  // Used for ordering. Larger weight values sink.
  int weight = 0

  private static String nameToLabel (String value) {
    // Strip double whitespace entries.
    return value?.trim().replaceAll(/([a-z0-9A-Z])([A-Z][a-z])/, '$1 $2')
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

  def beforeValidate() {
    if (!this.label && this.name) {
      this.label = nameToLabel(this.name)
    }
  }

}