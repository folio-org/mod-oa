package mod_oa

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
    "/oa/works" (resources: "work")

    "/oa/party" (resources: "party")
    "/oa/checklistGroup" (resources: "checklistGroup")
  }
}
