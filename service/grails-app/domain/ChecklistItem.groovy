package org.olf.oa

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

class ChecklistItem implements MultiTenant<ChecklistItem> {

  String id
  // TODO: Make name unique
  String name
  String rule

  static hasMany = [
    checklistGroupItems: ChecklistGroupItem
  ]

  static mappedBy = [
    checklistGroupItems: 'item'
  ]

  static mapping = {
    id column: 'ci_id', generator: 'uuid2', length: 36
    name column: 'ci_name'
    rule column: 'ci_rule'
  }
  
  static constraints = {
    name nullable: true
    rule nullable: true
  }
}
