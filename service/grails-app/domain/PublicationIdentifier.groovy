package org.olf.oa

import grails.gorm.MultiTenant
import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

class PublicationIdentifier implements MultiTenant<PublicationIdentifier> {

  String id

  @CategoryId(defaultInternal=false)
  @Defaults(['PMID'])
  RefdataValue type

  String publicationIdentifier


  static belongsTo = [
    owner: PublicationRequest
  ]

  static mapping = {
                       id column: 'pi_id', generator: 'uuid2', length: 36
                    owner column: 'pi_owner_fk'
                     type column: 'pi_type'
    publicationIdentifier column: 'pi_pub_identifier'
  }
  
  static constraints = {
                    owner(nullable:false, blank:false);
                     type nullable: true
    publicationIdentifier nullable: true
  }   
}
