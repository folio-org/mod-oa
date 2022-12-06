package org.olf.oa

import grails.gorm.MultiTenant

import java.util.Date
import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue
import com.k_int.web.toolkit.settings.AppSetting
import java.time.LocalDate

class Correspondence implements MultiTenant<Correspondence> {

  String id
  PublicationRequest owner
  Date lastUpdated
  LocalDate dateOfCorrespondence
  String content
  String correspondent

  @CategoryId(defaultInternal=false)
  @Defaults(['Awaiting Reply', 'Response Needed', 'Closed'])
  RefdataValue status

  @CategoryId(defaultInternal=false)
  @Defaults(['Email', 'Telephone'])
  RefdataValue mode

  @CategoryId(defaultInternal=false)
  @Defaults(['Invoice', 'Funding'])
  RefdataValue category

  static belongsTo = [
    owner: PublicationRequest
  ]

  static mapping = {
                      id column: 'prc_id', generator: 'uuid2', length: 36
                   owner column: 'prc_owner_fk'
             lastUpdated column: 'prc_last_updated'
    dateOfCorrespondence column: 'prc_date_of_correspondence'
                 content column: 'prc_content'
           correspondent column: 'prc_correspondent'
                  status column: 'prc_status_fk'
                    mode column: 'prc_mode_fk'
                category column: 'prc_category_fk'
  }
  
  static constraints = {
             lastUpdated nullable: true
    dateOfCorrespondence nullable: false
                   owner nullable: false
                 content nullable: false
           correspondent nullable: false
                  status nullable: false
                    mode nullable: false
                category nullable: true
  }

}
