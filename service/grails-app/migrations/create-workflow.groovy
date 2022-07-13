databaseChangeLog = {

  // WORKFLOW
  changeSet(author: "efreestone (manual)", id: "2022-07-07-1437-001") {
    createTable(tableName: "workflow") {
      column(name: "id", type: "VARCHAR(36)") {
        constraints(nullable: "false")
      }

      column(name: "version", type: "BIGINT") {
        constraints(nullable: "false")
      }
    }
  }

  changeSet(author: "efreestone (manual)", id: "2022-07-07-1437-002") {
    addPrimaryKey(columnNames: "id", constraintName: "workflowPK", tableName: "workflow")
  }

  // CHECKLIST ITEM DEFINITION
  changeSet(author: "efreestone (manual)", id: "2022-07-07-1045-003") {
    createTable(tableName: "checklist_item_definition") {
      column(name: "clid_id", type: "VARCHAR(36)") {
        constraints(nullable: "false")
      }

      column(name: "version", type: "BIGINT") {
        constraints(nullable: "false")
      }

      column(name: "clid_name", type: "VARCHAR(255)") {
        constraints(nullable: "false")
      }

      column(name: "clid_label", type: "VARCHAR(255)") {
        constraints(nullable: "false")
      }

      column(name: "clid_description", type: "TEXT")

      column(name: "clid_weight", type: "INT") {
        constraints(nullable: "false")
      }

      column(name: "clid_date_created", type: "TIMESTAMP")
      column(name: "clid_last_updated", type: "TIMESTAMP")
    }
  }

  changeSet(author: "efreestone (manual)", id: "2022-07-07-1045-004") {
    addPrimaryKey(columnNames: "clid_id", constraintName: "checklist_definitionPK", tableName: "checklist_item_definition")
  }

  changeSet(author: "efreestone (manual)", id: "2022-07-07-1045-005") {
    createIndex(indexName: "clid_label_idx", tableName: "checklist_item_definition") {
      column(name: "clid_label")
    }
  }

  changeSet(author: "efreestone (manual)", id: "2022-07-07-1045-006") {
    createIndex(indexName: "clid_name_idx", tableName: "checklist_item_definition") {
      column(name: "clid_name")
    }
  }

  changeSet(author: "efreestone (manual)", id: "2022-07-07-1045-007") {
    createIndex(indexName: "clid_weight_idx", tableName: "checklist_item_definition") {
      column(name: "clid_weight")
    }
  }

  // CHECKLIST ITEM
  changeSet(author: "efreestone (manual)", id: "2022-07-07-1045-008") {
    createTable(tableName: "checklist_item") {
      column(name: "cli_id", type: "VARCHAR(36)") {
        constraints(primaryKey: "true", primaryKeyName: "checklist_itemPK")
      }

      column(name: "version", type: "BIGINT") {
        constraints(nullable: "false")
      }

      column(name: "cli_date_created", type: "TIMESTAMP")
      column(name: "cli_last_updated", type: "TIMESTAMP")

      column(name: "cli_definition_fk", type: "VARCHAR(36)")
      column(name: "cli_parent_fk", type: "VARCHAR(36)")

      column(name: 'cli_outcome_fk', type: 'VARCHAR(36)')
      column(name: 'cli_status_fk', type: 'VARCHAR(36)')
    }
  }

  changeSet(author: "efreestone (manual)", id: "2022-07-07-1045-009") {
    addForeignKeyConstraint(baseColumnNames: "cli_definition_fk", baseTableName: "checklist_item", constraintName: "checklistItem_checklistDefinitionFK", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "clid_id", referencedTableName: "checklist_item_definition")
  }

  changeSet(author: "efreestone (manual)", id: "2022-07-07-1045-010") {
    addForeignKeyConstraint(baseColumnNames: "cli_parent_fk", baseTableName: "checklist_item", constraintName: "checklistItem_workflowFK", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "workflow")
  }

  // CHECKLIST ITEM NOTE
  changeSet(author: "efreestone (manual)", id: "2022-07-07-1045-011") {
    createTable(tableName: "checklist_item_note") {
      column(name: "clin_id", type: "VARCHAR(36)") {
        constraints(nullable: "false")
      }

      column(name: "version", type: "BIGINT") {
        constraints(nullable: "false")
      }

      column(name: "clin_date_created", type: "TIMESTAMP")
      column(name: "clin_last_updated", type: "TIMESTAMP")

      column(name: 'clin_parent_fk', type: 'VARCHAR(36)')

      column(name: 'clin_note', type: 'VARCHAR(255)')
    }
  }

  changeSet(author: "efreestone (manual)", id: "2022-07-07-1045-012") {
    addPrimaryKey(columnNames: "clin_id", constraintName: "checklist_item_notePK", tableName: "checklist_item_note")
  }

  changeSet(author: "efreestone (manual)", id: "2022-07-07-1045-013") {
    addForeignKeyConstraint(baseColumnNames: "clin_parent_fk", baseTableName: "checklist_item_note", constraintName: "checklistItemNote_checklistItemFK", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "cli_id", referencedTableName: "checklist_item")
  }

  changeSet(author: "efreestone (manual)", id: "2022-07-13-1154-001") {
    addUniqueConstraint(
      tableName: "checklist_item",
      columnNames: "cli_parent_fk,cli_definition_fk",
      constraintName: "checklist_item_unique_parent_definition"
    )
  }

}