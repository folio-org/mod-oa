# mod-oa

Copyright (C) 2021 The Open Library Foundation

This software is distributed under the terms of the Apache License,
Version 2.0. See the file "[LICENSE](LICENSE)" for more information.

## Introduction

Bounded Context Deailing with Open Access Publishing

## Additional information

### Deployment

A sample k8s resource definition for service and deployment [can be found the scripts directory]()

This module requires the following env parameters
* OKAPI_SERVICE_PORT - port number for okapi
* OKAPI_SERVICE_HOST - Host [namespace.hostname if running in a different namespace to okapi]

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



Notes on github actions for grails: https://guides.grails.org/grails-on-github-actions/guide/index.html
