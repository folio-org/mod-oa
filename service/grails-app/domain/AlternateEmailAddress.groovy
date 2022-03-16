package org.olf.oa;

import grails.gorm.MultiTenant

public class AlternateEmailAddress implements MultiTenant<AlternateEmailAddress> {
	
	String id
  String email
	
	static belongsTo = [ owner: Party ]
  
	  static mapping = {
      // table 'alternate_name'
                        id column: 'aea_id', generator: 'uuid2', length:36
                   version column: 'aea_version'
                      email column: 'aea_email'
                     owner column: 'aea_owner_fk'  
	}
  
	static constraints = {
		 owner(nullable:false, blank:false);
	   email(nullable:false, blank:false);
	}
}
