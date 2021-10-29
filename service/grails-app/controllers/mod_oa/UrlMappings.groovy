package mod_oa

class UrlMappings {

  static mappings = {
    "/"(controller: 'application', action:'index');

    '/oa/refdata'(resources: 'refdata') {
      collection {
        "/$domain/$property" (controller: 'refdata', action: 'lookup', method: 'GET')
      }
    }
    "/oa/scholarlyWork" (resources: "scholarlyWork")

    "/oa/settings/appSettings" (resources: 'setting');

    "/oa/publicationRequest" (resources: "publicationRequest")
    "/oa/funders" (resources: "funder")
    "/oa/party" (resources: "party")

    "/oa/checklistGroup" (resources: "checklistGroup")

  }
}
