package mod_oa

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
    // Max time to wait is 10 seconds
    def conditions = new PollingConditions(timeout: 10)

    when: 'Create the tenant'
      boolean resp = doPost('/_/tenant', {
        parameters ([["key": "loadReference", "value": "true" ],
                     ["key": "loadSample", "value": "true" ] ])
      }, null, booleanResponder)

    then: 'Wait for sample data to complete'
      // N.B. The _/tenant post above completes asynchronously and will return 200OK whilst the loading of sample data
      // completes in the background. This sleep ensures we wait for that to finishe before proceeding. N.B. that without this
      // (a) subsequent tests that rely on sample data will fail and (b) the DB connection may shut down if the tests complete
      // whilst the sample data is still being loaded - resulting in you seeing an exception on the command line but not in the test logs
      Thread.sleep(10*1000);

    then: 'Response obtained'
      resp == true

    and: 'Refdata added'

      List list
      // Wait for the refdata to be loaded.
      conditions.eventually {
        (list = doGet('/oa/refdata')).size() > 0
      }
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
      println("updated record: ${result_of_update}");
      result_of_update.requestStatus?.label == newstatus;
    
    where:
      publication_title|newstatus
      'My article'|'In progress'
  }

}
