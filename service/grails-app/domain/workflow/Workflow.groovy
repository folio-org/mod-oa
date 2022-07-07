package org.olf.oa.workflow

import org.olf.oa.workflow.ChecklistItem

import grails.gorm.MultiTenant

/**
 * an Workflow - Superclass
 * Contains checklistItems from definitions set up elsewhere
 */
public class Workflow implements MultiTenant<Workflow> {
  String id
  Set<ChecklistItem> checklist = []
  
  static hasMany = [
    checklist: ChecklistItem,
  ]

  static mappedBy = [
    checklist:  'parent',
  ]

  static mapping = {
    tablePerHierarchy   false
    id                  column: 'id', generator: 'uuid2', length:36
    version             column: 'version'

    checklist           cascade: 'all-delete-orphan'
  }
}
