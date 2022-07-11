package org.olf.oa;

import grails.gorm.MultiTenant

public class PartyAddress implements MultiTenant<PartyAddress> {
	
	String id
  Address address

	static belongsTo = [ owner: Party ]
	
  static mapping = {
         id column: 'padd_id', generator: 'uuid2', length:36
    version column: 'padd_version'
    address column: 'padd_address_fk'
      owner column: 'padd_owner_fk'
	}

  static constraints = {
      owner nullable: false
    address nullable: false
  }
}
