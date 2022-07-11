package org.olf.oa

import grails.rest.*
import grails.converters.*
import com.k_int.okapi.OkapiTenantAwareController
import groovy.util.logging.Slf4j
import org.olf.oa.Correspondence;

class CorrespondenceController extends OkapiTenantAwareController<Correspondence> {

  CorrespondenceController(){
    super(Correspondence)
  }

}
