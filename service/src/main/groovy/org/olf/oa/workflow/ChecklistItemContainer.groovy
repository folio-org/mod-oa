package org.olf.oa.workflow;

import org.hibernate.criterion.DetachedCriteria
import org.hibernate.criterion.Projections
import org.hibernate.criterion.Restrictions

import com.k_int.web.toolkit.databinding.BindUsingWhenRef
import com.k_int.web.toolkit.utils.DomainUtils.InternalPropertyDefinition

import grails.gorm.MultiTenant
import grails.gorm.annotation.Entity


@Entity
@BindUsingWhenRef({ obj, String propName, source -> CustomPropertiesBinder.bind (obj, propName, source) })
class ChecklistItemContainer implements MultiTenant<ChecklistItemContainer> {

  Set<ChecklistItem> items = []
  static hasMany = [
    items: ChecklistItem
  ]

  /*
   * Do the lookup join by returning ids 
   * @param property
   * @return
   */
  public static DetachedCriteria handleLookupViaSubquery ( final String property ) {
    String[] parts = property.split(/\./)
    
    // We know we should have at least 2 parts...
    if (parts.length < 2) {
      return null
    }
    InternalPropertyDefinition knownDef = null
    if (!knownDef) {
      knownDef = getItemDefByName(parts[0])
    }
    // This criteria should operate on the targeted property but should return matching container id(s)
    new DetachedCriteria( knownDef.type.name )
      .createAlias('definition', 'definition')
      .add( Restrictions.eq('definition.name', parts[0]) )
    .setProjection( Projections.property('parent') )
  }
  
  public static InternalPropertyDefinition getItemDefByName ( final String name ) {
    final Class checklistItemClass = grailsApplication.getDomainClass("org.olf.oa.workflow.ChecklistItem").clazz
    
    if ( checklistItemClass ) {
      InternalPropertyDefinition d = new InternalPropertyDefinition ()
      
      d.type = checklistItemClass
      d.domain = true
      d.sortable = false
      d.name = name
      d.owner = this
      
      return d
    }
    
    null
  }
  
  static mapping = {
    items cascade: 'all-delete-orphan', sort: "name"
  }
}