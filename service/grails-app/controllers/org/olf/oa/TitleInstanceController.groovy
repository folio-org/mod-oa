package org.olf.oa

import grails.rest.*
import grails.converters.*

import org.olf.oa.kb.TitleInstance

import com.k_int.okapi.OkapiTenantAwareController

import groovy.util.logging.Slf4j

class TitleInstanceController extends OkapiTenantAwareController<TitleInstance> {

  TitleInstanceController(){
    super(TitleInstance)
  }

}
