package mod_oa

import grails.rest.*
import grails.converters.*
import com.k_int.okapi.OkapiTenantAwareController
import org.olf.oa.Party
import groovy.util.logging.Slf4j

class PartyController extends OkapiTenantAwareController<Party> {

  PartyController(){
    super(Party)
  }

}
