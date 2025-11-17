databaseChangeLog = {
  include file: 'initial-customisations.groovy'
  include file: 'create-mod-oa.groovy'
  include file: 'create-workflow.groovy'
  include file: 'setup-oa-workflow.groovy'
  include file: 'update-mod-oa-1-1.groovy'
  include file: 'update-mod-oa-2-0.groovy'
  include file: 'add-missing-refdata-values.groovy'

  // Toolkit features opted into.
  include file: 'wtk/hidden-appsetting.feat.groovy'
}
