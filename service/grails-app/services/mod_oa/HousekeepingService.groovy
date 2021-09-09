package mod_oa

import java.sql.ResultSet

import javax.sql.DataSource

import org.grails.datastore.mapping.core.exceptions.ConfigurationException
import org.grails.orm.hibernate.HibernateDatastore
import org.grails.plugins.databasemigration.liquibase.GrailsLiquibase

import grails.core.GrailsApplication
import grails.events.annotation.Subscriber
import grails.gorm.multitenancy.Tenants
import grails.gorm.transactions.Transactional
import groovy.sql.Sql
import com.k_int.okapi.OkapiTenantAdminService
import com.k_int.web.toolkit.settings.AppSetting
import com.k_int.web.toolkit.refdata.*

/**
 * This service works at the module level, it's often called without a tenant context.
 */
public class HousekeepingService {

  /**
   * This is called by the eventing mechanism - There is no web request context
   * this method is called after the schema for a tenant is updated.
   */
  @Subscriber('okapi:schema_update')
  public void onSchemaUpdate(tn, tid) {
    log.debug("HousekeepingService::onSchemaUpdate(${tn},${tid})")
    setupData(tn, tid);
  }

  /**
   * Put calls to estabish any required reference data in here. This method MUST be communtative - IE repeated calls must leave the 
   * system in the same state. It will be called regularly throughout the lifecycle of a project. It is common to see calls to
   * lookupOrCreate, or "upsert" type functions in here."
   */
  private void setupData(tenantName, tenantId) {
    log.info("HousekeepingService::setupData(${tenantName},${tenantId})");
    // Establish a database session in the context of the activated tenant. You can use GORM domain classes inside the closure
    Tenants.withId(tenantId) {
      AppSetting.withNewTransaction { status ->
        AppSetting pubreq_id_prefix = AppSetting.findByKey('hrid_prefix') ?: new AppSetting(
                                  section:'PublicationRequests',
                                  settingType:'String',
                                  key: 'hrid_prefix',).save(flush:true, failOnError: true);

      }
    }
  }
}
