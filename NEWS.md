## 3.2.0 2025-03-14
  * Incremented version and interface for Sunflower release

## 3.1.0 2024-12-04
  * MODOA-69 Missing interface dependencies in module descriptor in mod-oa
  * MODOA-66 Update module license, guidance and dependencies for mod-oa

## 3.0.1 2024-11-06
  * Fixed borked release

## 3.0.0 2024-11-06
  * MODOA-65 Review and cleanup Module Descriptor for mod-oa
  * MODOA-58 Upgrade mod-oa to Grails 6

## 2.0.1 2024-09-12
 * MODOA-63 Ensure endpoints are protected with fine-grained permissions

## 2.0.0 2023-11-02
 * Upgraded to Grails 5 (including Hibernate 5.6.x for Poppy)
 * Implemented undertow servlet and enable scheduling annotation to application.groovy
 * MODOA-57 Fixed error on attempting to render a charge
 * MODOA-51 Added primary keys to database Schema
 * Bumped web-toolkit-ce to 8.1.4
 * Bumped dependencies for commons-io, spring-webmvc and kafka-clients Refs MODOA-53/54/55

## 1.1.0 2023-03-20
 * Modified p_main_email datatype from VARCHAR(36) to VARCHAR(255). Refs MODOA-44
 * Modified pr_title datatype from VARCHAR(255) to VARCHAR(4096) to accommodate longer publication titles. Refs MODOA-46
 * Fixed Checklist item definition name regex to handle special characters and language specific characters. Refs UIOA-211
 * Bumped dependencies for postgresql, opencsv, kafka-clients, commons-io. Refs MODOA-47

## 1.0.0 2023-01-10
 * Added domain model for Publication requests and partys
 * Added domain model for Charges
 * MODOA-4 Added domain model for journals
 * MODOA-7 Added domain model for agreements and links to publication request
 * MODOA-12 Added domain model for storing book/journal details within publication request 
 * MODOA-12 Added Correspondence domain class and links to publication request
 * Added ability for Invoice and Invoice lines to be linked to charges
 * Added Checklist/Checklist item domain model and links to publication request
 * Added report contrllers for generating Open APC reports
 