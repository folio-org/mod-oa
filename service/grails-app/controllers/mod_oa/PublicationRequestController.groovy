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

  @Override
  protected PublicationRequest queryForResource(Serializable id) {     
    PublicationRequest result = null;
    result = PublicationRequest.get(id);
    if ( result == null ) {
      result = PublicationRequest.findByRequestNumber(id)
    }
    return result;
  }

}
