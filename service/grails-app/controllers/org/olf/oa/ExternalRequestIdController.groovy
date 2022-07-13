package org.olf.oa

import grails.rest.*
import grails.converters.*
import com.k_int.okapi.OkapiTenantAwareController
import org.olf.oa.ExternalRequestId
import groovy.util.logging.Slf4j

class ExternalRequestIdController extends OkapiTenantAwareController<ExternalRequestId> {
   ExternalRequestIdController(){
    super(ExternalRequestId)
  }
}
