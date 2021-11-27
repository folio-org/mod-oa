import grails.gorm.multitenancy.Tenants
import grails.events.annotation.Subscriber
import grails.gorm.multitenancy.WithoutTenant
import grails.gorm.transactions.Transactional
import com.k_int.web.toolkit.refdata.RefdataValue
import com.k_int.web.toolkit.refdata.RefdataCategory
import com.k_int.web.toolkit.custprops.types.CustomPropertyRefdataDefinition
import com.k_int.web.toolkit.custprops.types.CustomPropertyText;
import com.k_int.web.toolkit.custprops.CustomPropertyDefinition
import grails.databinding.SimpleMapDataBindingSource
import static grails.async.Promises.*
import com.k_int.web.toolkit.settings.AppSetting
import org.olf.oa.ChecklistItem
import org.olf.oa.ChecklistGroupItem
import org.olf.oa.ChecklistGroup

log.info 'Importing sample data'

// Bootstrap ChecklistItems
ChecklistItem hasCorrespondingAuthor = ChecklistItem.findByName('has_corresponding_author') ?: ( new ChecklistItem(
  name: 'has_corresponding_author',
  rule: """{parent -> parent.correspondingAuthor != null ? 'complete' : 'not_started'}"""
  ).save(failOnError:true)
)
// Bootstrap ChecklistGroups
ChecklistGroup.findByName('default') ?: ( new ChecklistGroup(
  name: 'default',
  checklistItems: [
    [
      item: hasCorrespondingAuthor,
      status: 'not_started'
    ]
  ]
).save(failOnError:true)

)

println("\n\n***Completed tenant setup***");
