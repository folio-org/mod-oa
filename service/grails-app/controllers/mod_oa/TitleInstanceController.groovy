package mod_oa

import grails.rest.*
import grails.converters.*
import com.k_int.okapi.OkapiTenantAwareController
import mod_oa.kb.TitleInstance
import groovy.util.logging.Slf4j

class TitleInstanceController extends OkapiTenantAwareController<TitleInstance> {

  TitleInstanceController(){
    super(TitleInstance)
  }

}
