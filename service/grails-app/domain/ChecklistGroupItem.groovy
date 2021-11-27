package org.olf.oa

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

class ChecklistGroupItem implements MultiTenant<ChecklistGroupItem> {

  String id
  
  @CategoryId(defaultInternal=true)
  @Defaults(['Not started', 'In progress', 'Complete'])
  RefdataValue status

  Integer groupIndex

  ChecklistItem item

  ChecklistGroup group

  static mapping = {
    id column: 'cgi_id', generator: 'uuid2', length: 36
    status column: 'cgi_status_fk'
    groupIndex column: 'cgi_group_index'
    item column: 'cgi_item_fk'
    group column: 'cgi_group_fk'
  }
  
  static constraints = {
    status nullable: true
    groupIndex nullable: true
    item nullable: false
    group nullable: true
  }

  void evaluateRule(def parent) {
    GroovyShell sh = new GroovyShell()
    Closure closure = sh.evaluate(item.rule)
    String output = closure(parent)
    // TODO: Only change this value if it is different to current value
    status = lookupStatusByValue(output)
    this.save(flush: true, failOnError: true)
  }
}
