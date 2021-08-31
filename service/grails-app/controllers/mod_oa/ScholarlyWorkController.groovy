package mod_oa

import grails.rest.*
import grails.converters.*
import com.k_int.okapi.OkapiTenantAwareController
import org.olf.oa.ScholarlyWork

import groovy.util.logging.Slf4j

class ScholarlyWorkController extends OkapiTenantAwareController<ScholarlyWork> {
   ScholarlyWorkController(){
    super(ScholarlyWork)
  }
}
