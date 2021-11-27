package mod_oa

import grails.rest.*
import grails.converters.*
import com.k_int.okapi.OkapiTenantAwareController
import org.olf.oa.ChecklistGroup
import groovy.util.logging.Slf4j

class ChecklistGroupController extends OkapiTenantAwareController<ChecklistGroup> {

  ChecklistGroupController(){
    super(ChecklistGroup)
  }

}