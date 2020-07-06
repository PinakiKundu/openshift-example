Using Secured Remote Access to Cache in Openshift via HotRod
==============================================
Example of how to setup a local Hot Rod client running against a RHDG cluster in an Openshift environment.

What is it?
-----------

Hot Rod is a binary TCP client-server protocol used in Red Hat Data Grid. The Hot Rod protocol facilitates faster client and server interactions in comparison to other text based protocols(Memcached, REST) and allows clients to make decisions about load balancing, failover and data location operations.

The `hotrod-secured-remote-access-datagrid-openshift` demo demonstrates how to connect securely to remote Red Hat Data Grid (RHDG) running on Openshift to store, retrieve, and remove data from cache using the Hot Rod protocol.

System requirements
-------------------

All you need to build this project is:

* Java 8.0 (Java SDK 1.8) or higher, 
* Maven 3.3.x or higher.
* Red Hat Data Grid 7.3 is running on top of Openshift 3.11
* keytool is also needed to manage certificates, keystore and truststore

The Java application is designed to be run locally

Preparating Openshift Environment
---------------

> Some parts of this sections can be skipped if already done

1. Login as admin

~~~shell
$ oc login <admin>
~~~

2. Import `datagrid-service` template

~~~shell
$ oc create -n openshift -f https://raw.githubusercontent.com/jboss-container-images/jboss-datagrid-7-openshift-image/datagrid73/services/datagrid-service-template.yaml
~~~

3. Import `jboss-datagrid-7/datagrid73-openshift` image

~~~shell
$ oc import-image -n openshift jboss-datagrid-7/datagrid73-openshift --from=registry.redhat.io/jboss-datagrid-7/datagrid73-openshift --all --confirm
~~~


Deploying Red Hat Data Grid 7.3.x on Openshift
---------------

1. Login as user and create OCP project

~~~shell
$ oc login <user>
$ oc new-project rhdg
~~~

2. Deploy Red Hat Data Grid

~~~shell
$ oc new-app datagrid-service -n rhdg \
    -p APPLICATION_USER=datagrid \
    -p APPLICATION_PASSWORD=datagrid \
    -e CACHE_NAMES=teams
~~~

3. Create a SSL Route for Hod Rod Service

~~~shell
$ oc create -n rhdg route passthrough secure-datagrid-app-hotrod --service datagrid-service
$ oc get -n rhdg route secure-datagrid-app-hotrod

NAME                         HOST/PORT                                                                  PATH      SERVICES           PORT      TERMINATION   WILDCARD
secure-datagrid-app-hotrod   secure-datagrid-app-hotrod-rhdg.<Openshift Application Suffix>                       datagrid-service   hotrod    passthrough   None
~~~

Hot Rod client configuration
----------------------------
  
1. Clone the current project

~~~shell
$ git clone https://github.com/pinakispecial/openshift-example.git
$ cd hotrod-endpoint
~~~

2. Edit `src/main/resources/jdg.properties` as following

~~~
jdg.host=secure-datagrid-app-hotrod-rhdg.<Openshift Application Suffix>
jdg.hotrod.port=443
~~~

3. Extract the trusted certificate from the `service-certs` secret

~~~shell
$ oc get -n rhdg secret service-certs -o jsonpath='{.data.tls\.crt}' | base64 -d > tls.crt
~~~

4. Import the trusted certificate into a new trustore for the Java application

~~~shell
$ keytool -import -noprompt -v -trustcacerts -keyalg RSA -alias datagrid-service \
    -storepass mykeystorepass -file tls.crt -keystore src/main/resources/truststore.jks
~~~

Build and Run the Quickstart
----------------------------

~~~shell
$ mvn clean spring-boot:run
~~~

Using the application
---------------------
Over browser hit localhost:8080 and all instruction will be there...
