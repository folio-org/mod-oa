databaseChangeLog = {
  changeSet(author: "Jack-Golding (manual)", id: "2023-01-31-1520-001") {
    modifyDataType( 
      tableName: "party",
      columnName: "p_main_email",
      newDataType: "VARCHAR(255)",
      confirm: "Successfully updated the p_main_email column."
    )
  }

  changeSet(author: "Jack-Golding (manual)", id: "2023-02-21-1618-001") {
    modifyDataType( 
      tableName: "publication_request",
      columnName: "pr_title",
      newDataType: "VARCHAR(4096)",
      confirm: "Successfully updated the pr_title column."
    )
  }

  changeSet(author: "Jack-Golding (manual)", id: "2023-06-15-1608-001") {
    addPrimaryKey(columnNames: "ch_id", constraintName: "ch_idPK", tableName: "charge")
  }

  changeSet(author: "Jack-Golding (manual)", id: "2023-06-15-1608-002") {
    addPrimaryKey(columnNames: "prc_id", constraintName: "prc_idPK", tableName: "correspondence")
  }

  changeSet(author: "Jack-Golding (manual)", id: "2023-06-15-1608-003") {
    addPrimaryKey(columnNames: "eri_id", constraintName: "eri_idPK", tableName: "external_request_id")
  }

  changeSet(author: "Jack-Golding (manual)", id: "2023-06-15-1608-004") {
    addPrimaryKey(columnNames: "f_id", constraintName: "f_idPK", tableName: "funding")
  }

  changeSet(author: "Jack-Golding (manual)", id: "2023-06-15-1608-005") {
    addPrimaryKey(columnNames: "id_id", constraintName: "id_idPK", tableName: "identifier")
  }

  changeSet(author: "Jack-Golding (manual)", id: "2023-06-15-1608-006") {
    addPrimaryKey(columnNames: "idns_id", constraintName: "idns_idPK", tableName: "identifier_namespace")
  }

  changeSet(author: "Jack-Golding (manual)", id: "2023-06-15-1608-007") {
    addPrimaryKey(columnNames: "io_id", constraintName: "io_idPK", tableName: "identifier_occurrence")
  }

  changeSet(author: "Jack-Golding (manual)", id: "2023-06-15-1608-008") {
    addPrimaryKey(columnNames: "p_id", constraintName: "p_idPK", tableName: "party")
  }

  changeSet(author: "Jack-Golding (manual)", id: "2023-06-15-1608-009") {
    addPrimaryKey(columnNames: "cpy_id", constraintName: "cpy_idPK", tableName: "payer")
  }

  changeSet(author: "Jack-Golding (manual)", id: "2023-06-15-1608-010") {
    addPrimaryKey(columnNames: "pr_id", constraintName: "pr_idPK", tableName: "publication_request")
  }

  changeSet(author: "Jack-Golding (manual)", id: "2023-06-15-1608-011") {
    addPrimaryKey(columnNames: "rol_id", constraintName: "rol_idPK", tableName: "publication_request_agreement")
  }

  changeSet(author: "Jack-Golding (manual)", id: "2023-06-15-1608-012") {
    addPrimaryKey(columnNames: "prh_id", constraintName: "prh_idPK", tableName: "publication_request_history")
  }

  changeSet(author: "Jack-Golding (manual)", id: "2023-06-15-1608-013") {
    addPrimaryKey(columnNames: "rp_id", constraintName: "rp_idPK", tableName: "request_party")
  }

  changeSet(author: "Jack-Golding (manual)", id: "2023-06-15-1608-014") {
    addPrimaryKey(columnNames: "sw_id", constraintName: "sw_idPK", tableName: "scholarly_work")
  }

  changeSet(author: "Jack-Golding (manual)", id: "2023-06-15-1608-014") {
    addPrimaryKey(columnNames: "ti_id", constraintName: "ti_idPK", tableName: "title_instance")
  }
}
