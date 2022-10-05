package org.olf.oa.workflow

import org.olf.oa.workflow.ChecklistItem

import grails.compiler.GrailsCompileStatic
import grails.gorm.MultiTenant
import javax.persistence.MappedSuperclass

/**
 * an Workflow - Superclass
 * Contains checklistItems from definitions set up elsewhere
 */
@MappedSuperclass
public class Workflow implements MultiTenant<Workflow> {
  String id
  Set<ChecklistItem> checklist
  
  static hasMany = [
    checklist: ChecklistItem,
  ]

  static mappedBy = [
    checklist:  'parent',
  ]

  static mapping = {
    tablePerHierarchy   false
    id                  column: 'id', generator: 'uuid2', length:36
    checklist           cascade: 'all-delete-orphan'
  }
}
