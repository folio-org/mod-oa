package mod_oa

import grails.rest.*
import grails.converters.*
import com.k_int.okapi.OkapiTenantAwareController
import org.olf.oa.Party
import groovy.util.logging.Slf4j

class CorrespondenceController extends OkapiTenantAwareController<CorrespondenceParty> {

  CorrespondenceController(){
    super(Correspondence)
  }

}
