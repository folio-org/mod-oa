package mod_oa

import grails.rest.*
import grails.converters.*
import com.k_int.okapi.OkapiTenantAwareController
import org.olf.oa.PublicationRequest
import groovy.util.logging.Slf4j

class PublicationRequestController extends OkapiTenantAwareController<PublicationRequest> {
   PublicationRequestController(){
    super(PublicationRequest)
  }
}
