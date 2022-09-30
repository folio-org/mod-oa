package org.olf.oa

class UrlMappings {

  static mappings = {
    "/"(controller: 'application', action:'index');

    '/oa/refdata'(resources: 'refdata') {
      collection {
        "/$domain/$property" (controller: 'refdata', action: 'lookup', method: 'GET')
      }
    }

    "/oa/settings/appSettings" (resources: 'setting');

    "/oa/publicationRequest" (resources: "publicationRequest")
    "/oa/fundings" (resources: "funding")
    "/oa/titleInstances" (resources: "titleInstance")
    "/oa/works" (resources: "work") {
      collection {
        "/citation" (action: "createFromCitation", method: "POST")
      }
    }

    "/oa/party" (resources: "party")
    "/oa/correspondence" (resources: "correspondence")
    "/oa/charges" (resources : "charge")

    "/oa/checklistItems"(resources: 'checklistItemDefinition') {
      collection {
        "/" (controller: 'checklistItemDefinition', action: 'index') {
          sort = [ 'weight;asc', 'label;asc', 'id;asc']
        }
      }
    }
    "/oa/externalApi/oaSwitchboard" (controller: 'externalApi', action:'oaSwitchboard' )
  }
}
