package org.olf.oa

import org.olf.oa.kb.Identifier
import org.olf.oa.kb.IdentifierNamespace
import org.olf.oa.kb.IdentifierOccurrence
import org.olf.oa.kb.TitleInstance
import org.olf.oa.kb.Work

import com.k_int.web.toolkit.refdata.RefdataValue

public class BibReferenceService {

  /**
   * Passed in some metadata in the form
   *    { "title": "Annals of Global Analysis and Geometry", "type":"serial",
   *      "instances":[ { "ids":[ { "ns":"issn", "id":"0232-704X" } ], "subType":"electronic" }, 
   *                    { "ids":[ { "ns":"issn", "id":"1572-9060" } ], "subType":"print"} ]}
   *
   * Match (Possibly enriching) an existing titleInstance record, or create a new one and return that.
   */
  public TitleInstance resolveInstance(Map instance_description) {
    log.debug("BibReferenceService::resolveInstance(${instance_description})");

    if ( ( instance_description == null ) ||
         ( instance_description.ids == null ) ||
         ( instance_description.ids.size() == 0 ) ||
         ( instance_description.title == null ) ||
         ( instance_description.title.length() == 0 ) ) {
      throw new RuntimeException("Missing mandatory information in call to resolveInstance : ${instance_description}");
    }

    TitleInstance result = null;

    // Find all instances matching the set of identifiers provided
    List matching_instances = findAllInstances(instance_description.ids);

    log.debug("Matched instances: ${matching_instances}");

    if ( ( matching_instances == null ) || ( matching_instances.size() == 0 ) ) {
      log.debug("Entirely new title");
      result = createInstance(instance_description);
      // result = new TitleInstance(title:'hello').save(flush:true, failOnError:true);
    }
    else if ( matching_instances.size() == 1 ) {
      log.debug("Matched exactly 1 existing title");

      // Double check that the title matches
      if (matching_instances.get(0)?.work?.title == instance_description?.title) {
        result = matching_instances.get(0)
       } else {
         throw new RuntimeException("Instance description title ${instance_description?.title} does not match for TI record matched by id: ${matching_instances.get(0)}");
       }
    }
    else {
      log.warn("Matched >1 instance for identifiers: ${matching_instances}");
      throw new RuntimeException("Instance description ${instance_description} matched multiple (${matching_instances.size()}) titles");
    }

    return result;
  }

  public Work resolveWork(Map description) {

    log.debug("resolve work: ${description.title}");

    Work result = null;

    if ( ( description?.title != null ) && ( description?.title.length() > 0 ) ) {

      result = Work.createCriteria().get { eq('title',description.title) }

      if ( result == null ) {
        log.debug("Creating new work: ${description}");
        result = new Work(
          title:description.title,
          indexedInDOAJ: description.indexedInDOAJ ? Work.lookupOrCreateIndexedInDOAJ(description.indexedInDOAJ) : null,
          oaStatus: description.oaStatus ? Work.lookupOrCreateOaStatus(description.oaStatus) : null
        )
      }

      result.save(flush:true, failOnError:true)
    }

    log.debug("resolveWork complete");
    return result;
  }

  public Work importWorkAndInstances(Map description) {
    log.debug("BibReferenceService::importWorkAndInstances(${description})");
    Set<Work> works = []

    description?.instances?.each { instance_overrides ->
      Map instance_data = [
        title: description.title,
        type: description.type,
        indexedInDOAJ: description.indexedInDOAJ,
        oaStatus: description.oaStatus
      ] + instance_overrides
      TitleInstance ti = resolveInstance(instance_data)
      works.add(ti.work)
    }

    Work result
    switch (works.size()) {
      case 0:
        //error
        break;
      case 1:
        result = works[0]
        break;
      default:
        //error
        break
    }

    return result
  }

  public TitleInstance titleInstanceById(String ns, String value) {
    TitleInstance result = TitleInstance
                             .createCriteria()
                             .get {
                               identifiers {
                                 and {
                                   identifier {
                                     and {
                                       ns {
                                         eq('value', ns)
                                       }
                                       eq('value',value)
                                     }
                                   }
                                   not {
                                     eq('selected',false)
                                   }
                                 }
                               }
                             }

    return result;
  }

  private Identifier identifierLookup(String p_ns, String p_value) {
    List<Identifier> result = Identifier.createCriteria().list {
               'ns' {
                 eq('value',p_ns)
               }
               eq('value',p_value)
           }
    if (result?.size() > 1) {
      log.warn("Matched >1 identifier namespace and value combination: ${result.size()}");
      throw new RuntimeException("Namespace and value combination ${p_ns}, ${p_value} matched multiple (${result.size()}) identifiers");
    }
    return result[0];
  }
  /*
   * Given a list of ns:"namespace, id:"value" pairs, find all known instances that have an identifier in that list.
   * If we don't find any then we don't know about the title.
   * If we find one, its a good match and we may be able to add identifiers to the set we know about for this instance
   * If we find > 1 there there is bad data around, and a human will need to use the selected flag to make sense of the data
   *
   * @param idlist - the list of maps containnig ns, id pairs
   * @param use_only_selected_identifiers - pass false to match on unverified identifiers (But exclude specifically deselected ones) (the default)
   *                                        pass true to ONLY match on verified identifiers
   */
  private List<TitleInstance> findAllInstances(List<Map> idlist, boolean use_only_selected_identifiers = false) {
    log.debug("BibReferenceService::findAllInstances(${idlist},${use_only_selected_identifiers})");

    List<TitleInstance> result = null
    List<Identifier> located_id_records = []
    
    // Find all the identifiers listed that we already know about and find any attached instances
    idlist.each { id ->
      log.debug("Lookup identifier ${id}");

      Identifier located_identifier = identifierLookup(id.ns,id.id)

      log.debug("Result of Lookup identifier ${located_identifier}");

      if ( located_identifier ) {
        located_id_records.add(located_identifier)
      }
    }

    if ( located_id_records?.size() > 0 ) {

      log.debug("Attempt match against known existing records : ${located_id_records}");

      // Find all titles overlapping with this set of identifiers, but exclude any titles
      // where the identifier occurrence has been "Deselected" ie marked incorrect
      result = TitleInstance
                 .createCriteria()
                 .list {
                   identifiers  {
                     and {
                       inList('identifier',located_id_records)
                       not {
                         eq('selected',use_only_selected_identifiers)
                       }
                     }
                   }
                 }
    }

    return result;
  }

  private TitleInstance createInstance(Map instance_description) {

    log.debug("Create titleInstance for ${instance_description}");

    TitleInstance result = new TitleInstance();
    result.title = instance_description.title;

    if ( instance_description.subType ) {
      result.subType = RefdataValue.lookupOrCreate('TitleInstance.SubType', instance_description.subType, instance_description.subType)
    }

    if ( instance_description.type ) {
      result.type = RefdataValue.lookupOrCreate('TitleInstance.Type', instance_description.type, instance_description.type)
    }

    if ( instance_description.publicationType ) {
      result.publicationType = RefdataValue.lookupOrCreate('TitleInstance.PublicationType', instance_description.publicationType, instance_description.publicationType)
    }
    else {
      switch ( instance_description.type?.toLowerCase() ) {
        case 'monograph':
          result.publicationType=RefdataValue.lookupOrCreate('TitleInstance.PublicationType', 'Book', 'Book')
          break;
        case 'serial':
          result.publicationType=RefdataValue.lookupOrCreate('TitleInstance.PublicationType', 'Journal', 'Journal')
          break;
      }
    }

    result.work = resolveWork(instance_description)

    result.save(flush:true, failOnError:true)

    instance_description.ids.each { id ->
      log.debug("Add identifier: ${id}");
      addIdentifierToTitle(result, id)
    }

    // Reload the title with any identifiers properly attached
    result.refresh()

    return result;
  }

  private IdentifierNamespace lookupOrCreateIdentifierNamespace(String ns) {
    log.debug("lookupOrCreateIdentifierNamespace(${ns})");
    return IdentifierNamespace.findByValue(ns) ?: new IdentifierNamespace(value:ns).save(flush:true, failOnError:true);
  }

  private void addIdentifierToTitle(TitleInstance ti, Map idpair) {

    log.debug("addIdentifierToTitle(...${idpair})");

    boolean selected = true;
    boolean already_present = false;
    Identifier id = identifierLookup(idpair.ns, idpair.id)
    if ( id == null ) {
      log.debug("Create new identifier ${idpair}");
      id = new Identifier( ns: lookupOrCreateIdentifierNamespace(idpair.ns), value: idpair.id ).save(flush:true, failOnError:true);
    }
    else {
      IdentifierOccurrence current_selected = IdentifierOccurrence.createCriteria().get { and { eq('identifier',id) eq('selected',true) } }
      if ( current_selected ) {
        log.warn("The identifier ${idpair} is already selected for instance ${current_selected.title}. identifier will be added as selected:false");
        selected=false;
      }

      IdentifierOccurrence already_present_check = IdentifierOccurrence.createCriteria().get { and { eq('identifier',id) eq('title',ti) } }
      if ( already_present_check != null )
        already_present = true
    }


    if ( !already_present ) {
      IdentifierOccurrence io = new IdentifierOccurrence('identifier': id, 'title':ti, 'selected':selected).save(flush:true, failOnError:true);
    }
    else {
      log.warn("Not adding duplicate identifier occurrence for ${idpair}/${ti}");
    }
  }
}
