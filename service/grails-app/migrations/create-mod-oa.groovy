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
}
