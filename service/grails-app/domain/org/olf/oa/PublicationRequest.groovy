package org.olf.oa

import grails.compiler.GrailsCompileStatic
import grails.gorm.MultiTenant
import org.hibernate.Session
import org.hibernate.internal.SessionImpl
import org.olf.oa.kb.Work
import org.olf.oa.workflow.Workflow;

import java.time.LocalDate
import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue
import groovy.sql.Sql
import com.k_int.web.toolkit.settings.AppSetting

@GrailsCompileStatic
class PublicationRequest extends Workflow implements MultiTenant<PublicationRequest> {
  String id
  String requestNumber
  LocalDate requestDate
  boolean retrospectiveOA = false
  Date lastUpdated
  Date dateCreated
  String publicationTitle
  String authorNames
  RequestParty correspondingAuthor
  RequestParty requestContact
  String localReference
  String publicationUrl
  String doi

  /* If type is book, we need to store year/place of publication.
   * Creating a full TitleInstance under a Work seems pointless
   * without proper citation information, so instead we can store
   * those fields on the request itself for now with a view to
   * migrating those over once we need a more involved model.
   */

  String bookDateOfPublication
  String bookPlaceOfPublication
  String correspondingInstitutionLevel2

  boolean withoutAgreement = false

  @CategoryId(defaultInternal=true)
  @Defaults(['Open', 'Closed'])
  RefdataValue requestStatus

  @CategoryId(defaultInternal=true)
  @Defaults(['Rejected'])
  RefdataValue closureReason

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

  Work work

  @CategoryId(value='Global.Yes_No', defaultInternal=true)
  @Defaults(['Yes', 'No'])
  RefdataValue workIndexedInDOAJ

  @CategoryId(value='Work.OaStatus', defaultInternal=false)
  @Defaults(['Gold', 'Hybrid'])
  RefdataValue workOAStatus

  @CategoryId(value='Party.InstitutionLevel1', defaultInternal=false)
  @Defaults(['Faculty 1'])
  RefdataValue correspondingInstitutionLevel1

  static hasMany = [
    externalRequestIds: ExternalRequestId,
    history: PublicationRequestHistory,
    identifiers: PublicationIdentifier,
    publicationStatuses: PublicationStatus,
    fundings: Funding,
    correspondences: Correspondence,
    charges: Charge
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
    agreement: 'owner',
    charges: 'owner'
  ]

  static mapping = {
    version             false


                              id column: 'pr_id', generator: 'uuid2', length: 36
                     requestDate column: 'pr_request_date'
                   requestStatus column: 'pr_request_status'
                   requestNumber column: 'pr_request_number'
                 retrospectiveOA column: 'pr_retrospective_oa'
                     lastUpdated column: 'pr_last_updated'
                     dateCreated column: 'pr_date_created'
                   closureReason column: 'pr_closure_reason_fk'
                publicationTitle column: 'pr_title'
                 publicationType column: 'pr_pub_type_fk'
                         subtype column: 'pr_subtype'
                       publisher column: 'pr_publisher'
                         license column: 'pr_license'
                  publicationUrl column: 'pr_pub_url'
                  localReference column: 'pr_local_ref'
                     authorNames column: 'pr_authnames'
                             doi column: 'pr_doi'
             correspondingAuthor column: 'pr_corresponding_author_fk'
                  requestContact column: 'pr_request_contact_fk'
                withoutAgreement column: 'pr_without_agreement'
                            work column: 'pr_work_fk'
           bookDateOfPublication column: 'pr_book_date_of_publication'
          bookPlaceOfPublication column: 'pr_book_place_of_publication'
               workIndexedInDOAJ column: 'pr_work_indexed_in_doaj_fk'
                    workOAStatus column: 'pr_work_oa_status_fk'
  correspondingInstitutionLevel1 column: 'pr_corresponding_institution_level_1_fk'
  correspondingInstitutionLevel2 column: 'pr_corresponding_institution_level_2'

            publicationStatuses cascade: 'all-delete-orphan'
                       fundings cascade: 'all-delete-orphan'
             externalRequestIds cascade: 'all-delete-orphan'
                        history cascade: 'all-delete-orphan'
                    identifiers cascade: 'all-delete-orphan'
                      agreement cascade: 'all-delete-orphan'
                        charges cascade: 'all-delete-orphan'
  }
  
  static constraints = {
                   requestDate nullable: true
                 requestStatus nullable: true
                 requestNumber nullable: true
               retrospectiveOA nullable: true
                   lastUpdated nullable: true
                   dateCreated nullable: true
                 closureReason nullable: true
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
                     agreement nullable: true
              withoutAgreement(nullable: false, blank:false)
         bookDateOfPublication(nullable: true, blank:false, matches: '^\\d{4}(-((0[0-9])|(1[0-2]))(-(([0-2][0-9])|3[0-1]))?)?\$')
        bookPlaceOfPublication(nullable: true)
                          work nullable: true
             workIndexedInDOAJ(nullable:true)
                  workOAStatus(nullable:true)
correspondingInstitutionLevel1(nullable:true)
correspondingInstitutionLevel2(nullable:true)
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
    PublicationRequest.withSession { SessionImpl session ->
      
      AppSetting prefix_setting = AppSetting.findByKey('hrid_prefix')
      log.debug("Got app setting ${prefix_setting} ${prefix_setting?.value} ${prefix_setting?.defValue}")
      String hrid_prefix = prefix_setting?.value ?: prefix_setting.defValue ?: ''
      log.debug("Generate hrid");
      def sql = new Sql(session.connection())
      def query_result = sql.rows("select nextval('pubreq_hrid_seq')".toString())
      log.debug("Query result: ${query_result.toString()}")
      result = hrid_prefix + query_result[0].get('nextval')?.toString()
    }
    return result;
  }

}
