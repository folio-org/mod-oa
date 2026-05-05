package org.olf.oa

import com.k_int.okapi.OkapiHeaders
import com.k_int.web.toolkit.testing.HttpSpec

import grails.testing.mixin.integration.Integration
import groovyx.net.http.FromServer
import spock.lang.*
import spock.util.concurrent.PollingConditions
import groovy.util.logging.Slf4j

@Slf4j
@Integration
@Stepwise
class PartyLifecycleSpec extends HttpSpec {

  static final String tenantName = 'party_tests'

  def setup() {
    setHeaders((OkapiHeaders.TENANT): tenantName)
  }

  void "Create Tenant" () {
    when: 'Create the tenant'
      boolean resp = doPost('/_/tenant', {
        parameters ([
          ["key": "loadReference", "value": "true"],
          ["key": "loadSample", "value": "true"]
        ])
      }, null, {
        response.success { FromServer fs, Object body -> true }
        response.failure { FromServer fs, Object body -> false }
      })

    then: 'Response obtained'
      resp == true
  }

  void "List Current Parties"() {
    given:
      def conditions = new PollingConditions(timeout: 20, initialDelay: 1, delay: 2)

    expect: "The system eventually responds with a list of 10 (sample data)"
      conditions.eventually {
        List resp = doGet("/oa/party")
        resp.size() == 10
      }
  }

  void "Create a Party"() {
    when: "Post to create new party"
      log.debug("Create new party")
      Map respMap = doPost("/oa/party", {
        'title' 'Mr'
        'familyName' 'Bastu'
        'givenNames' 'Darin'
        'orcidId' '0000-0000-0000-0001'
        'mainEmail' 'darin.bastu@example.com'
        'institutionLevel1' 'Faculty 1'
        'alternateEmails' ([
          ['email': 'd.bastu@example.edu']
        ])
      })

    then: "Response is good and we have a new ID"
      respMap.id != null
      respMap.fullName == 'Darin Bastu'
      respMap.institutionLevel1?.value == 'faculty_1'
      respMap.alternateEmails.size() == 1
  }

  void "Update the Party"() {
    when: "Get the party"
      List resp = doGet("/oa/party", [filters: "familyName==Bastu"])
      def partyId = resp[0].id

    then: "Party exists"
      partyId != null

    when: "Update the party"
      Map updateData = resp[0].clone()
      updateData.familyName = 'Bastu-Updated'
      Map respMap = doPut("/oa/party/${partyId}", updateData)

    then: "Update successful"
      respMap.familyName == 'Bastu-Updated'
      respMap.fullName == 'Darin Bastu-Updated'
  }

  void "Delete the Party"() {
    when: "Get the party"
      List resp = doGet("/oa/party", [filters: "familyName==Bastu-Updated"])
      def partyId = resp[0].id

    then: "Party exists"
      partyId != null

    when: "Delete the party"
      doDelete("/oa/party/${partyId}")

    then: "Deletion successful"
      doGet("/oa/party", [filters: "familyName==Bastu-Updated"]).size() == 0
  }
}
