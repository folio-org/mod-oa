package org.olf.oa

import grails.gorm.MultiTenant

import java.util.Date

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue
import groovy.sql.Sql
import com.k_int.web.toolkit.settings.AppSetting

class PublicationRequestHistory implements MultiTenant<PublicationRequestHistory> {

  String id
  PublicationRequest owner
  Date dateModified
  String note
  RefdataValue fromState
  RefdataValue toState

  static belongsTo = [
    owner: PublicationRequest
  ]

  static mapping = {
                  id column: 'prh_id', generator: 'uuid2', length: 36
               owner column: 'prh_owner_fk'
        dateModified column: 'prh_date_modified'
                note column: 'prh_note'
           fromState column: 'prh_from_state'
             toState column: 'prh_to_state'
  }
  
  static constraints = {
        dateModified nullable: true
               owner nullable: false
                note nullable: true
           fromState nullable: true
             toState nullable: false
  }

}
