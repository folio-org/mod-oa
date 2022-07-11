package org.olf.oa

import grails.rest.*
import grails.converters.*
import com.k_int.okapi.OkapiTenantAwareController
import org.olf.oa.Funding
import groovy.util.logging.Slf4j

class FundingController extends OkapiTenantAwareController<Funding> {

  FundingController(){
    super(Funding)
  }

}
