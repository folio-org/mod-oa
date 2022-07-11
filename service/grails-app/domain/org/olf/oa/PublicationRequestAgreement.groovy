package org.olf.oa

import grails.gorm.MultiTenant

import java.time.LocalDate
import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue
import groovy.sql.Sql
import com.k_int.web.toolkit.settings.AppSetting
import com.k_int.okapi.remote_resources.RemoteOkapiLink

class PublicationRequestAgreement extends RemoteOkapiLink implements MultiTenant<PublicationRequestAgreement> {

  static belongsTo = [ owner: PublicationRequest ]

  static mapping = {
    owner column: 'pra_owner'
  }

  static constraints = {
    owner(nullable:false, blank:false)
  }

  @Override
  public final def remoteUri() {
    {->
      "erm/sas/${remoteId}"
    }
  }

}
