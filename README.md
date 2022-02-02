# mod-oa

Copyright (C) 2021 The Open Library Foundation

This software is distributed under the terms of the Apache License,
Version 2.0. See the file "[LICENSE](LICENSE)" for more information.

## Introduction

Bounded Context Deailing with Open Access Publishing

## Additional information

### Deployment

A sample k8s resource definition for service and deployment [can be found the scripts directory](https://github.com/folio-org/mod-oa/blob/master/scripts/k8s_deployment_template.yaml)
Or you can get the latest module descriptor from the project OKAPI - [For example - v1.0.0-SNAPSHOT65](curl http://folio-registry.aws.indexdata.com/_/proxy/modules/mod-oa-1.0.0-SNAPSHOT.65)

This module requires the following env parameters
* OKAPI_SERVICE_PORT - port number for okapi
* OKAPI_SERVICE_HOST - Host [namespace.hostname if running in a different namespace to okapi]

The following properties are understood and documented in the [Module Descriptor](https://github.com/folio-org/mod-oa/blob/master/service/src/main/okapi/ModuleDescriptor-template.json)
* DB_DATABASE
* DB_HOST
* DB_USERNAME
* DB_PASWORD
* DB_MAXPOOLSIZE
* DB_PORT
* EVENTS_PUBLISHER_BOOTSTRAP_SERVERS
* EVENTS_CONSUMER_BOOTSTRAP_SERVERS
* EVENTS_PUBLISHER_ZK_CONNECT
* EVENTS_CONSUMER_ZK_CONNECT


### Other documentation

Other [modules](https://dev.folio.org/source-code/#server-side) are described,
with further FOLIO Developer documentation at [dev.folio.org](https://dev.folio.org/)

### Issue tracker

See project [MODOA](https://issues.folio.org/browse/MODOA)
at the [FOLIO issue tracker](https://dev.folio.org/guidelines/issue-tracker).

### Download and configuration

The built artifacts for this module are available.
See [configuration](https://dev.folio.org/download/artifacts) for repository access,
and the [Docker image](https://hub.docker.com/r/folioorg/mod-oa/).



Notes on github actions for grails: https://guides.grails.org/grails-on-github-actions/guide/index.html, https://dev.to/erichelgeson/grails-ci-with-github-actions-25ff

