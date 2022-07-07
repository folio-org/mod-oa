package org.olf.oa.workflow;

import org.olf.oa.workflow.ChecklistItemContainer
import org.olf.oa.workflow.ChecklistItemDefinition
import org.olf.oa.workflow.ChecklistItem


import grails.databinding.SimpleMapDataBindingSource
import grails.util.Holders
import grails.web.databinding.DataBindingUtils
import grails.web.databinding.GrailsWebDataBinder
import groovy.util.logging.Log
import groovy.util.logging.Slf4j

@Slf4j
class ChecklistItemBinder {
  
  private static GrailsWebDataBinder getDataBinder() {
    Holders.grailsApplication.mainContext.getBean(DataBindingUtils.DATA_BINDER_BEAN_NAME)
  }
  
  
  private static ChecklistItemContainer doBind (Map itemSource, ChecklistItemContainer cic) {
    log.debug ('Using custom binder')

    if (itemSource && itemSource.size() > 0) {

      // Each supplied property. We only allow predefined types.
      final Set<String> itemNames = itemSource.keySet()
      log.debug ("Looking for properties ${itemNames}")
  
      // Grab the defs present for all properties supplied.
      final Set<ChecklistItemDefinition> itemDefs = ChecklistItemDefinition.createCriteria().list {
        'in' "name", itemNames
      } as Set<ChecklistItemDefinition>
      
      log.debug ("... found ${itemDefs.size()}")
      // Go through every property...
      for (ChecklistItemDefinition itemDef: itemDefs) {
        
        log.debug ("Checking property ${itemDef.name}")
        
        // Grab the values sent in for this property.
        // Either {id: 'someId', value: someValue, [_delete: true]} for update
        // OR  { value: someValue } for new.
        def vals = itemSource[itemDef.name]
        
        // Ensure we have a collection.
        if (vals instanceof Map) {
          vals = [vals]
        }
        
        if (vals instanceof Collection) {
          for (final def valObj : vals) {
            log.debug ("Attempting to bind ${valObj}")
            
            // Single values are presumed to be the 'outcome' key
            Map<String, ?> val
            if ( !(valObj instanceof Map) ) {
              val = [outcome: valObj]
            } else {
              val = valObj
            }

            log.debug ("Using shape ${val}")
            
            // If we have an ID. Select it by id and has to also be of this type.
            ChecklistItem theItem
            final boolean deleteFlag = (val.'_delete' == true)
            if (val.id) {
              // Needs to be a get so that the changes are persisted.
              theItem = ChecklistItem.get(val.id)
              
              // If we are to delete the property we should do that here.
              if (deleteFlag) {
                cic.removeFromItems ( theItem )
              }
            }
            
            // Not delete
            if (!deleteFlag) {
              // Create a new property if we need one.
              theItem = theItem ?: new ChecklistItem()
              log.debug ("Property instance to use as the target ${theItem}")
              
              dataBinder.bind(theItem, new SimpleMapDataBindingSource(val) )
              log.debug ("Property instance after binding ${theItem}")

              // We only want one item of each name in the container,
              //so at this point if more than one exists, delete the others and replace
              Set<ChecklistItem> containerItems = ChecklistItem.executeQuery(
                """
                  SELECT * FROM ChecklistItem AS ci
                  WHERE ci.parent.id = :parentId
                    AND
                      ci.name = :ciName
                    AND ci.id != :ciId
                """,
                [parentId: cic.id, ciName: itemDef.name, ciId: val.id ?: '']
              )
              containerItems.each { cic.removeFromItems(it) }

              
              // Add the property to the container
              cic.addToValue ( theItem )
            }
          }
        }
      }
  
  
      // Else bind the property
  
      // Property has single value.
  
      // Property has multiple values. i.e. Multiple instances of properties,
      // with the same def.
    }
    
    cic.save(failOnError: true)
    cic
  }

  // Add the binder here as a static so we can debug easily.
  static final ChecklistItemContainer bind ( obj, String itemName, source ) {
    // Default way to bind when using this trait. Data is treated as immutable. i.e. Any properties specified,
    // are bound as is and replace any existing values.
    
    // We need the property source only
    def itemSource = source[itemName]
    doBind (itemSource, (obj[itemName] ?: new ChecklistItemContainer()))
  }

}