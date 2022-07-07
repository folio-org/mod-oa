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
}