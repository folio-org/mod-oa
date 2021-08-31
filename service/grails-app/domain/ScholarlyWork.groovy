package org.olf.oa

import grails.gorm.MultiTenant
import com.k_int.grails.tools.refdata.*

class ScholarlyWork implements MultiTenant<ScholarlyWork> {

  String id

  String authorNameList

  String publisherURL

  String localReference

  String journalIssueDate

  String journalVolume

  String journalIssue

  String journalPages

  static mapping = {
    id column: 'sw_id', generator: 'uuid2', length: 36
    authorNameList column: 'sw_author_name_list'
    publisherURL column: 'sw_publisher_url'
    localReference column: 'sw_local_reference'
    journalIssue column: 'sw_journal_issue'
    journalIssueDate column: 'sw_journal_issue_date'
    journalVolume column: 'sw_journal_volume'
    journalPages column: 'sw_journal_pages'
  }
  
  static constraints = {
    authorNameList nullable: true
    publisherURL nullable: true
    localReference nullable: true
    journalIssue nullable: true
    journalIssueDate nullable: true
    journalVolume nullable: true
    journalPages nullable: true
  }
  
  // Integer embargoPeriod = 0

  // Integer tasks

  // String publicationPlace

  // String conferenceOrg

  // Boolean complianceStatus

  // Boolean workflowStatus = false



    //  User assignedTo
    // Date contactDate = new Date()
    // RulesService rulesService
  
    // @Defaults([
    // 'Green', 'Gold', 'Gold Paid by Other'
    //     ])
    // RefdataValue publicationRoute

    // @Defaults(['Published', 'Awaiting Publication', 'Unknown'])
    // RefdataValue publicationStatus
  
    // @Defaults(['Accepted', 'Rejected', 'Pending', 'Eligible'])
    // RefdataValue apcFundingApproval
    // Date apcFundingDate

    // Date publishedDate
  
    // @Defaults(['Submitted', 'Not Submitted'])
    // RefdataValue publisherSubmissionStatus
    // Date publisherSubmissionDate
  
    // @Defaults(['Accepted', 'Rejected'])
    // RefdataValue publisherResponse
    // Date publisherResponseDate

    // @Defaults(['CC BY', 'CC BY-SA', 'CC BY-ND', 'CC BY-NC', 'CC BY-NC-SA', 'CC BY-NC-ND', 'Other'])
    // RefdataValue licence

    // // Ugh - hate this model - really would prefer publication to be separate to the AO
    // Org publisher

    // PublicationTitle publishedIn
  
    // @Defaults(['Journal Article', 'Conference Paper'])
    // RefdataValue outputType

    // Date conferenceStartDate
    // Date conferenceEndDate

    // @Defaults(['Delayed', 'Hybrid', 'Pure', 'None'])
    // RefdataValue openAccessStatus

    // @Defaults(['Yes', 'No'])
    // RefdataValue verifiedAuthor

    // Date embargoEndDate

    // @Defaults(['Yes', 'No', 'Not Required'])
    // RefdataValue acknowledgement

    // @Defaults(['Yes', 'No', 'Not Required'])
    // RefdataValue accessStatement

    // @AbsoluteCollection
    // Set academicOutputCosts = []
  
    // @AbsoluteCollection
    // Set names = []
  
    // @AbsoluteCollection
    // Set funds = []
  
    // @AbsoluteCollection
    // Set deposits = []
  
    // @Defaults(['Yes', 'No'])
    // RefdataValue deposited
  

  
  











//   String id 
//   Integer runtime

//   static mapping = {
//     id column: 'mac_id', generator: 'uuid2', length: 36
//     runtime column: 'mac_runtime'
//   }
}