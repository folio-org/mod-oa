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

  changeSet(author: "samhepburn (manual)", id: "i202109091145") {
    addColumn(tableName: "publication_request") {
      column(name: "pr_date_created", type: "DATE")
    }
  }

  changeSet(author: "samhepburn (manual)", id: "i202109091155") {
    createTable(tableName: "corresponding_author") {
      column(name: "ca_id", type: "VARCHAR(36)")
      column(name: "ca_title", type: "VARCHAR(12)")
      column(name: "ca_family_name", type: "VARCHAR(36)")
      column(name: "ca_given_names", type: "VARCHAR(36)")
      column(name: "ca_orcid_id", type: "VARCHAR(36)")
      column(name: "ca_mail_email", type: "VARCHAR(36)")
      column(name: "ca_phone", type: "VARCHAR(36)")
      column(name: "ca_mobile", type: "VARCHAR(36)")
      column(name: "version", type: "BIGINT") {
        constraints(nullable: "false")
      }
    }
  }

  changeSet(author: "ianibbo (manual)", id: "i202109091132") {
    createTable(tableName: "funder") {
      column(name: "version", type: "BIGINT") {
        constraints(nullable: "false")
      }
      column(name: "f_id", type: "VARCHAR(36)")
      column(name: "f_name", type: "VARCHAR(36)")
      column(name: "f_date_modified", type: "DATE") 
    }
  }


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

    changeSet(author: "samhepburn (manual)", id: "i20210910441") {
      addColumn(tableName: "corresponding_author") {
        column(name: "ca_owner_fk", type: "VARCHAR(36)") {
          constraints(nullable: "false")
        }
      }
      addForeignKeyConstraint(baseColumnNames: "ca_owner_fk",
        baseTableName: "corresponding_author",
        constraintName: "corresponding_author_owner_fk",
        deferrable: "false",
        initiallyDeferred: "false",
        referencedColumnNames: "pr_id",
        referencedTableName: "publication_request")
    }

    changeSet(author: "samhepburn (manual)", id: "i202109101458") {
      addColumn(tableName: "publication_request") {
        column(name: "pr_corresponding_author_fk", type: "VARCHAR(36)") {
          constraints(nullable: "true")
        }
    }
  }
}
