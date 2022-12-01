package org.olf.oa;

import grails.core.GrailsApplication
import grails.plugins.*
import grails.converters.JSON
import groovy.util.logging.Slf4j
import grails.gorm.multitenancy.CurrentTenant


import com.k_int.okapi.OkapiTenantAwareController
import com.opencsv.CSVWriterBuilder
import com.opencsv.ICSVWriter
import org.olf.oa.finance.MonetaryValue
import org.olf.oa.kb.IdentifierOccurrence
import org.olf.oa.kb.TitleInstance


/**
 * External for OA network connectivity
 */
@Slf4j
@CurrentTenant
class ReportsController {

  private static final String REPORT_QRY = '''select c
from Charge as c
where (:pp is null or c.paymentPeriod = :pp)
and ((:ccList) is null or c.category.value in (:ccList))
and ((:csList) is null or c.chargeStatus.value in (:csList))
'''

  GrailsApplication grailsApplication

  def openApcChargesReport(String institution, 
                           String paymentPeriod, 
                           String chargeCategory, 
                           String chargeStatus) {

    log.debug("openApcChargesReport(${institution},${paymentPeriod},${chargeCategory},${chargeStatus}) ${params}")

    // Set the file disposition.
    OutputStreamWriter osWriter

    try {
      response.setHeader "Content-disposition", "attachment; filename=export.csv"
      osWriter = new OutputStreamWriter(new BufferedOutputStream(response.outputStream))
      ICSVWriter csvWriter = new CSVWriterBuilder(osWriter)
        .withSeparator(ICSVWriter.DEFAULT_SEPARATOR)          // ASCII 44: ,
        .withQuoteChar(ICSVWriter.DEFAULT_QUOTE_CHARACTER)    // ASCII 34: "
        .withEscapeChar(ICSVWriter.DEFAULT_ESCAPE_CHARACTER)  // ASCII 34: "
        .withLineEnd(ICSVWriter.DEFAULT_LINE_END)             // "\n"
      .build()

      List<String> header = [ 'institution', 'period', 'euro', 'doi', 'is_hybrid', 'publisher', 'journal_full_title', 'issn', 'url' ]
      csvWriter.writeNext(header as String[])

      List<Charge> output = Charge.executeQuery(REPORT_QRY, [ pp: paymentPeriod, ccList : chargeCategory?.split(','), csList: chargeStatus?.split(',') ] )
      
      output.each { chg ->

        MonetaryValue mv = chg.getEstimatedPrice()

        List<TitleInstance> ti = chg.owner?.work?.instances?.sort { it?.subType?.value }
        
        IdentifierOccurrence io = ti[0].identifiers.find { it.identifier.ns.value == "issn" }

        List<String> datarow = [ institution, 
                                 chg.paymentPeriod, 
                                 mv?.baseCurrency?.getCurrencyCode() == 'EUR' ? mv.value : null,
                                 chg.owner.doi, 
                                 chg.owner.workOAStatus?.value == 'hybrid' ? true : false, 
                                 chg.owner.publisher?.label, 
                                 chg.owner.work?.title, 
                                 io?.identifier?.value, 
                                 chg.owner.doi == null ? chg.owner.publicationUrl : null ]
        csvWriter.writeNext(datarow as String[])
      }

      // exportService.exportLicensesAsCsv(csvWriter, exportObj)
    } finally {
      // Always close the stream.
      osWriter?.close()
    }
  }

  def openApcBcpReport(String institution, 
                       String paymentPeriod, 
                       String chargeCategory, 
                       String chargeStatus) {
    log.debug("openApcBcpReport(${institution},${paymentPeriod},${chargeCategory},${chargeStatus}) ${params}")

    // Set the file disposition.
    OutputStreamWriter osWriter

    try {
      response.setHeader "Content-disposition", "attachment; filename=export.csv"
      osWriter = new OutputStreamWriter(new BufferedOutputStream(response.outputStream))
      ICSVWriter csvWriter = new CSVWriterBuilder(osWriter)
        .withSeparator(ICSVWriter.DEFAULT_SEPARATOR)          // ASCII 44: ,
        .withQuoteChar(ICSVWriter.DEFAULT_QUOTE_CHARACTER)    // ASCII 34: "
        .withEscapeChar(ICSVWriter.DEFAULT_ESCAPE_CHARACTER)  // ASCII 34: "
        .withLineEnd(ICSVWriter.DEFAULT_LINE_END)             // "\n"
      .build()

      List<String> header = [ 'institution', 'period', 'euro', 'doi', 'backlist_oa', 'book_title', 'isbn' ]
      csvWriter.writeNext(header as String[])

      List<Charge> output = Charge.executeQuery(REPORT_QRY, [ pp: paymentPeriod, ccList : chargeCategory?.split(','), csList: chargeStatus?.split(',') ] )

      output.each { chg ->

        MonetaryValue mv = chg.getEstimatedPrice()
        PublicationIdentifier pi = chg.owner.identifiers.find { it.type.value == 'isbn' }

        List<String> datarow = [ institution,
                                 chg.paymentPeriod,
                                 mv?.baseCurrency?.getCurrencyCode() == 'EUR' ? mv.value : null,
                                 chg.owner.doi,
                                 chg.owner?.retrospectiveOA,
                                 chg.owner.publicationTitle,
                                 chg.owner.doi == null ? pi?.publicationIdentifier : null ]
        csvWriter.writeNext(datarow as String[])
      }

      // exportService.exportLicensesAsCsv(csvWriter, exportObj)
    } finally {
      // Always close the stream.
      osWriter?.close()
    }
  }


}
