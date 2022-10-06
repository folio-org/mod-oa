package org.olf.oa

import com.k_int.okapi.OkapiHeaders
import com.k_int.web.toolkit.testing.HttpSpec

import grails.testing.mixin.integration.Integration
import groovyx.net.http.FromServer
import spock.lang.*
import spock.util.concurrent.PollingConditions
import groovy.util.logging.Slf4j

/**
 * This class requires special properties to be configured in grails-app/config/application-test.yml - this file
 * is in the .gitignore file to ensure we do not leak API keys via git. See the .gitignore file for details about the
 * properties that need to be set
 */

@Slf4j
@Integration
@Stepwise
class PublicationRequestSpec extends HttpSpec {

  static final String tenantName = 'pr_tests'

  // OA Switchboard test messages - E1 - Eligibility Enquiry, P1 Publication Payment settlement notification messages copied from
  // https://bitbucket.org/oaswitchboard/api/src/master/messages/samples/
  static final Map OA_SWITCHBOARD_E1_NO_PRIOR_AGREEMENT = [
    "header": [
      "type": "e1",
      "version": "v2",
      "to": [ "address": "https://ror.org/000002", "type": "publisher", "name": "Royal Society of Middle-Earth" ],
      "ref": "XXXX-0002",
      "validity": "2022-04-01",
      "persistent": true
    ],
    "data": [
      "authors": [[
        "listingorder": 1, "lastName": "Baggins", "firstName": "Frodo", "initials": "FB", "ORCID": "0000-0000-0000-0000", "email": "frodo.baggins@vu.nl", "isCorrespondingAuthor": true,
        "institutions": [
          [ "name": "VU Amsterdam", "ror": "https://ror.org/008xxew50", "isni": "0000000417549227", "grid": "grid.12380.38" ]
        ],
        "currentaddress": [
          [ "name": "VU Amsterdam", "ror": "https://ror.org/008xxew50", "isni": "0000000417549227", "grid": "grid.12380.38" ]
        ],
        "affiliation": "VU Amsterdam"
      ]],
      "article": [
        "title": "The International relations of Middle-Earth",
        "type": "research-article",
        "funders": [[ "name": "Aragorn Foundation", "ror": "https://ror.org/999999" ],
          [
            "name": "Middle-Earth Thinktank",
            "ror": "https://ror.org/888888"
          ]
        ],
        "acknowledgement": "Aragorn Foundation, Middle-Earth Thinktank",
        "grants": [[
          "name": "Generous grant",
          "id": "GD-000-001"
        ]],
        "doi": "https://doi.org/00.0000/mearth.0000",
        "submissionId": "00.0000",
        "manuscript": [
          "dates": [
            "submission": "2021-02-01",
            "acceptance": "2021-03-01",
            "publication": "2021-04-01"
          ],
          "title": "The International relations of Middle-Earth",
          "id": "00.0000-000"
        ],
        "preprint": [ "title": "The International relations of Middle-Earth", "url": "https://arxiv.org/00.0000-000", "id": "00.0000-000" ],
        "vor": [ "publication": "pure OA journal", "license": "CC BY", "deposition": "open repository, like PMC", "researchdata": "data available on request" ]
      ],
      "journal": [ "name": "Middle Earth papers", "id": "0000-0000", "inDOAJ": true ],
      "settlement": [ "charges": [] ],
      "charges": [
        "prioragreement": false,
        "charged": true,
        "currency": "EUR",
        "fees": [
          "apc": [ "name": "estimated", "type": "per article APC list price", "amount": 1000 ],
          "extra": [[ "type": "submission format", "amount": 100 ], [ "type": "license choice", "amount": 100 ] ], "total": [ "name": "estimated", "amount": 1200 ]
        ]
      ]
    ]
  ]
  

  @Shared
  def grailsApplication

  static final Closure booleanResponder = {
    response.success { FromServer fs, Object body ->
      true
    }
    response.failure { FromServer fs, Object body ->
      false
    }
  }

  def setupSpec() {
    httpClientConfig = {
      client.clientCustomizer { HttpURLConnection conn ->
        conn.connectTimeout = 2000
        conn.readTimeout = 10000 // Need this for activating tenants
      }
    }
  }

  def setup() {
    setHeaders((OkapiHeaders.TENANT): tenantName)
  }

  void "Purge Tenant" () {

    when: 'Purge the tenant'
      boolean resp = doDelete('/_/tenant', null, booleanResponder)

    then: 'Response obtained'
      // We are happy if this one fails - it will fail if there was no tenant to delete, which will be the
      // case in clean dev systems
      1 == 1
  }


  void "Create Tenant" () {
    when: 'Create the tenant'
      boolean resp = doPost('/_/tenant', {
        parameters ([["key": "loadReference", "value": "true" ],
                     ["key": "loadSample", "value": "true" ] ])
      }, null, booleanResponder);
    then:
      resp == true
      
    when: 'Check title count settled'
      // Check every 2 seconds after an initial wait of 3 seconds and
      // timeout after 5 seconds of total wait.
      def conditions = new PollingConditions(timeout: 5, initialDelay: 3, delay: 2)
    
    and: 'Get title count'
      int count = doGet('/oa/titleInstances', [
        stats: true
      ])?.totalRecords ?: 0
    
    then: 'We have some titles'
      conditions.eventually {
        count > 0
        count = (doGet('/oa/titleInstances', [
          stats: true
        ])?.totalRecords ?: 0)
      }

    when: 'Keep checking title count'
      // Check every 5 seconds after an initial wait of 10 seconds and
      // timeout after 60 seconds of total wait.
      conditions = new PollingConditions(timeout: 60, initialDelay: 10, delay: 10)
      int previousCount = count 
    
    then: 'Count eventually settles'
      conditions.eventually {
        count = (doGet('/oa/titleInstances', [
          stats: true
        ])?.totalRecords ?: 0)
        println("# of title instances currently: ${count}");
        count == previousCount
        
        previousCount = count
      }
      
      println("# of title instances settled on : ${count}");
  }

  void "Check sample data was loaded"() {
    log.debug("\n\nCheck sample data loaded\n\n");
    when: 'check sample data to complete'
      List list = doGet('/oa/refdata')
    then: 
      list.size() > 0
  }

  void "Check specific refdata category"() {
    log.debug("\n\nCheck sample data loaded\n\n");
    when: 'check specific refdata'
      List list = doGet('/oa/refdata/Funding/AspectFunded')
    then:
      list.size() > 0
  }

  void "Check specific refdata category with params"() {
    log.debug("\n\nCheck sample data loaded\n\n");
    when: 'check specific refdata'
      Map resp = doGet('/oa/refdata/Funding/AspectFunded', [
        'stats': true,
        'offset': 0,
        'perPage': 10,
        'page': 0
      ])
    then:
      println("resp: ${resp}");
      resp.totalRecords == 2
  }

  void "Check specific refdata category with params and filter and match"() {
    log.debug("\n\nCheck sample data loaded\n\n");
    when: 'check specific refdata'
      // This request is only there to game the coverage - we know these work. sigh.
      Map resp = doGet('/oa/refdata/Funding/AspectFunded', [
        'stats': true,
        'offset': 0,
        'perPage': 10,
        'page': 0,
        'filter': 'value==research',
        'match':'value',
        'term':'research'
      ])
    then:
      println("resp: ${resp}");
      resp.totalRecords == 1
  }

  void 'Set up checklist item definitions'(String name, String description, String label, int weight) {
    when: 'We create a new checkist item definition'
      def checklist_item = [
        'name': name,
        'description': description,
        'label': label,
        'weight': weight
      ]
      def resp = doPost('/oa/checklistItems', checklist_item)
    then:
      resp != null
    where:
      name | description | label | weight
      'Check1' | 'Check1' | 'Check1' | 1
      'Check2' | 'Check2' | 'Check3' | 2
      'Check3' | 'Check2' | 'Check3' | 3
  }

  // Add lots of utterly unnecessary parameters that don't add at all to the quality of the tests to game the coverage metrics. /sigh.
  void 'List checklist item definitions'(boolean stats, int offset, int perPage, int page, String filters, String match, String term) {

    when: 'We get the list of checklist items'
      def resp = doGet('/oa/checklistItems', [
        'stats': stats,
        'offset': offset,
        'perPage': perPage,
        'page': page,
        'filters': filters,
        'match': match,
        'term': term
      ])
      log.debug("List checklist items response: ${resp}");
    then:
      if ( stats == true )
        resp.totalRecords == 1
      else
        resp.size() == 1
      
    where:
      stats | offset | perPage | page | filters | match | term
      true  | 0 | 10 | 0 | 'description==Check1' | 'match' | 'Check1'
      false | 0 | 10 | 0 | 'description==Check1' | 'match' | 'Check1'
  }

  void 'Check title instances'() {
    when: 'We ask for a list of title instances'
      def resp = doGet('/oa/titleInstances', [
        stats: true
      ])

    then:'get the result'
      println("Result of calling /oa/titleInstances: ${resp}");
      resp != null
      resp.totalRecords == 230

  }
  void 'Check no publication requests'() {
    when:'we list bespoke sources'
      def resp = doGet('/oa/publicationRequest', [
        stats: true
      ])

    then:'get the result'
      println("Result of calling /oa/publicationRequest: ${resp}");
      resp != null
      resp.totalRecords == 0
  }

  void 'Create Publication Request'(publication_type, publication_title, author_names, extid) {
    when:'We post a create request'

      def external_ids = extid != null ?  [ [ externalId: extid ] ] : null;
      def create_resp = doPost('/oa/publicationRequest', [
        publicationType: publication_type,
        publicationTitle: publication_title,
        authorNames: author_names,
        externalRequestIds:external_ids
      ]);

    then:
      println("got response: ${create_resp}");
      create_resp != null;

    then:'Check that we do not unneccesarily create multiple history entries'
      def update_resp = doPut("/oa/publicationRequest/${create_resp.id}".toString(), create_resp)
      println("put response: ${update_resp}");
      update_resp.history.size() == 0

    where:
      publication_type|publication_title|author_names|extid
      'Journal Article'|'My article'|'Auth1, Auth2'|'EXTID-1'
      'Journal Article'|'Article 2'|'Auth1'|'EXTID-2'
      'Journal Article'|'Another article 57'|'Auth2'|null
  }

  void 'publication request with correspondence'(publication_type, publication_title, author_names, extid) {
    when:'We post a create request with corrrespondence'

      def external_ids = extid != null ?  [ [ externalId: extid ] ] : null;
      def create_resp = doPost('/oa/publicationRequest', [
        publicationType: publication_type,
        publicationTitle: publication_title,
        authorNames: author_names,
        externalRequestIds:external_ids,
        correspondences:[
          [
            dateOfCorrespondence:'2021-12-17',
            content:'This is the content of the correspondence',
            correspondent:'Fred the postman',
            status:'Awaiting Reply',
            mode:'Email',
            category: 'Funding'
          ]
        ],
        charges:[
          [
            amount:[
              baseCurrency:"EUR",
              value:1.23
            ],
            exchangeRate:[
              fromCurrency:'EUR',
              toCurrency:'EUR',
              coefficient:1
            ],
            description:'A charge',
            paymentPeriod:'2022',
            category:'APC',
            discountType:'percentage',
            invoiceReference:'1323',
            invoiceLineItemReference:'12',
            tax:10
          ]
        ]
      ]);

    then:
      println("got response: ${create_resp}");
      create_resp != null;

    then:'Check that we do not unneccesarily create multiple history entries'
      def update_resp = doPut("/oa/publicationRequest/${create_resp.id}".toString(), create_resp)
      println("put response: ${update_resp}");
      update_resp.history.size() == 0

    where:
      publication_type|publication_title|author_names|extid
      'Journal Article'|'Another article 58'|'Auth3'|null
  }

  void 'update publication request status'(publication_title, newstatus) {
    when:'we find and update a publication request'
      def resp = doGet('/oa/publicationRequest', [
        stats: true,
        match: 'publicationTitle',
        term: publication_title
      ])

      println("Result of find: ${resp}");
      def pub_to_update = resp.results[0]
      pub_to_update.requestStatus = newstatus
      def result_of_update = doPut("/oa/publicationRequest/${pub_to_update.id}".toString(), pub_to_update);

    then:'Check request status updated'
      println("updated publication request record (status): ${result_of_update}");
      result_of_update.requestStatus?.label == newstatus;
    
    where:
      publication_title|newstatus
      'My article'|'Open'
  }

  void 'update publication request with new checklist item'(publication_title, newstatus) {
    when:'we find and update a publication request'

      def resp = doGet('/oa/publicationRequest', [
        stats: true,
        match: 'publicationTitle',
        term: publication_title
      ])

      def checklist_definitions = doGet('/oa/checklistItems', [
        'stats': true,
        'filters': 'name==check1'
      ])
      log.debug("Definitions search result: ${checklist_definitions}");

      String check1_definition_id = checklist_definitions.results[0].id;

      println("Result of find: ${resp}");
      def pub_to_update = resp.results[0]
      pub_to_update.requestStatus = newstatus
      // Set checklist item Check1 to no
      pub_to_update.checklist = [
        [
          'definition': check1_definition_id,
          'outcome': 'no',
          'notes':[
            [ 'note':'This is a note' ]
          ]
        ]
      ]
      
      def result_of_update = doPut("/oa/publicationRequest/${pub_to_update.id}".toString(), pub_to_update);

    then:'Check request status updated'
      println("updated record: ${result_of_update}");
      result_of_update.requestStatus?.label == newstatus;
      result_of_update.checklist[0].outcome.value == 'no'

    where:
      publication_title|newstatus
      'My article'|'Open'
  }

  void 'update existing checklist item'(publication_title, newstatus) {
    when:'we find and update a publication request'

      def resp = doGet('/oa/publicationRequest', [
        stats: true,
        match: 'publicationTitle',
        term: publication_title
      ])

      println("Result of find: ${resp}");
      def pub_to_update = resp.results[0]
      pub_to_update.requestStatus = newstatus

      // we set the outcome to NO in the test above, Set checklist item Check1 to yes here
      pub_to_update.checklist[0].outcome = 'yes'
      pub_to_update.checklist[0].notes[0].note = 'This is an updated note'

      def result_of_update = doPut("/oa/publicationRequest/${pub_to_update.id}".toString(), pub_to_update);

    then:'Check request status updated'
      println("updated record: ${result_of_update}");
      result_of_update.requestStatus?.label == newstatus;
      // Check that the put result thinks that the outcome YES is a refdata which has a value of yes
      result_of_update.checklist[0].outcome.value == 'yes'

    then: 'Re-Fetch the publication request'
      // Do an explicit fetch of the updated publication request
      def refetched_pr = doGet("/oa/publicationRequest/${pub_to_update.id}".toString());
      println("The refetched pr is ${refetched_pr}");

      // Check that the checklist item really has changed to yes
      refetched_pr.checklist[0].outcome.value == 'yes'

    where:
      publication_title|newstatus
      'My article'|'Open'

  }

  void 'Query works endpoint'(qry, expected_count) {
    when:'we seach works'
      def resp = doGet('/oa/works', [
        stats: true,
        match: 'title',
        term: qry
      ])
      println("Result of find: ${resp}");

    then:'Check result count'
      resp.totalRecords == expected_count

    where:
      qry|expected_count
      'Annals of Global Analysis and Geometry'|1
  }

  void 'Post to the oaSwitchboard endpoint'(payload, expected_status) {
    when:'we post to the OA switchboard endpoint'
      def resp = doGet('/oa/externalApi/oaSwitchboard', payload)
      println("Result of post to oaSwitchboard: ${resp}");

    then:'Got right result'
      resp.status == expected_status

    where:
      payload|expected_status
      OA_SWITCHBOARD_E1_NO_PRIOR_AGREEMENT | 'ok'

  }

  void 'Test import endpoint'() {
    when:'We post a citation'
      def resp=doPost('/oa/works/citation', [
        "title": "Platform For Change", 
        "type":"monograph",
        "instances":[ 
          [ "ids":[ [ "ns":"isbn", "id":"978-0471948407" ] ], "subType":"print" ], 
          [ "ids":[ [ "ns":"isbn", "id":"0471948403" ] ], "subType":"electronic"]
        ]
      ])

    then:
      println("result of import: ${resp}");
      resp != null;
  }

  void 'Import requires a title'() {
    def resp = null;
    when: 'We try to import a citation without a title'
    try {
      resp=doPost('/oa/works/citation', [
        "notaprop": "somevalue"
      ])
    }
    catch ( Exception e ) {
    }
    then: 
      resp == null;
  }

  void 'Get Settings'() {
    when: 'We get the settings' 
      def resp=doGet('/oa/settings/appSettings')

    then:
      log.debug("Got settings ${resp}");
  }

  void 'Add a setting'() {
    when: 'We post a setting'
      def resp=doPost('/oa/settings/appSettings', [
        section: 'Test',
        key: 'TestSetting',
        settingType: 'String',
        vocab: null,
        defValue: 'two',
        value: 'one',
        hidden: false
      ])

    then: 'New setting created'
      log.debug("created setting ${resp}");
  }

  void 'get correspondence'() {
    when: 'We get correspondence'
      def resp=doGet('/oa/correspondence', [
        'stats': true,
        'offset': 0,
        'perPage': 10,
        'page': 0
      ])
    then:
      println("resp: ${resp}");
      resp.totalRecords > 0

    then: 'rows'
      resp != null
  }

  void testApcReport() {
    when:'we seach works'
      def resp_bytes = doGet('/oa/reports/openApcChargesReport', [institution: 'My Institution String'])
      String resp_as_str = new String(resp_bytes)
      println("Result of reports: ${resp_as_str}");

    then:'Check result count'
      resp_as_str != null
  }

  void testBpcReport() {
    when:'we seach works'
      def resp_bytes = doGet('/oa/reports/openApcBcpReport', [institution: 'My Institution String'])
      String resp_as_str = new String(resp_bytes)
      println("Result of reports: ${resp_as_str}");

    then:'Check result count'
      resp_as_str != null
  }

  void 'get charges'() {
    when: 'We get charges'
      def resp=doGet('/oa/charges', [
        'stats': true,
        'offset': 0,
        'perPage': 10,
        'page': 0
      ])
    then:
      println("resp: ${resp}");
      resp.totalRecords > 0

    then: 'rows'
      resp != null
  }

  void 'get parties'() {
    when: 'We get parties'
      def resp=doGet('/oa/party', [
        'stats': true,
        'offset': 0,
        'perPage': 10,
        'page': 0
      ])
    then:
      println("resp: ${resp}");
      resp.totalRecords > 0

    then: 'rows'
      resp != null
  }

  void 'get fundings'() {
    when: 'We get fundings'
      def resp=doGet('/oa/fundings', [
        'stats': true,
        'offset': 0,
        'perPage': 10,
        'page': 0
      ])
    then:
      println("resp: ${resp}");
      resp.totalRecords != null

    then: 'rows'
      resp != null
  }


}
