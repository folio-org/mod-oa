databaseChangeLog = {
  changeSet(author: "Jack Golding (manual)", id: "20250721-1620-001") {
    // create the Charge.ChargeStatus category if it doesn't already exist
    grailsChange {
      change {
        sql.execute("INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal) SELECT md5(random()::text || clock_timestamp()::text) as id, 0 as version, 'Charge.ChargeStatus' as description, false as internal WHERE NOT EXISTS (SELECT rdc_description FROM ${database.defaultSchemaName}.refdata_category WHERE (rdc_description)=('Charge.ChargeStatus') LIMIT 1);".toString())
      }
    }

    // Create the "missingChargeStatusRefDataValue" refDataValue for ChargeStatus category
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT md5(random()::text || clock_timestamp()::text) AS id,
                0 AS version,
                'missingChargeStatusRefDataValue' AS value,
                (SELECT rdc_id
                  FROM ${database.defaultSchemaName}.refdata_category
                  WHERE rdc_description = 'Charge.ChargeStatus'
                  LIMIT 1) AS owner,
                'missingChargeStatusRefDataValue' AS label
          WHERE
            -- don't recreate if it already exists
            NOT EXISTS (
              SELECT 1
              FROM ${database.defaultSchemaName}.refdata_value rv
              JOIN ${database.defaultSchemaName}.refdata_category rc
              ON rv.rdv_owner = rc.rdc_id
              WHERE rc.rdc_description = 'Charge.ChargeStatus'
              AND rv.rdv_value = 'missingChargeStatusRefDataValue'
            )
            AND
            -- create only if there are orphaned ch_charge_status_fk FKs
            EXISTS (
              SELECT 1
                FROM ${database.defaultSchemaName}."charge" c
                WHERE c.ch_charge_status_fk IS NOT NULL
                AND NOT EXISTS (
                      SELECT 1
                      FROM ${database.defaultSchemaName}.refdata_value rv2
                      WHERE rv2.rdv_id = c.ch_charge_status_fk
                    )
            );
        """.toString())
      }
    }

    /*
    Get the newly created 'missingChargeStatusRefDataValue' ID from the refDataValue table and set the ch_charge_status_fk
    column to that ID where the value currently in the ch_charge_status_fk DOES NOT CURRENTLY EXIST in the refDataValue table (where block below).
    */
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.charge c
          SET ch_charge_status_fk = (
            SELECT rv.rdv_id
            FROM ${database.defaultSchemaName}.refdata_value rv
            JOIN ${database.defaultSchemaName}.refdata_category rc
            ON rv.rdv_owner = rc.rdc_id
            WHERE rc.rdc_description = 'Charge.ChargeStatus'
            AND rv.rdv_value = 'missingChargeStatusRefDataValue'
            LIMIT 1
          )
          WHERE
            -- only touch rows whose current FK doesn't exist in refdata_value
            NOT EXISTS (
            SELECT 1
            FROM ${database.defaultSchemaName}.refdata_value rvx
            WHERE rvx.rdv_id = c.ch_charge_status_fk
            );
        """.toString())
      }
    }

    addForeignKeyConstraint(baseColumnNames: "ch_charge_status_fk", baseTableName: "charge", constraintName: "charge_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")

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
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT md5(random()::text || clock_timestamp()::text) AS id,
                0 AS version,
                'missingPayerRefDataValue' AS value,
                (SELECT rdc_id
                  FROM ${database.defaultSchemaName}.refdata_category
                  WHERE rdc_description = 'Payer.Payer'
                  LIMIT 1) AS owner,
                'missingPayerRefDataValue' AS label
          WHERE
            -- don't recreate if it already exists
            NOT EXISTS (
              SELECT 1
              FROM ${database.defaultSchemaName}.refdata_value rv
              JOIN ${database.defaultSchemaName}.refdata_category rc
              ON rv.rdv_owner = rc.rdc_id
              WHERE rc.rdc_description = 'Payer.Payer'
              AND rv.rdv_value = 'missingPayerRefDataValue'
            )
            AND
            -- create only if there are orphaned cpy_payer_fk FKs
            EXISTS (
              SELECT 1
                FROM ${database.defaultSchemaName}."payer" p
                WHERE p.cpy_payer_fk IS NOT NULL
                AND NOT EXISTS (
                      SELECT 1
                      FROM ${database.defaultSchemaName}.refdata_value rv2
                      WHERE rv2.rdv_id = p.cpy_payer_fk
                    )
            );
        """.toString())
      }
    }

    /*
    Get the newly created 'missingPayerRefDataValue' ID from the refDataValue table and set the cpy_payer_fk
    column to that ID for any orphaned rows.
    */
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.payer p
          SET cpy_payer_fk = (
            SELECT rv.rdv_id
            FROM ${database.defaultSchemaName}.refdata_value rv
            JOIN ${database.defaultSchemaName}.refdata_category rc
            ON rv.rdv_owner = rc.rdc_id
            WHERE rc.rdc_description = 'Payer.Payer'
            AND rv.rdv_value = 'missingPayerRefDataValue'
            LIMIT 1
          )
          WHERE
            -- only touch rows whose current FK doesn't exist in refdata_value
            NOT EXISTS (
            SELECT 1
            FROM ${database.defaultSchemaName}.refdata_value rvx
            WHERE rvx.rdv_id = p.cpy_payer_fk
            );
        """.toString())
      }
    }

    addForeignKeyConstraint(baseColumnNames: "cpy_payer_fk", baseTableName: "payer", constraintName: "cpy_payer_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")

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
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT md5(random()::text || clock_timestamp()::text) AS id,
                0 AS version,
                'missingRequestStatusRefDataValue' AS value,
                (SELECT rdc_id
                  FROM ${database.defaultSchemaName}.refdata_category
                  WHERE rdc_description = 'PublicationRequest.RequestStatus'
                  LIMIT 1) AS owner,
                'missingRequestStatusRefDataValue' AS label
          WHERE
            -- don't recreate if it already exists
            NOT EXISTS (
              SELECT 1
              FROM ${database.defaultSchemaName}.refdata_value rv
              JOIN ${database.defaultSchemaName}.refdata_category rc
              ON rv.rdv_owner = rc.rdc_id
              WHERE rc.rdc_description = 'PublicationRequest.RequestStatus'
              AND rv.rdv_value = 'missingRequestStatusRefDataValue'
            )
            AND
            -- create only if there are orphaned publication_request FKs
            EXISTS (
              SELECT 1
                FROM ${database.defaultSchemaName}."publication_request" pr
                WHERE pr.pr_request_status IS NOT NULL
                AND NOT EXISTS (
                      SELECT 1
                      FROM ${database.defaultSchemaName}.refdata_value rv2
                      WHERE rv2.rdv_id = pr.pr_request_status
                    )
            );
        """.toString())
      }
    }

    /*
    Get the newly created 'missingRequestStatusRefDataValue' ID from the refDataValue table and set the pr_request_status
    column to that ID for any orphaned rows in the 'publication_request' table.
    */
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.publication_request pr
          SET pr_request_status = (
            SELECT rv.rdv_id
            FROM ${database.defaultSchemaName}.refdata_value rv
            JOIN ${database.defaultSchemaName}.refdata_category rc
            ON rv.rdv_owner = rc.rdc_id
            WHERE rc.rdc_description = 'PublicationRequest.RequestStatus'
            AND rv.rdv_value = 'missingRequestStatusRefDataValue'
            LIMIT 1
          )
          WHERE
            -- only touch rows whose current FK doesn't exist in refdata_value
            NOT EXISTS (
            SELECT 1
            FROM ${database.defaultSchemaName}.refdata_value rvx
            WHERE rvx.rdv_id = pr.pr_request_status
            );
        """.toString())
      }
    }

    addForeignKeyConstraint(baseColumnNames: "pr_request_status", baseTableName: "publication_request", constraintName: "pr_request_status_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "mchaib (manual)", id: "20250721-1630-004") {
    // create the PublicationRequest.RequestStatus category if it doesn't already exist
    grailsChange {
      change {
        sql.execute("INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal) SELECT md5(random()::text || clock_timestamp()::text) as id, 0 as version, 'PublicationRequestHistory.FromState' as description, false as internal WHERE NOT EXISTS (SELECT rdc_description FROM ${database.defaultSchemaName}.refdata_category WHERE (rdc_description)=('PublicationRequestHistory.FromState') LIMIT 1);".toString())
      }
    }

    // Create the "missingFromStatusRefDataValue" refDataValue for RequestStatus category
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT md5(random()::text || clock_timestamp()::text) AS id,
                0 AS version,
                'missingFromStatusRefDataValue' AS value,
                (SELECT rdc_id
                  FROM ${database.defaultSchemaName}.refdata_category
                  WHERE rdc_description = 'PublicationRequestHistory.FromState'
                  LIMIT 1) AS owner,
                'missingFromStatusRefDataValue' AS label
          WHERE
            -- don't recreate if it already exists
            NOT EXISTS (
              SELECT 1
              FROM ${database.defaultSchemaName}.refdata_value rv
              JOIN ${database.defaultSchemaName}.refdata_category rc
              ON rv.rdv_owner = rc.rdc_id
              WHERE rc.rdc_description = 'PublicationRequestHistory.FromState'
              AND rv.rdv_value = 'missingFromStatusRefDataValue'
            )
            AND
            -- create only if there are orphaned publication_request_history FKs
            EXISTS (
              SELECT 1
                FROM ${database.defaultSchemaName}."publication_request_history" prh
                WHERE prh.prh_from_state IS NOT NULL
                AND NOT EXISTS (
                      SELECT 1
                      FROM ${database.defaultSchemaName}.refdata_value rv2
                      WHERE rv2.rdv_id = prh.prh_from_state
                    )
            );
        """.toString())
      }
    }

    /*
    Get the newly created 'missingRequestStatusRefDataValue' ID from the refDataValue table and set the prh_from_state
    column to that ID for any orphaned rows in the 'publication_request' table.
    */
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.publication_request_history prh
          SET prh_from_state = (
            SELECT rv.rdv_id
            FROM ${database.defaultSchemaName}.refdata_value rv
            JOIN ${database.defaultSchemaName}.refdata_category rc
            ON rv.rdv_owner = rc.rdc_id
            WHERE rc.rdc_description = 'PublicationRequestHistory.FromState'
            AND rv.rdv_value = 'missingFromStatusRefDataValue'
            LIMIT 1
          )
          WHERE
            -- only touch rows whose current FK doesn't exist in refdata_value
            NOT EXISTS (
            SELECT 1
            FROM ${database.defaultSchemaName}.refdata_value rvx
            WHERE rvx.rdv_id = prh.prh_from_state
            );
        """.toString())
      }
    }
    addForeignKeyConstraint(baseColumnNames: "prh_from_state", baseTableName: "publication_request_history", constraintName: "prh_from_state_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")

  }

  changeSet(author: "mchaib (manual)", id: "20250721-1630-005") {
    // create the PublicationRequest.RequestStatus category if it doesn't already exist
    grailsChange {
      change {
        sql.execute("INSERT INTO ${database.defaultSchemaName}.refdata_category (rdc_id, rdc_version, rdc_description, internal) SELECT md5(random()::text || clock_timestamp()::text) as id, 0 as version, 'PublicationRequestHistory.ToState' as description, false as internal WHERE NOT EXISTS (SELECT rdc_description FROM ${database.defaultSchemaName}.refdata_category WHERE (rdc_description)=('PublicationRequestHistory.ToState') LIMIT 1);".toString())
      }
    }

    // Create the "missingToStateRefDataValue" refDataValue for RequestStatus category
    grailsChange {
      change {
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.refdata_value (rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label)
          SELECT md5(random()::text || clock_timestamp()::text) AS id,
                0 AS version,
                'missingToStatusRefDataValue' AS value,
                (SELECT rdc_id
                  FROM ${database.defaultSchemaName}.refdata_category
                  WHERE rdc_description = 'PublicationRequestHistory.ToState'
                  LIMIT 1) AS owner,
                'missingToStatusRefDataValue' AS label
          WHERE
            -- don't recreate if it already exists
            NOT EXISTS (
              SELECT 1
              FROM ${database.defaultSchemaName}.refdata_value rv
              JOIN ${database.defaultSchemaName}.refdata_category rc
              ON rv.rdv_owner = rc.rdc_id
              WHERE rc.rdc_description = 'PublicationRequestHistory.ToState'
              AND rv.rdv_value = 'missingToStatusRefDataValue'
            )
            AND
            -- create only if there are orphaned publication_request_history FKs
            EXISTS (
              SELECT 1
                FROM ${database.defaultSchemaName}."publication_request_history" prh
                WHERE prh.prh_to_state IS NOT NULL
                AND NOT EXISTS (
                      SELECT 1
                      FROM ${database.defaultSchemaName}.refdata_value rv2
                      WHERE rv2.rdv_id = prh.prh_to_state
                    )
            );
        """.toString())
      }
    }

    /*
    Get the newly created 'missingToStateRefDataValue' ID from the refDataValue table and set the pr_request_status
    column to that ID for any orphaned rows in the 'publication_request' table.
    */
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.publication_request_history prh
          SET prh_to_state = (
            SELECT rv.rdv_id
            FROM ${database.defaultSchemaName}.refdata_value rv
            JOIN ${database.defaultSchemaName}.refdata_category rc
            ON rv.rdv_owner = rc.rdc_id
            WHERE rc.rdc_description = 'PublicationRequestHistory.ToState'
            AND rv.rdv_value = 'missingToStatusRefDataValue'
            LIMIT 1
          )
          WHERE
            -- only touch rows whose current FK doesn't exist in refdata_value
            NOT EXISTS (
            SELECT 1
            FROM ${database.defaultSchemaName}.refdata_value rvx
            WHERE rvx.rdv_id = prh.prh_to_state
            );
        """.toString())
      }
    }

    addForeignKeyConstraint(baseColumnNames: "prh_to_state", baseTableName: "publication_request_history", constraintName: "prh_to_state_to_rdv_fk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")

  }

}