// OA-Specific workflow migrations
databaseChangeLog = {
  // Foreign key link checklistItem outcome/status to OA refdata tables
  changeSet(author: "efreestone (manual)", id: "2022-07-07-1137-002") {
    addForeignKeyConstraint(
      baseColumnNames: "cli_outcome_fk",
      baseTableName: "checklist_item",
      constraintName: "checklistItem_outcomeFK",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "rdv_id",
      referencedTableName: "refdata_value"
    )
  }

  changeSet(author: "efreestone (manual)", id: "2022-07-07-1137-003") {
    addForeignKeyConstraint(
      baseColumnNames: "cli_status_fk",
      baseTableName: "checklist_item",
      constraintName: "checklistItem_statusFK",
      deferrable: "false",
      initiallyDeferred: "false",
      referencedColumnNames: "rdv_id",
      referencedTableName: "refdata_value"
    )
  }

  changeSet(author: "efreestone (manual)", id: "2022-07-13-1012-001") {
    grailsChange {
      change {
        sql.eachRow("SELECT pr_id, version FROM ${database.defaultSchemaName}.publication_request".toString()) { def row ->
          // Create workflow for each PR.
          sql.executeInsert("""
            INSERT INTO ${database.defaultSchemaName}.workflow (id, version)
                VALUES (:id, :version);
          """.toString(), [id: row.pr_id, version: row.version])
        }
      }
    }
  }

  // Drop version column from PR ready for usage with Workflow
  changeSet(author: "efreestone (manual)", id: "2022-07-13-1012-002") {
        dropColumn(columnName: "version", tableName: "publication_request")

  }
}