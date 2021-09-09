package org.olf.oa

import grails.gorm.MultiTenant

import java.util.Date

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue
import groovy.sql.Sql
import com.k_int.web.toolkit.settings.AppSetting

class PublicationRequest implements MultiTenant<PublicationRequest> {

  String id
  String requestNumber
  Date requestDate
  Date dateModified
  String publicationTitle
  String authorNames

  @CategoryId(defaultInternal=true)
  @Defaults(['New', 'Rejected'])
  RefdataValue requestStatus

  @CategoryId(defaultInternal=true)
  @Defaults(['Rejected'])
  RefdataValue rejectionReason

  @CategoryId(defaultInternal=true)
  @Defaults(['Journal Article', 'Book'])
  RefdataValue publicationType

  static hasMany = [
    externalRequestIds: ExternalRequestId
  ]

  static mapping = {
                  id column: 'pr_id', generator: 'uuid2', length: 36
         requestDate column: 'pr_request_date'
       requestStatus column: 'pr_request_status'
       requestNumber column: 'pr_request_number'
        dateModified column: 'pr_date_modified'
     rejectionReason column: 'pr_rejection_reason'
    publicationTitle column: 'pr_title'
     publicationType column: 'pr_pub_type_fk'
         authorNames column: 'pr_authnames'
  }
  
  static constraints = {
         requestDate nullable: true
       requestStatus nullable: true
       requestNumber nullable: true
        dateModified nullable: true
     rejectionReason nullable: true
    publicationTitle nullable: true
     publicationType nullable: true
         authorNames nullable: true
  }

  def beforeValidate() {
    if ( requestNumber == null ) {
      this.requestNumber = generateHrid()
    }
  }

  private String generateHrid() {
    String result = null;

    AppSetting prefix_setting = AppSetting.findByKey('hrid_prefix')
    log.debug("Got app setting ${prefix_setting} ${prefix_setting?.value} ${prefix_setting?.defValue}");

    String hrid_prefix = prefix_setting?.value ?: prefix_setting.defValue ?: ''

    // Use this to make sessionFactory.currentSession work as expected
    PublicationRequest.withSession { session ->
      log.debug("Generate hrid");
      def sql = new Sql(session.connection())
      def query_result = sql.rows("select nextval('pubreq_hrid_seq')".toString());
      log.debug("Query result: ${query_result.toString()}");
      result = hrid_prefix + query_result[0].get('nextval')?.toString();
    }
    return result;
  }

}
