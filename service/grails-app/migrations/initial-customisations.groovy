databaseChangeLog = {

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
            }        }
    }
}
