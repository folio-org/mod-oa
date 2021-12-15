package mod_oa

import grails.rest.*
import grails.converters.*
import com.k_int.okapi.OkapiTenantAwareController
import mod_oa.kb.Work
import groovy.util.logging.Slf4j

class WorkController extends OkapiTenantAwareController<Work> {

  WorkController(){
    super(Work)
  }

}
