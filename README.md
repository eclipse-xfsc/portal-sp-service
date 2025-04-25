# Solution Packaging service
In order to run this application locally, the following should be satisfied:
- Identity system (keycloak) should be accessible from localhost
- GAIA-X Federated Catalogue service or Mocking service (https://gitlab.com/gaia-x/data-infrastructure-federation-services/por/demo) 
should be accessible

## Configuring keycloak
In order to protect provided endpoints and develop/test authorization with OIDC, the access to working identity system (keycloak)
should be configured. The following variables should be specified:
  * KC_URL (URL to Keycloak)
  * KC_REALM (realm name in Keycloak)
  * KC_RESOURCE (client in Keycloak)

All these variables are configured in Portal infrastructure project 
(https://gitlab.com/gaia-x/data-infrastructure-federation-services/por/infra-mesh/-/settings/ci_cd) and contains sensitive information.

For instance, they might have the following values:
  * KC_URL="http://78.138.66.50/" 
  * KC_REALM="gaiax_realm"
  * KC_RESOURCE="gaiax_client"

## Configuration of external systems
Open the application.yml file and configure value for the services.demo.uri.internal setting representing URI for
GAIA-X Federated Catalogue service. For instance:

~~~~
services.demo.uri.internal: http://gaia-x.demo.local:8081/demo
~~~~

Make sure that such external system is available from your host.


## Application run
With all these configurations, use the following command to run application:

~~~~
$ KC_URL="http://78.138.66.50/" KC_REALM="gaiax_realm" KC_RESOURCE="gaiax_client" \
  ./mvnw clean spring-boot:run
~~~~

The application should be started on localhost and use port, configured in application.yml file:

~~~~
server.port: 8092
~~~~
