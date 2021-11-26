package mod_oa

import mod_oa.kb.TitleInstance
import mod_oa.kb.Work

public class BibReferenceService {

  public TitleInstance resolveInstance(Map instance_description) {
    log.debug("BibReferenceService::resolveInstance(${instance_description})");
    return null;
  }

  public Work resolveWork(Map description) {

    Work result = null;

    if ( ( description?.title != null ) && ( description?.title.length() > 0 ) ) {

      result = Work.createCriteria().get { title: description.title }

      if ( result == null ) {
        result = new Work(description)
        result.save(flush:true, failOnError:true)
      }
    }

    return result;
  }

  public void importWorkAndInstances(Map description) {
    log.debug("BibReferenceService::importWorkAndInstances(${description})");
    Work work = resolveWork(description)
    description?.instances?.each { instance_overrides ->
      Map instance_data = [ title: description.title, type: description.type ] + instance_overrides
      TitleInstance ti = resolveInstance(instance_data)
    }
  }

}
