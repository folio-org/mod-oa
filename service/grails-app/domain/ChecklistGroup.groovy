package org.olf.oa

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

class ChecklistGroup implements MultiTenant<ChecklistGroup> {

  String id

  String name 

  static hasMany =  [
    checklistItems: ChecklistGroupItem
  ]

  static mappedBy = [
    checklistItems: 'group',
  ]
  
  static mapping = {
    id column: 'cg_id', generator: 'uuid2', length: 36
    name column: 'cg_name'
  }
  
  static constraints = {
    name nullable: true
  }

}