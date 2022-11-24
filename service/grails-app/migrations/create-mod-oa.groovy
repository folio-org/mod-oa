databaseChangeLog = {

  changeSet(author: "ianibbo (manual)", id: "i202105051310-001") {
    createSequence(sequenceName: "hibernate_sequence")
  }

  changeSet(author: "ianibbo (generated)", id: "i202105051311-001") {

    createTable(tableName: "refdata_category") {
      column(name: "rdc_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "rdc_version", type: "BIGINT") { constraints(nullable: "false") }
      column(name: "rdc_description", type: "VARCHAR(255)") { constraints(nullable: "false") }
      column(name: "internal", type: "boolean")
    }

    addPrimaryKey(columnNames: "rdc_id", constraintName: "refdata_categoryPK", tableName: "refdata_category")

    addNotNullConstraint (tableName: "refdata_category", columnName: "internal", defaultNullValue: false)

    createTable(tableName: "refdata_value") {
      column(name: "rdv_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "rdv_version", type: "BIGINT") { constraints(nullable: "false") }
      column(name: "rdv_value", type: "VARCHAR(255)") { constraints(nullable: "false") }
      column(name: "rdv_owner", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "rdv_label", type: "VARCHAR(255)") { constraints(nullable: "false") }
    }

    addPrimaryKey(columnNames: "rdv_id", constraintName: "refdata_valuePK", tableName: "refdata_value")

  }

  changeSet(author: "samhepburn (manual)", id: "i202107091112-001") {
    createTable(tableName: "scholarly_work") {
      column(name: "sw_id", type: "VARCHAR(36)")
      column(name: "sw_author_name_list", type: "VARCHAR(255)")
      column(name: "sw_publisher_url", type: "VARCHAR(255)")
      column(name: "sw_local_reference", type: "VARCHAR(36)")
      column(name: "sw_journal_issue", type: "VARCHAR(36)")
      column(name: "sw_journal_issue_date", type: "VARCHAR(36)")
      column(name: "sw_journal_volume", type: "VARCHAR(36)")
      column(name: "sw_journal_pages", type: "VARCHAR(36)")
      column(name: "version", type: "BIGINT") {
        constraints(nullable: "false")
      }
    }
  }

  changeSet(author: "ianibbo (manual)", id: "i202109011115-001") {
    createTable(tableName: "app_setting") {
      column(name: "st_id", type: "VARCHAR(36)") {
        constraints(nullable: "false")
      }
      column(name: "st_version", type: "BIGINT") {
        constraints(nullable: "false")
      }
      column(name: 'st_section', type: "VARCHAR(255)")
      column(name: 'st_key', type: "VARCHAR(255)")
      column(name: 'st_setting_type', type: "VARCHAR(255)")
      column(name: 'st_vocab', type: "VARCHAR(255)")
      column(name: 'st_default_value', type: "VARCHAR(255)")
      column(name: 'st_value', type: "VARCHAR(255)")
    }

    addPrimaryKey(columnNames: "st_id", constraintName: "app_setting_PK", tableName: "app_setting")
  }

  // DON'T USE DATE COL TYPE--USE TIMESTAMP
  // Version here removed later to work with Workflow superclass 
  changeSet(author: "samhepburn (manual)", id: "i202108171122-001") {
    createTable(tableName: "publication_request") {
      column(name: "pr_id", type: "VARCHAR(36)")
      column(name: "pr_request_date", type: "DATE")
      column(name: "pr_request_status", type: "VARCHAR(36)")
      column(name: "version", type: "BIGINT") {
        constraints(nullable: "false")
      }
    }
  }

  // DON'T USE DATE COL TYPE--USE TIMESTAMP
  changeSet(author: "samhepburn (manual)", id: "i202109011321") {
    addColumn(tableName: "publication_request") {
      column(name: "pr_request_number", type: "VARCHAR(36)")
      column(name: "pr_date_modified", type: "DATE")
      column(name: "pr_rejection_reason", type: "VARCHAR(36)")
    }
  }

  changeSet(author: "samhepburn (manual)", id: "i202109051020") {
    createTable(tableName: "external_request_id") {
      column(name: "eri_id", type: "VARCHAR(36)")
      column(name: "eri_external_id", type: "VARCHAR(36)")
      column(name: "version", type: "BIGINT") {
        constraints(nullable: "false")
      }
    }
  }

  changeSet(author: "samhepburn (manual)", id: "i202109061208") {
    addColumn(tableName: "external_request_id") {
      column(name: "eri_owner", type: "VARCHAR(36)") {
        constraints(nullable: "false")
      }
    }
  }

  changeSet(author: "samhepburn (manual)", id: "i202109061520") {
    addUniqueConstraint(columnNames: "pr_id", constraintName: "pr_id_unique", tableName: "publication_request")

    renameColumn(tableName: "external_request_id", oldColumnName: "eri_owner", newColumnName: "eri_owner_fk")

    addForeignKeyConstraint(baseColumnNames: "eri_owner_fk",
        baseTableName: "external_request_id",
        constraintName: "external_request_id_owner_fk",
        deferrable: "false",
        initiallyDeferred: "false",
        referencedColumnNames: "pr_id",
        referencedTableName: "publication_request")
  }

  changeSet(author: "ianibbo (manual)", id: "i202109091024") {
    createSequence(sequenceName:'pubreq_hrid_seq')
    addColumn(tableName: "publication_request") {
      column(name: "pr_title", type: "VARCHAR(256)")
      column(name: 'pr_pub_type_fk', type: "VARCHAR(36)")
      column(name: 'pr_authnames', type: "VARCHAR(256)")
    }
  }

  // DON'T USE DATE COL TYPE--USE TIMESTAMP
  changeSet(author: "samhepburn (manual)", id: "i202109091145") {
    addColumn(tableName: "publication_request") {
      column(name: "pr_date_created", type: "DATE")
    }
  }

  // DON'T USE DATE COL TYPE--USE TIMESTAMP
  changeSet(author: "ianibbo (manual)", id: "i202109091208") {
    createTable(tableName: "publication_request_history") {
      column(name: "version", type: "BIGINT") {
        constraints(nullable: "false")
      }
      column(name: "prh_id", type: "VARCHAR(36)")
      column(name: "prh_date_modified", type: "DATE")
      column(name: "prh_owner_fk", type: "VARCHAR(36)")
      column(name: "prh_note", type: "VARCHAR(256)")
      column(name: "prh_from_state", type: "VARCHAR(36)")
      column(name: "prh_to_state", type: "VARCHAR(36)")
    }
  }

  changeSet(author: "ibbo (generated)", id: "1549360204236-37") {
    createTable(tableName: "title_instance") {
      column(name: "ti_id", type: "VARCHAR(36)")
      column(name: "ti_version", type: "BIGINT")
      column(name: "ti_work_fk", type: "VARCHAR(36)")
      column(name: "ti_type_fk", type: "VARCHAR(36)")
      column(name: "ti_subtype_fk", type: "VARCHAR(36)")
      column(name: "ti_publication_type_fk", type: "VARCHAR(36)")
      column(name: "ti_title", type: "VARCHAR(2048)")
    }
  }

  changeSet(author: "ibbo (generated)", id: "1549360204236-38b") {
    createTable(tableName: "work") {
      column(name: "w_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "w_version", type: "BIGINT") { constraints(nullable: "false") }
      column(name: "w_title", type: "VARCHAR(2048)") { constraints(nullable: "false") }
    }

    createIndex(indexName: "work_title_idx", tableName: "work") {
      column(name: "w_title")
    }
  }

  changeSet(author: "ibbo (generated)", id: "1549360204236-17") {
    createTable(tableName: "identifier") {
      column(name: "id_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "id_version", type: "BIGINT") { constraints(nullable: "false") }
      column(name: "id_ns_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "id_value", type: "VARCHAR(255)") { constraints(nullable: "false") } 
    }
  }

  changeSet(author: "ibbo (generated)", id: "1549360204236-18") {
    createTable(tableName: "identifier_namespace") {
      column(name: "idns_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "idns_version", type: "BIGINT") { constraints(nullable: "false") }
      column(name: "idns_value", type: "VARCHAR(255)") { constraints(nullable: "false") }
    }
  }

  changeSet(author: "ibbo (generated)", id: "1549360204236-19") {
    createTable(tableName: "identifier_occurrence") {
      column(name: "io_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "io_version", type: "BIGINT") { constraints(nullable: "false") }
      column(name: "io_ti_fk", type: "VARCHAR(36)")
      column(name: "io_status_fk", type: "VARCHAR(36)")
      column(name: "io_identifier_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "io_selected", type: "boolean") 
    }
  }

  changeSet(author: "samhepburn (manual)", id: "i202109151056") {
    createTable(tableName: "party") {
      column(name: "version", type: "BIGINT") {
        constraints(nullable: "false")
      }
      column(name: "p_id", type: "VARCHAR(36)")
      column(name: "p_title", type: "VARCHAR(12)")
      column(name: "p_family_name", type: "VARCHAR(36)")
      column(name: "p_given_names", type: "VARCHAR(36)")
      column(name: "p_orcid_id", type: "VARCHAR(36)")
      column(name: "p_main_email", type: "VARCHAR(36)")
      column(name: "p_phone", type: "VARCHAR(36)")
      column(name: "p_mobile", type: "VARCHAR(36)")
    }
  }

    changeSet(author: "samhepburn (manual)", id: "i202109151106") {
    createTable(tableName: "request_party") {
      column(name: "version", type: "BIGINT") {
        constraints(nullable: "false")
      }
      column(name: "rp_id", type: "VARCHAR(36)")
      column(name: "rp_role", type: "VARCHAR(36)")
      column(name: "rp_publication_request_fk", type: "VARCHAR(36)")
      column(name: "rp_party_fk", type: "VARCHAR(36)")
    }
  }

    changeSet(author: "samhepburn (manual)", id: "i202109151110") {

      addColumn(tableName: "publication_request") {
        column(name: "pr_corresponding_author_fk", type: "VARCHAR(36)")
      }

      addUniqueConstraint(columnNames: "p_id", constraintName: "p_id_unique", tableName: "party")
      addUniqueConstraint(columnNames: "rp_id", constraintName: "rp_id_unique", tableName: "request_party")

      addForeignKeyConstraint(baseColumnNames: "rp_publication_request_fk",
        baseTableName: "request_party",
        constraintName: "publication_request_fk",
        deferrable: "false",
        initiallyDeferred: "false",
        referencedColumnNames: "pr_id",
        referencedTableName: "publication_request")
    
      addForeignKeyConstraint(baseColumnNames: "rp_party_fk",
        baseTableName: "request_party",
        constraintName: "request_party_party_fk",
        deferrable: "false",
        initiallyDeferred: "false",
        referencedColumnNames: "p_id",
        referencedTableName: "party")

      addForeignKeyConstraint(baseColumnNames: "pr_corresponding_author_fk",
        baseTableName: "publication_request",
        constraintName: "publication_request_request_party_fk",
        deferrable: "false",
        initiallyDeferred: "false",
        referencedColumnNames: "rp_id",
        referencedTableName: "request_party")
  }
  
    changeSet(author: "samhepburn (manual)", id: "i202109231034") {
      addColumn(tableName: "publication_request") {
        column(name: "pr_local_ref", type: "VARCHAR(36)")
      }
      addColumn(tableName: "publication_request") {
        column(name: "pr_pub_url", type: "VARCHAR(255)")
      }
      addColumn(tableName: "publication_request") {
        column(name: "pr_subtype", type: "VARCHAR(36)")
      }
      addColumn(tableName: "publication_request") {
        column(name: "pr_publisher", type: "VARCHAR(36)")
      }
      addColumn(tableName: "publication_request") {
        column(name: "pr_license", type: "VARCHAR(36)")
      }
      addColumn(tableName: "publication_request") {
        column(name: "pr_doi", type: "VARCHAR(36)")
      }
    }

    changeSet(author: "samhepburn (manual)", id: "i202109231121") {
      createTable(tableName: "publication_identifier") {
        column(name: "pi_id", type: "VARCHAR(36)")
        column(name: "pi_type", type: "VARCHAR(36)")
        column(name: "pi_owner_fk", type: "VARCHAR(36)")
        column(name: "pi_pub_identifier", type: "VARCHAR(36)")
        column(name: "version", type: "BIGINT") {
          constraints(nullable: "false")
        }
      }
      addForeignKeyConstraint(baseColumnNames: "pi_owner_fk",
        baseTableName: "publication_identifier",
        constraintName: "publication_identifier_owner_fk",
        deferrable: "false",
        initiallyDeferred: "false",
        referencedColumnNames: "pr_id",
        referencedTableName: "publication_request")
    }

    // DON'T USE DATE COL TYPE--USE TIMESTAMP
    changeSet(author: "samhepburn (manual)", id: "i202109281157") {
      createTable(tableName: "publication_status") {
        column(name: "ps_id", type: "VARCHAR(36)")
        column(name: "ps_owner_fk", type: "VARCHAR(36)")
        column(name: "ps_publication_status", type: "VARCHAR(36)")
        column(name: "ps_status_date", type: "DATE")
        column(name: "ps_status_note", type: "VARCHAR(255)")
        column(name: "version", type: "BIGINT") {
          constraints(nullable: "false")
        }
      }
      addForeignKeyConstraint(baseColumnNames: "ps_owner_fk",
        baseTableName: "publication_status",
        constraintName: "publication_status_owner_fk",
        deferrable: "false",
        initiallyDeferred: "false",
        referencedColumnNames: "pr_id",
        referencedTableName: "publication_request"
      )
    }

    // Checklist stuff is later dropped ready for separate checklist workflow domain classes
    changeSet(author: "samhepburn (manual)", id: "i202110141107") {
      createTable(tableName: "checklist_group") {
        column(name: "cg_id", type: "VARCHAR(36)")
        column(name: "cg_name", type: "VARCHAR(255)")
        column(name: "version", type: "BIGINT") {
          constraints(nullable: "false")
        }
      }
      createTable(tableName: "checklist_item") {
        column(name: "ci_id", type: "VARCHAR(36)")
        column(name: "ci_name", type: "VARCHAR(255)")
        column(name: "ci_rule", type: "TEXT")
        column(name: "version", type: "BIGINT") {
          constraints(nullable: "false")
        }
      }
      createTable(tableName: "checklist_group_item") {
        column(name: "cgi_id", type: "VARCHAR(36)")
        column(name: "cgi_status_fk", type: "VARCHAR(36)")
        column(name: "cgi_group_index", type: "VARCHAR(36)")
        column(name: "cgi_item_fk", type: "VARCHAR(36)")
        column(name: "cgi_group_fk", type: "VARCHAR(36)")
        column(name: "version", type: "BIGINT") {
          constraints(nullable: "false")
        }
      }
      addUniqueConstraint(columnNames: "cg_id", constraintName: "cg_id_unique", tableName: "checklist_group")
      addUniqueConstraint(columnNames: "ci_id", constraintName: "ci_id_unique", tableName: "checklist_item")
      addForeignKeyConstraint(baseColumnNames: "cgi_group_fk",
        baseTableName: "checklist_group_item",
        constraintName: "cgi_group_fk",
        deferrable: "false",
        initiallyDeferred: "false",
        referencedColumnNames: "cg_id",
        referencedTableName: "checklist_group"
      )
      addForeignKeyConstraint(baseColumnNames: "cgi_item_fk",
        baseTableName: "checklist_group_item",
        constraintName: "cgi_item_fk",
        deferrable: "false",
        initiallyDeferred: "false",
        referencedColumnNames: "ci_id",
        referencedTableName: "checklist_item"
      )
    }
  changeSet(author: "samhepburn (manual)", id: "i202110141531") {
    addColumn(tableName: "publication_request") {
      column(name: "pr_group_fk", type: "VARCHAR(36)")
    }
  }

  changeSet(author: "samhepburn (manual)", id: "2021-10-19-1634-001") {
    modifyDataType( 
        tableName: "publication_request", 
        columnName: "pr_request_date", 
        newDataType: "timestamp", 
        confirm: "Successfully updated the pr_request_date column."
      )
  }

    changeSet(author: "samhepburn (manual)", id: "2021-10-28-1633-001") {
      addColumn(tableName: "party") {
        column(name: "p_full_name", type: "VARCHAR(255)")
      }
    }

    changeSet(author: "samhepburn (manual)", id: "2021-11-02-0929-001") {
      addColumn(tableName: "publication_request") {
        column(name: "pr_request_contact_fk", type: "VARCHAR(36)")
      }
      addForeignKeyConstraint(baseColumnNames: "pr_request_contact_fk",
        baseTableName: "publication_request",
        constraintName: "publication_request_contact_fk",
        deferrable: "false",
        initiallyDeferred: "false",
        referencedColumnNames: "rp_id",
        referencedTableName: "request_party")
    }
    
    changeSet(author: "samhepburn (manual)", id: "2021-11-02-1142-002") {
      dropForeignKeyConstraint(baseTableName: "request_party", constraintName: "publication_request_fk")
      dropColumn(columnName: "rp_publication_request_fk", tableName: "request_party")
  }

  changeSet(author: "efreestone (manual)", id: "2021-12-09-1507-001") {
    renameColumn(
      tableName: "publication_request",
      oldColumnName: "pr_date_modified",
      newColumnName: "pr_last_updated"
    )

    renameColumn(
      tableName: "publication_request_history",
      oldColumnName: "prh_date_modified",
      newColumnName: "prh_last_updated"
    )
  }

  changeSet(author: "efreestone (manual)", id: "2021-11-11-1239-002") {
    modifyDataType( 
      tableName: "publication_request", 
      columnName: "pr_last_updated", 
      newDataType: "timestamp", 
      confirm: "Successfully updated the pr_last_updated column."
    )
  }

  changeSet(author: "efreestone (manual)", id: "2021-11-11-1239-004") {
    modifyDataType( 
      tableName: "publication_request_history", 
      columnName: "prh_last_updated", 
      newDataType: "timestamp", 
      confirm: "Successfully updated the prh_last_updated column."
    )
  }

  changeSet(author: "efreestone (manual)", id: "2021-11-11-1239-005") {
    modifyDataType( 
      tableName: "publication_request", 
      columnName: "pr_date_created", 
      newDataType: "timestamp", 
      confirm: "Successfully updated the pr_date_created column."
    )
  }

  changeSet(author: "efreestone (manual)", id: "2021-12-09-1153-001") {
    modifyDataType( 
      tableName: "publication_status",
      columnName: "ps_status_date",
      newDataType: "timestamp",
      confirm: "Successfully updated the ps_status_date column."
    )
  }

  changeSet(author: "efreestone (manual)", id: "2021-12-09-1153-003") {
    createTable(tableName: "funding") {
      column(name: "version", type: "BIGINT") {
        constraints(nullable: "false")
      }
      column(name: "f_id", type: "VARCHAR(36)")
      column(name: "f_funder_fk", type: "VARCHAR(36)")
      column(name: "f_last_updated", type: "timestamp")
      column(name: "f_aspect_funded", type: "VARCHAR(36)")
    }
  }

  changeSet(author: "efreestone (manual)", id: "2021-12-09-1153-004") {
    addColumn(tableName: "funding") {
      column(name: "f_owner_fk", type: "VARCHAR(36)")
    }
  }

  changeSet(author: "samhepburn (manual)", id: "2021-12-09-1153-005") {
    addForeignKeyConstraint(baseColumnNames: "f_owner_fk",
      baseTableName: "funding",
      constraintName: "funding_owner_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "pr_id",
      referencedTableName: "publication_request"
    )
  }

  // DON'T USE DATE COL TYPE--USE TIMESTAMP
  changeSet(author: "ianibbo (manual)", id: "2021-12-17-1216-001") {
    createTable(tableName: "correspondence") {
      column(name: "version", type: "BIGINT") {
        constraints(nullable: "false")
      }
      column(name: "prc_id", type: "VARCHAR(36)")
      column(name: "prc_owner_fk", type: "VARCHAR(36)")
      column(name: "prc_last_updated", type: "timestamp")
      column(name: "prc_date_of_correspondence", type: "DATE")
      column(name: "prc_content", type: "TEXT")
      column(name: "prc_correspondent", type: "VARCHAR(256)")
      column(name: "prc_status_fk", type: "VARCHAR(36)")
      column(name: "prc_mode_fk", type: "VARCHAR(36)")
      column(name: "prc_category_fk", type: "VARCHAR(36)")
    }
  }

  changeSet(author: "ianibbo (manual)", id: "2022-01-04-1556-001") {
    createTable(tableName: "publication_request_agreement") {
      column(name: "rol_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "rol_version", type: "BIGINT") { constraints(nullable: "false") }
      column(name: "rol_remote_id", type: "VARCHAR(50)") { constraints(nullable: "false") }
      column(name: "pra_owner", type: "VARCHAR(36)") { constraints(nullable: "false") }
    }

    addColumn(tableName: "publication_request") {
      column(name: "pr_agreement_reference", type: "VARCHAR(36)")
    }
  }

  changeSet(author: "efreestone (manual)", id: "2022-01-13-1515-001") {
    addColumn(tableName: "publication_request") {
      column(name: "pr_without_agreement", type: "BOOLEAN")
    }
  }

  changeSet(author: "efreestone (manual)", id: "2022-01-13-1515-002") {
    modifyDataType( 
      tableName: "correspondence",
      columnName: "prc_date_of_correspondence",
      newDataType: "timestamp",
      confirm: "Successfully updated the prc_date_of_correspondence column."
    )
  }

  changeSet(author: "efreestone (manual)", id: "2022-01-14-1513-001") {
    grailsChange {
      change {
        sql.execute("UPDATE ${database.defaultSchemaName}.publication_request SET pr_without_agreement = false".toString());
      }
    }
  }

  // Add foreign key constraints to those columns which need them
  changeSet(author: "efreestone (manual)", id: "2022-02-03-0933-001") {
    addForeignKeyConstraint(
      baseColumnNames: "pr_pub_type_fk",
      baseTableName: "publication_request",
      constraintName: "publication_request_publication_type_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "rdv_id",
      referencedTableName: "refdata_value"
    )
  }

  changeSet(author: "efreestone (manual)", id: "2022-02-03-0933-002") {
    addForeignKeyConstraint(
      baseColumnNames: "prh_owner_fk",
      baseTableName: "publication_request_history",
      constraintName: "publication_request_history_owner_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "pr_id",
      referencedTableName: "publication_request"
    )
  }

  changeSet(author: "efreestone (manual)", id: "2022-02-03-0933-004") {
    addForeignKeyConstraint(
      baseColumnNames: "ti_type_fk",
      baseTableName: "title_instance",
      constraintName: "title_instance_type_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "rdv_id",
      referencedTableName: "refdata_value"
    )
  }

  changeSet(author: "efreestone (manual)", id: "2022-02-03-0933-005") {
    addForeignKeyConstraint(
      baseColumnNames: "ti_subtype_fk",
      baseTableName: "title_instance",
      constraintName: "title_instance_subtype_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "rdv_id",
      referencedTableName: "refdata_value"
    )
  }

  changeSet(author: "efreestone (manual)", id: "2022-02-03-0933-006") {
    addForeignKeyConstraint(
      baseColumnNames: "ti_publication_type_fk",
      baseTableName: "title_instance",
      constraintName: "title_instance_publication_type_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "rdv_id",
      referencedTableName: "refdata_value"
    )
  }

  changeSet(author: "efreestone (manual)", id: "2022-02-03-0933-007") {
    addForeignKeyConstraint(
      baseColumnNames: "io_status_fk",
      baseTableName: "identifier_occurrence",
      constraintName: "identifier_occurrence_status_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "rdv_id",
      referencedTableName: "refdata_value"
    )
  }

  // Checklist stuff is later dropped ready for separate checklist workflow domain classes
  changeSet(author: "efreestone (manual)", id: "2022-02-03-0933-008") {
    addForeignKeyConstraint(
      baseColumnNames: "cgi_status_fk",
      baseTableName: "checklist_group_item",
      constraintName: "checklist_group_item_status_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "rdv_id",
      referencedTableName: "refdata_value"
    )
  }

  changeSet(author: "efreestone (manual)", id: "2022-02-03-0933-009") {
    addForeignKeyConstraint(
      baseColumnNames: "f_funder_fk",
      baseTableName: "funding",
      constraintName: "funding_funder_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "rdv_id",
      referencedTableName: "refdata_value"
    )
  }

  changeSet(author: "efreestone (manual)", id: "2022-02-03-0933-010") {
    addForeignKeyConstraint(
      baseColumnNames: "f_aspect_funded",
      baseTableName: "funding",
      constraintName: "funding_aspect_funded_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "rdv_id",
      referencedTableName: "refdata_value"
    )
  }

  changeSet(author: "efreestone (manual)", id: "2022-02-03-0933-011") {
    addForeignKeyConstraint(
      baseColumnNames: "prc_status_fk",
      baseTableName: "correspondence",
      constraintName: "correspondence_status_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "rdv_id",
      referencedTableName: "refdata_value"
    )
  }

  changeSet(author: "efreestone (manual)", id: "2022-02-03-0933-012") {
    addForeignKeyConstraint(
      baseColumnNames: "prc_mode_fk",
      baseTableName: "correspondence",
      constraintName: "correspondence_mode_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "rdv_id",
      referencedTableName: "refdata_value"
    )
  }

  changeSet(author: "efreestone (manual)", id: "2022-02-03-0933-013") {
    addForeignKeyConstraint(
      baseColumnNames: "prc_category_fk",
      baseTableName: "correspondence",
      constraintName: "correspondence_category_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "rdv_id",
      referencedTableName: "refdata_value"
    )
  }

  changeSet(author: "efreestone (manual)", id: "2022-02-03-1110-001") {
    createTable(tableName: "charge") {
      column(name: "version", type: "BIGINT") {
        constraints(nullable: "false")
      }
      column(name: "ch_id", type: "VARCHAR(36)")
      column(name: "ch_amount_fk", type: "VARCHAR(36)")
      column(name: 'ch_exchange_rate_fk', type: "VARCHAR(36)")
      column(name: "ch_description", type: "TEXT")
      column(name: "ch_discount", type: "NUMBER(19,2)") // This could be an amount, £13.50, or a percentage, 30%
      column(name: "ch_discount_type_fk", type: "VARCHAR(36)")
      column(name: "ch_category_fk", type: "VARCHAR(36)")
      column(name: "ch_owner_fk", type: "VARCHAR(36)")
    }
  }

  changeSet(author: "efreestone (manual)", id: "2022-02-03-1110-002") {
    addForeignKeyConstraint(
      baseColumnNames: "ch_category_fk",
      baseTableName: "charge",
      constraintName: "charge_category_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "rdv_id",
      referencedTableName: "refdata_value"
    )
  }

  changeSet(author: "efreestone (manual)", id: "2022-02-03-1110-003") {
    addForeignKeyConstraint(
      baseColumnNames: "rp_role",
      baseTableName: "request_party",
      constraintName: "request_party_role_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "rdv_id",
      referencedTableName: "refdata_value"
    )
  }

  changeSet(author: "efreestone (manual)", id: "2022-02-03-1110-004") {
    createTable(tableName: "monetary_value") {
      column(name: "version", type: "BIGINT") {
        constraints(nullable: "false")
      }
      column(name: "id", type: "VARCHAR(36)")
      column(name: "basecurrency", type: "VARCHAR(36)")
      column(name: "monval", type: "NUMBER(19,2)")
    }

    addPrimaryKey(columnNames: "id", constraintName: "monetary_valuePK", tableName: "monetary_value")
  }

  changeSet(author: "efreestone (manual)", id: "2022-02-04-0940-001") {
    addForeignKeyConstraint(
      baseColumnNames: "ch_amount_fk",
      baseTableName: "charge",
      constraintName: "charge_amount_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "id",
      referencedTableName: "monetary_value"
    )
  }

  changeSet(author: "efreestone (manual)", id: "2022-02-04-0940-002") {
    addForeignKeyConstraint(
      baseColumnNames: "ch_discount_type_fk",
      baseTableName: "charge",
      constraintName: "charge_discount_type_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "rdv_id",
      referencedTableName: "refdata_value"
    )
  }

  changeSet(author: "efreestone (manual)", id: "2022-02-04-0940-003") {
    addForeignKeyConstraint(
      baseColumnNames: "ch_owner_fk",
      baseTableName: "charge",
      constraintName: "charge_owner_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "pr_id",
      referencedTableName: "publication_request"
    )
  }

  changeSet(author: "efreestone (manual)", id: "2022-02-04-1202-001") {
    createTable(tableName: "exchange_rate") {
      column(name: "version", type: "BIGINT") {
        constraints(nullable: "false")
      }
      column(name: "id", type: "VARCHAR(36)")
      column(name: "from_currency", type: "VARCHAR(36)")
      column(name: "to_currency", type: "VARCHAR(36)")
      column(name: "coefficient", type: "NUMBER(20,10)")
    }

    addPrimaryKey(columnNames: "id", constraintName: "exchange_ratePK", tableName: "exchange_rate")
  }

  changeSet(author: "efreestone (manual)", id: "2022-02-04-1202-002") {
    addForeignKeyConstraint(
      baseColumnNames: "ch_exchange_rate_fk",
      baseTableName: "charge",
      constraintName: "charge_exchange_rate_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "id",
      referencedTableName: "exchange_rate"
    )
  }

  changeSet(author: "ianibbo (manual)", id: "2022-02-15-1339-001") {
    addColumn(tableName: "charge") {
      column(name: "ch_invoice_reference", type: "VARCHAR(36)")
      column(name: "ch_invoice_line_item_reference", type: "VARCHAR(36)")
    }
  }

  changeSet(author: "ianibbo (manual)", id: "2022-02-22-1438-001") {
    addColumn(tableName: "charge") {
      column(name: "ch_payer_fk", type: "VARCHAR(36)")
      column(name: "ch_discount_note", type: "TEXT")
      column(name: "ch_tax", type: "NUMBER(20,10)")
      column(name: "ch_payer_note", type: "TEXT")
      column(name: "ch_charge_status_fk", type: "VARCHAR(36)")
    }
  }

  changeSet(author: "efreestone (manual)", id: "2022-03-16-1005-001") {
    addColumn(tableName: "publication_request") {
      column(name: "pr_work_fk", type: "VARCHAR(36)")
    }
  }

  changeSet(author: "efreestone (manual)", id: "2022-03-16-1005-002") {
    addPrimaryKey(columnNames: "w_id", constraintName: "workPK", tableName: "work")
  }

  changeSet(author: "efreestone (manual)", id: "2022-03-16-1005-003") {
    addForeignKeyConstraint(
      baseColumnNames: "pr_work_fk",
      baseTableName: "publication_request",
      constraintName: "work_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "w_id",
      referencedTableName: "work"
    )
  }

  changeSet(author: "efreestone (manual)", id: "2022-03-16-1054-001") {

    createTable(tableName: "alternate_email_address") {
      column(name: "aea_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "aea_version", type: "BIGINT") { constraints(nullable: "false") }
      column(name: "aea_email", type: "VARCHAR(255)") { constraints(nullable: "false") }
      column(name: "aea_owner_fk", type: "VARCHAR(36)")
    }

    addPrimaryKey(columnNames: "aea_id", constraintName: "alternate_email_addressPK", tableName: "alternate_email_address")
    
    addForeignKeyConstraint(
      baseColumnNames: "aea_owner_fk",
      baseTableName: "alternate_email_address",
      constraintName: "aea_to_party_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "p_id",
      referencedTableName: "party"
    )
  }

  // Setup addresses
  changeSet(author: "efreestone (manual)", id: "20220408-1351-001") {
    createTable(tableName: "address") {
      column(name: "add_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "add_version", type: "BIGINT") { constraints(nullable: "false") }
      column(name: "add_name", type: "VARCHAR(255)")
      column(name: "add_address_line_one", type: "VARCHAR(255)")
      column(name: "add_address_line_two", type: "VARCHAR(255)")
      column(name: "add_city", type: "VARCHAR(255)")
      column(name: "add_region", type: "VARCHAR(255)")
      column(name: "add_postal_code", type: "VARCHAR(255)")
      column(name: "add_country", type: "VARCHAR(255)")
    }

    addPrimaryKey(columnNames: "add_id", constraintName: "address_PK", tableName: "address")
  }

  changeSet(author: "efreestone (manual)", id: "20220408-1351-002") {
    createTable(tableName: "party_address") {
      column(name: "padd_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "padd_version", type: "BIGINT") { constraints(nullable: "false") }
      column(name: "padd_address_fk", type: "VARCHAR(36)")
      column(name: "padd_owner_fk", type: "VARCHAR(36)")
    }

    addPrimaryKey(columnNames: "padd_id", constraintName: "party_address_PK", tableName: "party_address")

    addForeignKeyConstraint(
      baseColumnNames: "padd_address_fk",
      baseTableName: "party_address",
      constraintName: "padd_to_address_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "add_id",
      referencedTableName: "address"
    )

    addForeignKeyConstraint(
      baseColumnNames: "padd_owner_fk",
      baseTableName: "party_address",
      constraintName: "padd_to_party_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "p_id",
      referencedTableName: "party"
    )
  }

  changeSet(author: "efreestone (manual)", id: "20220408-1351-003") {
    addColumn (tableName: "party" ) {
      column(name: "p_street_address_fk", type: "VARCHAR(36)")
    }

    addForeignKeyConstraint(
      baseColumnNames: "p_street_address_fk",
      baseTableName: "party",
      constraintName: "p_to_party_address_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "padd_id",
      referencedTableName: "party_address"
    )
  }

  changeSet(author: "efreestone (manual)", id: "20220419-1210-001") {
    addColumn (tableName: "publication_request" ) {
      column(name: "pr_book_date_of_publication", type: "VARCHAR(36)")
    }

    addColumn (tableName: "publication_request" ) {
      column(name: "pr_book_place_of_publication", type: "VARCHAR(255)")
    }
  }

  changeSet(author: "efreestone (manual)", id: "2022-04-29-1232-001") {
    addColumn (tableName: "work" ) {
      column(name: "w_indexed_in_doaj_fk", type: "VARCHAR(36)")
    }
  }

  changeSet(author: "efreestone (manual)", id: "2022-04-29-1232-002") {
    addForeignKeyConstraint(
      baseColumnNames: "w_indexed_in_doaj_fk",
      baseTableName: "work",
      constraintName: "work_indexed_in_doaj_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "rdv_id",
      referencedTableName: "refdata_value"
    )
  }

  changeSet(author: "efreestone (manual)", id: "2022-04-29-1232-003") {
    addColumn (tableName: "work" ) {
      column(name: "w_oa_status_fk", type: "VARCHAR(36)")
    }
  }

  changeSet(author: "efreestone (manual)", id: "2022-04-29-1232-004") {
    addForeignKeyConstraint(
      baseColumnNames: "w_oa_status_fk",
      baseTableName: "work",
      constraintName: "work_oa_status_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "rdv_id",
      referencedTableName: "refdata_value"
    )
  }

  changeSet(author: "efreestone (manual)", id: "2022-04-29-1232-005") {
    addColumn (tableName: "publication_request" ) {
      column(name: "pr_work_indexed_in_doaj_fk", type: "VARCHAR(36)")
    }
  }

  changeSet(author: "efreestone (manual)", id: "2022-04-29-1232-006") {
    addForeignKeyConstraint(
      baseColumnNames: "pr_work_indexed_in_doaj_fk",
      baseTableName: "publication_request",
      constraintName: "publication_request_work_indexed_in_doaj_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "rdv_id",
      referencedTableName: "refdata_value"
    )
  }

  changeSet(author: "efreestone (manual)", id: "2022-04-29-1232-007") {
    addColumn (tableName: "publication_request" ) {
      column(name: "pr_work_oa_status_fk", type: "VARCHAR(36)")
    }
  }

  changeSet(author: "efreestone (manual)", id: "2022-04-29-1232-008") {
    addForeignKeyConstraint(
      baseColumnNames: "pr_work_oa_status_fk",
      baseTableName: "publication_request",
      constraintName: "publication_request_work_oa_status_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "rdv_id",
      referencedTableName: "refdata_value"
    )
  }

  changeSet(author: "Jack-Golding (manual)", id: "2022-05-31-1052-001") {
    addForeignKeyConstraint(
      baseColumnNames: "pr_license",
      baseTableName: "publication_request",
      constraintName: "publication_request_license_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "rdv_id",
      referencedTableName: "refdata_value"
    )
  }

  changeSet(author: "Jack-Golding (manual)", id: "2022-05-31-1052-002") {
    addForeignKeyConstraint(
      baseColumnNames: "pr_publisher",
      baseTableName: "publication_request",
      constraintName: "publication_request_publisher_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "rdv_id",
      referencedTableName: "refdata_value"
    )
  }

  changeSet(author: "Jack-Golding (manual)", id: "2022-05-31-1052-003") {
    addForeignKeyConstraint(
      baseColumnNames: "pr_subtype",
      baseTableName: "publication_request",
      constraintName: "publication_request_subtype_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "rdv_id",
      referencedTableName: "refdata_value"
    )
  }

  changeSet(author: "Jack-Golding (manual)", id: "2022-05-31-1052-004") {
    addForeignKeyConstraint(
      baseColumnNames: "ps_publication_status",
      baseTableName: "publication_status",
      constraintName: "publication_status_publication_status_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "rdv_id",
      referencedTableName: "refdata_value"
    )
  }

  changeSet(author: "Jack-Golding (manual)", id: "2022-05-31-1052-005") {
    addForeignKeyConstraint(
      baseColumnNames: "pi_type",
      baseTableName: "publication_identifier",
      constraintName: "publication_identifier_type_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "rdv_id",
      referencedTableName: "refdata_value"
    )
  }

  changeSet(author: "Jack-Golding (manual)", id: "2022-06-08-1555-001") {
    createTable(tableName: "payer") {
      column(name: "cpy_id", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "cpy_version", type: "BIGINT") { constraints(nullable: "false") }
      column(name: "cpy_payer_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
      column(name: "cpy_payer_amount", type: "NUMBER(19,2)") { constraints(nullable: "false") }
      column(name: "cpy_payer_note", type: "TEXT") { constraints(nullable: "true") }
      column(name: "cpy_owner_fk", type: "VARCHAR(36)") { constraints(nullable: "false") }
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "2022-06-14-1659-001") {
      dropColumn(columnName: "ch_payer_note", tableName: "charge")
      dropColumn(columnName: "ch_payer_fk", tableName: "charge")
  }

  changeSet(author: "Jack-Golding (manual)", id: "2022-06-16-1453-001") {
      addColumn (tableName: "charge" ) {
        column(name: "ch_date_created", type: "timestamp")
        column(name: "ch_last_updated", type: "timestamp")
    }
  }

  // DROP Checklist stuff ready for separate checklist workflow domain classes
  changeSet(author: "EFreestone (manual)", id:"2022-07-07-1449-001") {
    dropTable(tableName: "checklist_group_item")
  }

  changeSet(author: "EFreestone (manual)", id:"2022-07-07-1449-002") {
    dropTable(tableName: "checklist_group")
  }

  changeSet(author: "EFreestone (manual)", id:"2022-07-07-1449-003") {
    dropTable(tableName: "checklist_item")
  }

  changeSet(author: "Jack-Golding (manual)", id:"2202-08-15-1351-001") {
    addColumn (tableName: "party") {
      column(name: "p_faculty_fk", type:"VARCHAR(36)")
      column(name: "p_department", type:"VARCHAR(255)")
    }
  }

  changeSet(author: "Jack-Golding (manual)", id:"2202-08-15-1355-002") {
     addForeignKeyConstraint(
      baseColumnNames: "p_faculty_fk",
      baseTableName: "party",
      constraintName: "party_faculty_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "rdv_id",
      referencedTableName: "refdata_value"
    )
  }

  changeSet(author: "Jack-Golding (manual)", id:"2202-08-15-1359-003") {
    addColumn (tableName: "publication_request") {
      column(name: "pr_corresponding_faculty_fk", type:"VARCHAR(36)")
      column(name: "pr_corresponding_department", type:"VARCHAR(255)")
    }
  }

  changeSet(author: "Jack-Golding (manual)", id:"2202-08-15-1400-004") {
     addForeignKeyConstraint(
      baseColumnNames: "pr_corresponding_faculty_fk",
      baseTableName: "publication_request",
      constraintName: "publication_request_corresponding_faculty_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "rdv_id",
      referencedTableName: "refdata_value"
    )
  }

  changeSet(author: "Jack-Golding (manual)", id:"2022-09-23-1430-001"){
    addColumn(tableName: "charge") {
      column(name: "ch_payment_period", type: "VARCHAR(36)")
    }
  }

  changeSet(author: "Jack-Golding (manual)", id:"2022-09-27-1137-001"){
    addColumn (tableName: "publication_request") {
      column(name: "pr_corresponding_institution_level_1_fk", type:"VARCHAR(36)")
      column(name: "pr_corresponding_institution_level_2", type:"VARCHAR(255)")
  }
}

  changeSet(author: "Jack-Golding (manual)", id:"2022-09-27-1139-002") {
     addForeignKeyConstraint(
      baseColumnNames: "pr_corresponding_institution_level_1_fk",
      baseTableName: "publication_request",
      constraintName: "publication_request_corresponding_institution_level_1_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "rdv_id",
      referencedTableName: "refdata_value"
    )
  }

  changeSet(author: "Jack-Golding (manual)", id:"2022-09-27-1141-003") {
    addColumn (tableName: "party") {
      column(name: "p_institution_level_1_fk", type:"VARCHAR(36)")
      column(name: "p_institution_level_2", type:"VARCHAR(255)")
    }
  }

  changeSet(author: "Jack-Golding (manual)", id:"2022-09-27-1142-004") {
     addForeignKeyConstraint(
      baseColumnNames: "p_institution_level_1_fk",
      baseTableName: "party",
      constraintName: "party_institution_level_1_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "rdv_id",
      referencedTableName: "refdata_value"
    )
  }

  changeSet(author: "Jack-Golding (manual)", id: "2022-09-28-1333-001"){
    grailsChange{
      change{
        sql.rows("SELECT p_id, p_faculty_fk, p_department FROM ${database.defaultSchemaName}.party".toString()).each {
          sql.execute("UPDATE ${database.defaultSchemaName}.party SET p_institution_level_1_fk = :instL1, p_institution_level_2 = :instL2 WHERE p_id = :pId".toString(),
           [pId: it.p_id, instL1: it.p_faculty_fk, instL2: it.p_department])
        }
      }
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "2022-09-28-1333-02"){
    grailsChange{
      change{
        sql.rows("SELECT pr_id, pr_corresponding_faculty_fk, pr_corresponding_department FROM ${database.defaultSchemaName}.publication_request".toString()).each {
          sql.execute("UPDATE ${database.defaultSchemaName}.publication_request SET pr_corresponding_institution_level_1_fk = :corrInstL1, pr_corresponding_institution_level_2 = :corrInstL2 WHERE pr_id = :prId".toString(), 
           [prId: it.pr_id, corrInstL1: it.pr_corresponding_faculty_fk, corrInstL2: it.pr_corresponding_department])
        }
      }
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "2022-09-28-1344-003") {
    dropForeignKeyConstraint(baseTableName: "party", constraintName: "party_faculty_fk")
    dropColumn(columnName: "p_faculty_fk", tableName: "party")
    dropColumn(columnName: "p_department", tableName: "party")
  }

  changeSet(author: "Jack-Golding (manual)", id: "2022-09-28-1344-004") {
    dropForeignKeyConstraint(baseTableName: "publication_request", constraintName: "publication_request_corresponding_faculty_fk")
    dropColumn(columnName: "pr_corresponding_faculty_fk", tableName: "publication_request")
    dropColumn(columnName: "pr_corresponding_department", tableName: "publication_request")
  }

  changeSet(author: "Jack-Golding (manual)", id: "2022-10-27") {
    addUniqueConstraint(columnNames: "id_value,id_ns_fk", constraintName: "identifier_ns_value_unique", tableName: "identifier")
  }

  changeSet(author: "Jack-Golding (manual)", id: "2022-11-08") {
    addColumn(tableName: "publication_request") {
      column(name: "pr_retrospective_oa", type: "BOOLEAN")
    }
  }

  changeSet(author: "Jack-Golding (manual)", id:"2022-11-24-1140-001"){
    addColumn (tableName: "publication_request") {
      column(name: "pr_closure_reason_fk", type:"VARCHAR(36)")
    }
  }

  changeSet(author: "Jack-Golding (manual)", id:"2022-11-24-1141-002") {
     addForeignKeyConstraint(
      baseColumnNames: "pr_closure_reason_fk",
      baseTableName: "publication_request",
      constraintName: "publication_request_closure_reason_fk",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "rdv_id",
      referencedTableName: "refdata_value"
    )
  }

  changeSet(author: "Jack-Golding (manual)", id: "2022-11-24-1143-03"){
    grailsChange{
      change{
        sql.rows("SELECT pr_id, pr_rejection_reason FROM ${database.defaultSchemaName}.publication_request".toString()).each {
          sql.execute("UPDATE ${database.defaultSchemaName}.publication_request SET pr_closure_reason_fk = :rejReason WHERE pr_id = :prId".toString(), 
           [prId: it.pr_id, rejReason: it.pr_rejection_reason])
        }
      }
    }
  }

  changeSet(author: "Jack-Golding (manual)", id: "2022-11-24-1146-004") {
    dropColumn(columnName: "pr_rejection_reason", tableName: "publication_request")
  }

}
