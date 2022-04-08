package org.olf.oa;

import grails.gorm.MultiTenant

public class Address implements MultiTenant<Address> {
	
	String id

  /*
   * Likely not to be used for this project initially,
   * but allow naming of an Address for shared addresses later down the line
   */
  String name 
  
  /* For now we will implement this directly,
   * but we should have an eye to migrating this to something more
   * generic and internationalisable in future
   */

  String addressLineOne
  String addressLineTwo

  String city
  String region // State/Province/Region

  String postalCode // Zip/postal code

  String country // We will leave this as a string for now - migration issues can be dealt with
	
  static mapping = {
                id column: 'add_id', generator: 'uuid2', length:36
           version column: 'add_version'

              name column: 'add_name'

    addressLineOne column: 'add_address_line_one'
    addressLineTwo column: 'add_address_line_two'
              city column: 'add_city'
            region column: 'add_region'
        postalCode column: 'add_postal_code'
           country column: 'add_country'
	}

  static constraints = {
              name nullable: true
    addressLineOne nullable: true
    addressLineTwo nullable: true
              city nullable: true
            region nullable: true
        postalCode nullable: false
           country nullable: false
  }
}
