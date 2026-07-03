# Reusable components

* You can create the following folders here:
  - `schemas` - reusable [Schema Objects](https://github.com/OAI/OpenAPI-Specification/blob/main/versions/3.0.2.md#schemaObject)
  - `responses` - reusable [Response Objects](https://github.com/OAI/OpenAPI-Specification/blob/main/versions/3.0.2.md#responseObject)
  - `parameters` - reusable [Parameter Objects](https://github.com/OAI/OpenAPI-Specification/blob/main/versions/3.0.2.md#parameterObject)
  - `examples` - reusable [Example Objects](https://github.com/OAI/OpenAPI-Specification/blob/main/versions/3.0.2.md#exampleObject)
  - `headers` - reusable [Header Objects](https://github.com/OAI/OpenAPI-Specification/blob/main/versions/3.0.2.md#headerObject)
  - `requestBodies` - reusable [Request Body Objects](https://github.com/OAI/OpenAPI-Specification/blob/main/versions/3.0.2.md#requestBodyObject)
  - `links` - reusable [Link Objects](https://github.com/OAI/OpenAPI-Specification/blob/main/versions/3.0.2.md#linkObject)
  - `callbacks` - reusable [Callback Objects](https://github.com/OAI/OpenAPI-Specification/blob/main/versions/3.0.2.md#callbackObject)
  - `securitySchemes` - reusable [Security Scheme Objects](https://github.com/OAI/OpenAPI-Specification/blob/main/versions/3.0.2.md#securitySchemeObject)
* Filename of files inside the folders represent component name, e.g. `Customer.yaml`

## Local practice

* Some parameters and schemas are shared with other Grails-based FOLIO module repositories and live in `../common/` instead of here. Check there first before adding something that might already exist.
* If you edit a file in `common/`, make the same edit in the other repository's `common/` folder too — there's no automatic sync.
* Only add something to `common/` if it's identical across repos, self-contained (no `$ref` to a file that only exists in this repo), and actually used somewhere.
* New list-returning schemas: wrapper = `<Type>Results.yaml` (results + pagination), array = `<Type>ResultsArray.yaml`.
