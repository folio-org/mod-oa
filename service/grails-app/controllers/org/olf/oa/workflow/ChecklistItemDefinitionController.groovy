package org.olf.oa.workflow

import javax.servlet.ServletRequest

import com.k_int.okapi.OkapiTenantAwareController
import org.olf.oa.workflow.ChecklistItemDefinition

import grails.databinding.SimpleMapDataBindingSource
import grails.gorm.multitenancy.CurrentTenant
import grails.web.Controller
import groovy.util.logging.Slf4j

@Slf4j
@CurrentTenant
@Controller
class ChecklistItemDefinitionController extends OkapiTenantAwareController<ChecklistItemDefinition> {
  
  ChecklistItemDefinitionController() {
    super(ChecklistItemDefinition)
  }
  
  protected def doTheLookup (Class res = this.resource, Closure baseQuery) {
    final int offset = params.int("offset") ?: 0
    final int perPage = Math.min(params.int('perPage') ?: params.int('max') ?: 10, 100)
    final int page = params.int("page") ?: (offset ? (offset / perPage) + 1 : 1)
    final List<String> filters = params.list("filters[]") ?: params.list("filters")
    final List<String> match_in = params.list("match[]") ?: params.list("match")
    
    // This is an ugly way to set defaults. We need to fix this.
    final List<String> sorts = params.list("sort[]") ?: params.list("sort") ?: [ 'weight;asc', 'label;asc', 'id;asc']
    
    if (params.boolean('stats')) {
      return simpleLookupService.lookupWithStats(res, params.term, perPage, page, filters, match_in, sorts, null, baseQuery)
    } else {
      return simpleLookupService.lookup(res, params.term, perPage, page, filters, match_in, sorts, baseQuery)
    }
  }
  
  protected ChecklistItemDefinition createResource(Map parameters) {
    def res
    if (!parameters.type) {
      res = super.createResource(parameters)
    } else {
      res = resource.forType("${parameters.type}", parameters)
    }
    
    res
  }
  
  protected ChecklistItemDefinition createResource() {
    def instance
    def json = getObjectToBind()
    if (json && json.type) {
      instance = resource.forType("${json.type}")
    }
    
    if (!instance) {
      instance = super.createResource()
    }
    
    bindData instance, (json ? new SimpleMapDataBindingSource(json) : getObjectToBind()), ['exclude': ['type']]
    instance
  }
}
