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
  Date dateCreated
  String publicationTitle
  String authorNames
  RequestParty correspondingAuthor
  String localReference
  String publicationUrl
  String doi

  @CategoryId(defaultInternal=true)
  @Defaults(['New', 'Rejected'])
  RefdataValue requestStatus

  @CategoryId(defaultInternal=true)
  @Defaults(['Rejected'])
  RefdataValue rejectionReason

  @CategoryId(defaultInternal=true)
  @Defaults(['Journal Article', 'Book'])
  RefdataValue publicationType

  @CategoryId(defaultInternal=true)
  @Defaults(['Subtype 1'])
  RefdataValue subtype
  
  @CategoryId(defaultInternal=true)
  @Defaults(['Publisher 1'])
  RefdataValue publisher

  @CategoryId(defaultInternal=true)
  @Defaults(['License 1'])
  RefdataValue license

  static hasMany = [
    externalRequestIds: ExternalRequestId,
    history: PublicationRequestHistory,
    identifiers: PublicationIdentifier
  ]

  static mappedBy = [
    externalRequestIds: 'owner',
    history: 'owner',
    correspondingAuthor: 'publicationRequestOwner',
    identifiers: 'owner'
  ]

  static mapping = {
                   id column: 'pr_id', generator: 'uuid2', length: 36
          requestDate column: 'pr_request_date'
        requestStatus column: 'pr_request_status'
        requestNumber column: 'pr_request_number'
         dateModified column: 'pr_date_modified'
          dateCreated column: 'pr_date_created'
      rejectionReason column: 'pr_rejection_reason'
     publicationTitle column: 'pr_title'
      publicationType column: 'pr_pub_type_fk'
              subtype column: 'pr_subtype'
            publisher column: 'pr_publisher'
              license column: 'pr_license'
       publicationUrl column: 'pr_pub_url'
       localReference column: 'pr_local_ref'
          authorNames column: 'pr_authnames'
                  doi column: 'pr_doi'
  correspondingAuthor column: 'pr_corresponding_author_fk', cascade: 'save-update'
  }
  
  static constraints = {
          requestDate nullable: true
        requestStatus nullable: true
        requestNumber nullable: true
         dateModified nullable: true
          dateCreated nullable: true
      rejectionReason nullable: true
     publicationTitle nullable: true
      publicationType nullable: true
              subtype nullable: true
            publisher nullable: true
              license nullable: true
       publicationUrl nullable: true
       localReference nullable: true
          authorNames nullable: true
                  doi nullable: true
  correspondingAuthor nullable: true
  }

  def beforeValidate() {
    if ( requestNumber == null ) {
      this.requestNumber = generateHrid()
    }

    if ( this.requestStatus == null ) {
      this.requestStatus = RefdataValue.lookupOrCreate('requestStatus', 'New')
    }
  }

  private String generateHrid() {
    String result = null;

    // Use this to make sessionFactory.currentSession work as expected
    PublicationRequest.withSession { session ->
      AppSetting prefix_setting = AppSetting.findByKey('hrid_prefix')
      log.debug("Got app setting ${prefix_setting} ${prefix_setting?.value} ${prefix_setting?.defValue}");
      String hrid_prefix = prefix_setting?.value ?: prefix_setting.defValue ?: ''
      log.debug("Generate hrid");
      def sql = new Sql(session.connection())
      def query_result = sql.rows("select nextval('pubreq_hrid_seq')".toString());
      log.debug("Query result: ${query_result.toString()}");
      result = hrid_prefix + query_result[0].get('nextval')?.toString();
    }
    return result;
  }

}
