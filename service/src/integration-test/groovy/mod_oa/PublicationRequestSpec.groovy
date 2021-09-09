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
        parameters ([["key": "loadReference", "value": true]])
      }, null, booleanResponder)

    then: 'Response obtained'
      resp == true

    and: 'Refdata added'

      List list
      // Wait for the refdata to be loaded.
      conditions.eventually {
        (list = doGet('/oa/refdata')).size() > 0
      }
  }

  void 'Check no funders'() {
    when:
      def resp = doGet('/oa/funders', [
        stats: true
      ])

    then:'get the result'
      println("Result of calling /oa/funders: ${resp}");
      resp != null
      resp.totalRecords == 0
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

  void 'Create Funders'(name, note) {
    when:'We post a create funder'
      def create_resp = doPost('/oa/funders', [
        name: name
      ]);

    then:
      println("got response: ${create_resp}");
      create_resp != null;

    where:
      name|note
      'WELLCOME'|'none'
  }

  void 'Create Publication Request'(publication_type, publication_title, author_names) {
    when:'We post a create request'
      def create_resp = doPost('/oa/publicationRequest', [
        publicationType: publication_type,
        publicationTitle: publication_title,
        authorNames: author_names
      ]);

    then:
      println("got response: ${create_resp}");
      create_resp != null;

    where:
      publication_type|publication_title|author_names
      'Journal Article'|'My article'|'Auth1, Auth2'
  }

}
