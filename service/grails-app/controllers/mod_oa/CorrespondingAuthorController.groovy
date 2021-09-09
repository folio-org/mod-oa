package mod_oa

import grails.rest.*
import grails.converters.*
import com.k_int.okapi.OkapiTenantAwareController
import org.olf.oa.CorrespondingAuthor
import groovy.util.logging.Slf4j

class CorrespondingAuthorController extends OkapiTenantAwareController<CorrespondingAuthor> {
   CorrespondingAuthorController(){
    super(CorrespondingAuthor)
  }
}
