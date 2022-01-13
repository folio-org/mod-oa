package org.olf.oa

import grails.gorm.MultiTenant

import java.time.LocalDate
import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue
import groovy.sql.Sql
import com.k_int.web.toolkit.settings.AppSetting

class PublicationRequest implements MultiTenant<PublicationRequest> {

  String id
  String requestNumber
  LocalDate requestDate
  Date lastUpdated
  Date dateCreated
  String publicationTitle
  String authorNames
  RequestParty correspondingAuthor
  RequestParty requestContact
  String localReference
  String publicationUrl
  String doi

  boolean withoutAgreement

  @CategoryId(defaultInternal=true)
  @Defaults(['New', 'Closed', 'In progress'])
  RefdataValue requestStatus

  @CategoryId(defaultInternal=true)
  @Defaults(['Rejected'])
  RefdataValue rejectionReason

  @CategoryId(defaultInternal=true)
  @Defaults(['Journal Article', 'Book'])
  RefdataValue publicationType

  @CategoryId(defaultInternal=false)
  @Defaults(['Subtype 1'])
  RefdataValue subtype
  
  @CategoryId(defaultInternal=false)
  @Defaults(['Publisher 1'])
  RefdataValue publisher

  @CategoryId(defaultInternal=false)
  @Defaults(['License 1'])
  RefdataValue license

// TODO: PR can only have one group right now
  ChecklistGroup group

  static hasMany = [
    externalRequestIds: ExternalRequestId,
    history: PublicationRequestHistory,
    identifiers: PublicationIdentifier,
    publicationStatuses: PublicationStatus,
    fundings: Funding,
    correspondences: Correspondence
  ]

  static hasOne = [
    agreement: PublicationRequestAgreement
  ]


  static mappedBy = [
    externalRequestIds: 'owner',
    history: 'owner',
    identifiers: 'owner',
    publicationStatuses: 'owner',
    correspondences: 'owner',
    agreement: 'owner'
  ]

  static mapping = {
                   id column: 'pr_id', generator: 'uuid2', length: 36
          requestDate column: 'pr_request_date'
        requestStatus column: 'pr_request_status'
        requestNumber column: 'pr_request_number'
          lastUpdated column: 'pr_last_updated'
          dateCreated column: 'pr_date_created'
      rejectionReason column: 'pr_rejection_reason'
     publicationTitle column: 'pr_title'
      publicationType column: 'pr_pub_type_fk'
  publicationStatuses cascade: 'all-delete-orphan'
             fundings cascade: 'all-delete-orphan'
              subtype column: 'pr_subtype'
            publisher column: 'pr_publisher'
              license column: 'pr_license'
       publicationUrl column: 'pr_pub_url'
       localReference column: 'pr_local_ref'
          authorNames column: 'pr_authnames'
                  doi column: 'pr_doi'
  correspondingAuthor column: 'pr_corresponding_author_fk'
       requestContact column: 'pr_request_contact_fk'
                group column: 'pr_group_fk'
     withoutAgreement column: 'pr_without_agreement'
            agreement cascade: 'all-delete-orphan'
  }
  
  static constraints = {
            requestDate nullable: true
          requestStatus nullable: true
          requestNumber nullable: true
            lastUpdated nullable: true
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
         requestContact nullable: true
                  group nullable: true
              agreement nullable: true
       withoutAgreement(nullable:false, blank:false)

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
