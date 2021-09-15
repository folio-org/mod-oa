package mod_oa

import grails.rest.*
import grails.converters.*
import com.k_int.okapi.OkapiTenantAwareController
import org.olf.oa.RequestParty
import groovy.util.logging.Slf4j

class RequestPartyController extends OkapiTenantAwareController<RequestParty> {

  RequestPartyController(){
    super(RequestParty)
  }

}
