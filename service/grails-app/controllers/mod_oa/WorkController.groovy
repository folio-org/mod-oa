package mod_oa

import grails.rest.*
import grails.converters.*
import com.k_int.okapi.OkapiTenantAwareController
import mod_oa.kb.Work
import mod_oa.BibReferenceService
import groovy.util.logging.Slf4j

class WorkController extends OkapiTenantAwareController<Work> {
  BibReferenceService bibReferenceService

  WorkController(){
    super(Work)
  }

  def createFromCitation() {
    Work.withTransaction {
      def data = getObjectToBind()
      Work work = bibReferenceService.importWorkAndInstances(data)
      work.refresh()
      respond (work, view: 'work')
    }
  }
}
