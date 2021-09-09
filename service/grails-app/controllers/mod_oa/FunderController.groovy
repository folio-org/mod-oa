package mod_oa

import grails.rest.*
import grails.converters.*
import com.k_int.okapi.OkapiTenantAwareController
import org.olf.oa.Funder
import groovy.util.logging.Slf4j

class FunderController extends OkapiTenantAwareController<Funder> {

  FunderController(){
    super(Funder)
  }

}
