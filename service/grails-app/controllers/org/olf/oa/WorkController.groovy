package org.olf.oa

import grails.rest.*
import grails.converters.*

import org.olf.oa.BibReferenceService
import org.olf.oa.kb.Work

import com.k_int.okapi.OkapiTenantAwareController

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
      respond work
    }
  }
}
