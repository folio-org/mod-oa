package org.olf.oa;

import grails.core.GrailsApplication
import grails.plugins.*
import grails.converters.JSON
import groovy.util.logging.Slf4j
import grails.gorm.multitenancy.CurrentTenant

/**
 * External for OA network connectivity
 */
@Slf4j
@CurrentTenant
class ExternalApiController {

  GrailsApplication grailsApplication

  def index() {
  }

  def oaSwitchboard() {
    log.info("ExternalApiController::oaSwitchboard(${params})");
    log.info("Request JSON if present: ${request?.JSON}")
    Map result=['status':'ok']
    render result as JSON
  }
}
