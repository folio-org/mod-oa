package org.olf.oa.workflow;

import grails.gorm.MultiTenant;
import grails.gorm.annotation.Entity;


@Entity
class ChecklistItem implements MultiTenant<ChecklistItem> {

  String id
  Date dateCreated
  Date lastUpdated

  ChecklistItemDefinition definition

  ChecklistItemContainer parent

  static mappedBy = [
    "parent" : "items",
  ]

  Set<ChecklistItemNote> notes = []
  static hasMany = [
    notes: ChecklistItemNote
  ]
  
  @CategoryId(defaultInternal=false)
  @Defaults(['Met', 'Not met', 'Other'])
  RefdataValue outcome

  @CategoryId(defaultInternal=true)
  @Defaults(['Required', 'Not required'])
  RefdataValue status



  static mapping = {
    id            column: 'cli_id' // Handle ids slightly differently for custprops
    dateCreated   column: 'cli_date_created'
    lastUpdated   column: 'cli_last_updated'
    outcome       column: 'cli_outcome_fk'
    status        column: 'cli_status_fk'
    definition    column: 'cli_definition_fk'
    parent        column: 'cli_parent_fk'
    notes         cascade: 'all-delete-orphan', sort dateCreated: "desc"
  }
  
  static constraints = {
    dateCreated   (nullable: true)
    lastUpdated   (nullable: true)
    outcome       (nullable: true)
    status        (nullable: true)
    definition    (definition: false)
  }

}