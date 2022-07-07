package org.olf.oa.workflow;

import grails.gorm.MultiTenant;
import grails.gorm.annotation.Entity;


@Entity
class ChecklistItemNote implements MultiTenant<ChecklistItemNote> {

  String id
  Date dateCreated
  Date lastUpdated

  ChecklistItem parent

  static mappedBy = [
    "parent" : "notes",
  ]

  String note

  static mapping = {
    id            column: 'clin_id', generator: 'uuid2', length: 36
    dateCreated   column: 'clin_date_created'
    lastUpdated   column: 'clin_last_updated'
    outcome       column: 'clin_note'
    parent        column: 'clin_parent_fk'
  }
  
  static constraints = {
    dateCreated   (nullable: true)
    lastUpdated   (nullable: true)
    note          (nullable: true)
  }

}