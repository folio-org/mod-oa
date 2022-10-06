databaseChangeLog = {
  include file: 'initial-customisations.groovy'
  include file: 'create-mod-oa.groovy'
  include file: 'create-workflow.groovy'
  include file: 'setup-oa-workflow.groovy'  
  
  // Toolkit features opted into.
  include file: 'wtk/hidden-appsetting.feat.groovy'
}
