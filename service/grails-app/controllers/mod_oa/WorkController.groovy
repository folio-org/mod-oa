package mod_oa

import grails.rest.*
import grails.converters.*
import com.k_int.okapi.OkapiTenantAwareController
import mod_oa.kb.Work
import grails.gorm.transactions.Transactional
import mod_oa.BibReferenceService
import groovy.util.logging.Slf4j

class WorkController extends OkapiTenantAwareController<Work> {
  BibReferenceService bibReferenceService

  WorkController(){
    super(Work)
  }

  @Transactional
  def createFromCitation() {
    def data = getObjectToBind()
    def works = bibReferenceService.importWorkAndInstances(data)
    log.debug("LOGDEBUG WORKS: ${works}")
    render render(template: "work", collection: works)
  }
}
