databaseChangeLog = {
  changeSet(author: "mchaib (manual)", id: "20250721-1620-001") {
    // create the Charge.ChargeStatus category if it doesn't already exist
    grailsChange {
      change {
        sql.execute("INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal) SELECT md5(random()::text || clock_timestamp()::text) as id, 0 as version, 'Charge.ChargeStatus' as description, false as internal WHERE NOT EXISTS (SELECT rdc_description FROM ${database.defaultSchemaName}.refdata_category WHERE (rdc_description)=('Charge.ChargeStatus') LIMIT 1);".toString())
      }
    }

    // Create the "missingChargeStatusRefDataValue" refDataValue for ChargeStatus category
    grailsChange {
      change {
        sql.execute("INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label) SELECT md5(random()::text || clock_timestamp()::text) as id, 0 as version, 'missingChargeStatusRefDataValue' as value, (SELECT rdc_id FROM  ${database.defaultSchemaName}.refdata_category WHERE rdc_description='Charge.ChargeStatus') as owner, 'missingChargeStatusRefDataValue' as label WHERE NOT EXISTS (SELECT rdv_id FROM ${database.defaultSchemaName}.refdata_value INNER JOIN ${database.defaultSchemaName}.refdata_category ON refdata_value.rdv_owner = refdata_category.rdc_id WHERE rdc_description='Charge.ChargeStatus' AND rdv_value='missingChargeStatusRefDataValue' LIMIT 1);".toString())
      }
    }

    /*
    Get the newly created 'missingChargeStatusRefDataValue' ID from the refDataValue table and set the ch_charge_status_fk
    column to that ID where the value currently in the ch_charge_status_fk DOES NOT CURRENTLY EXIST in the refDataValue table (where block below).
    */
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.charge
          SET
            ch_charge_status_fk = (
              SELECT ${database.defaultSchemaName}.refdata_value.rdv_id
              FROM ${database.defaultSchemaName}.refdata_value
              INNER JOIN ${database.defaultSchemaName}.refdata_category ON ${database.defaultSchemaName}.refdata_value.rdv_owner = ${database.defaultSchemaName}.refdata_category.rdc_id
              WHERE ${database.defaultSchemaName}.refdata_category.rdc_description = 'Charge.ChargeStatus'
                AND ${database.defaultSchemaName}.refdata_value.rdv_value = 'missingChargeStatusRefDataValue'
              LIMIT 1
            )
          WHERE
            NOT EXISTS (
              SELECT 1
              FROM ${database.defaultSchemaName}.refdata_value
              WHERE ${database.defaultSchemaName}.refdata_value.rdv_id = ${database.defaultSchemaName}.charge.ch_charge_status_fk
            )
        """.toString())
      }
    }
  }

  changeSet(author: "mchaib (manual)", id: "20250721-1620-002") {
    // create the Payer.Payer category if it doesn't already exist
    grailsChange {
      change {
        sql.execute("INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal) SELECT md5(random()::text || clock_timestamp()::text) as id, 0 as version, 'Payer.Payer' as description, false as internal WHERE NOT EXISTS (SELECT rdc_description FROM ${database.defaultSchemaName}.refdata_category WHERE (rdc_description)=('Payer.Payer') LIMIT 1);".toString())
      }
    }

    // Create the "missingPayerRefDataValue" refDataValue for Payer category
    grailsChange {
      change {
        sql.execute("INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label) SELECT md5(random()::text || clock_timestamp()::text) as id, 0 as version, 'missingPayerRefDataValue' as value, (SELECT rdc_id FROM  ${database.defaultSchemaName}.refdata_category WHERE rdc_description='Payer.Payer') as owner, 'missingPayerRefDataValue' as label WHERE NOT EXISTS (SELECT rdv_id FROM ${database.defaultSchemaName}.refdata_value INNER JOIN ${database.defaultSchemaName}.refdata_category ON refdata_value.rdv_owner = refdata_category.rdc_id WHERE rdc_description='Payer.Payer' AND rdv_value='missingPayerRefDataValue' LIMIT 1);".toString())
      }
    }

    /*
    Get the newly created 'missingPayerRefDataValue' ID from the refDataValue table and set the cpy_payer_fk
    column to that ID for any orphaned rows.
    */
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.payer
          SET
            cpy_payer_fk = (
              SELECT ${database.defaultSchemaName}.refdata_value.rdv_id
              FROM ${database.defaultSchemaName}.refdata_value
              INNER JOIN ${database.defaultSchemaName}.refdata_category ON ${database.defaultSchemaName}.refdata_value.rdv_owner = ${database.defaultSchemaName}.refdata_category.rdc_id
              WHERE ${database.defaultSchemaName}.refdata_category.rdc_description = 'Payer.Payer'
                AND ${database.defaultSchemaName}.refdata_value.rdv_value = 'missingPayerRefDataValue'
              LIMIT 1
            )
          WHERE
            NOT EXISTS (
              SELECT 1
              FROM ${database.defaultSchemaName}.refdata_value
              WHERE ${database.defaultSchemaName}.refdata_value.rdv_id = ${database.defaultSchemaName}.payer.cpy_payer_fk
            )
        """.toString())
      }
    }
  }

  changeSet(author: "mchaib (manual)", id: "20250721-1630-003") {
    // create the PublicationRequest.RequestStatus category if it doesn't already exist
    grailsChange {
      change {
        sql.execute("INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal) SELECT md5(random()::text || clock_timestamp()::text) as id, 0 as version, 'PublicationRequest.RequestStatus' as description, false as internal WHERE NOT EXISTS (SELECT rdc_description FROM ${database.defaultSchemaName}.refdata_category WHERE (rdc_description)=('PublicationRequest.RequestStatus') LIMIT 1);".toString())
      }
    }

    // Create the "missingRequestStatusRefDataValue" refDataValue for RequestStatus category
    grailsChange {
      change {
        sql.execute("INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label) SELECT md5(random()::text || clock_timestamp()::text) as id, 0 as version, 'missingRequestStatusRefDataValue' as value, (SELECT rdc_id FROM  ${database.defaultSchemaName}.refdata_category WHERE rdc_description='PublicationRequest.RequestStatus') as owner, 'missingRequestStatusRefDataValue' as label WHERE NOT EXISTS (SELECT rdv_id FROM ${database.defaultSchemaName}.refdata_value INNER JOIN ${database.defaultSchemaName}.refdata_category ON refdata_value.rdv_owner = refdata_category.rdc_id WHERE rdc_description='PublicationRequest.RequestStatus' AND rdv_value='missingRequestStatusRefDataValue' LIMIT 1);".toString())
      }
    }

    /*
    Get the newly created 'missingRequestStatusRefDataValue' ID from the refDataValue table and set the pr_request_status
    column to that ID for any orphaned rows in the 'publication_request' table.
    */
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.publication_request
          SET
            pr_request_status = (
              SELECT ${database.defaultSchemaName}.refdata_value.rdv_id
              FROM ${database.defaultSchemaName}.refdata_value
              INNER JOIN ${database.defaultSchemaName}.refdata_category ON ${database.defaultSchemaName}.refdata_value.rdv_owner = ${database.defaultSchemaName}.refdata_category.rdc_id
              WHERE ${database.defaultSchemaName}.refdata_category.rdc_description = 'PublicationRequest.RequestStatus'
                AND ${database.defaultSchemaName}.refdata_value.rdv_value = 'missingRequestStatusRefDataValue'
              LIMIT 1
            )
          WHERE
            NOT EXISTS (
              SELECT 1
              FROM ${database.defaultSchemaName}.refdata_value
              WHERE ${database.defaultSchemaName}.refdata_value.rdv_id = ${database.defaultSchemaName}.publication_request.pr_request_status
            )
        """.toString())
      }
    }
  }
}