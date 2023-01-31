databaseChangeLog = {
  changeSet(author: "Jack-Golding (manual)", id: "2023-01-31-1520-001") {
    modifyDataType( 
      tableName: "party",
      columnName: "p_main_email",
      newDataType: "VARCHAR(255)",
      confirm: "Successfully updated the p_main_email column."
    )
  }
}
