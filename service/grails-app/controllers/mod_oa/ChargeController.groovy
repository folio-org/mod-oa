package mod_oa

import grails.rest.*
import grails.converters.*
import com.k_int.okapi.OkapiTenantAwareController
import groovy.util.logging.Slf4j
import org.olf.oa.Charge;

class ChargeController extends OkapiTenantAwareController<Charge> {

  ChargeController(){
    super(Charge)
  }

}