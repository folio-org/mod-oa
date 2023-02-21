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
}
