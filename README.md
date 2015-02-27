InGrid Codelist Repository
==========================

The codelist repository maintains codelists that are used by several InGrid components. The Codelists are used especially in conjunction with the InGrid Catalog (meta data).


Requirements
-------------

- a running InGrid Software System

Installation
------------

Download from https://dev.informationgrid.eu/ingrid-distributions/ingrid-codelist-repository/
 
or

build from source with `mvn package assembly:single`.

Execute

```
java -jar ingrid-codelist-repository-x.x.x-installer.jar
```

and follow the install instructions.

Obtain further information at http://www.ingrid-oss.eu/


Contribute
----------

- Issue Tracker: https://github.com/informationgrid/ingrid-codelist-repository/issues
- Source Code: https://github.com/informationgrid/ingrid-codelist-repository
 
### Set up eclipse project

```
mvn eclipse:eclipse
```

and import project into eclipse.

### Debug in eclipse

- execute ```mvn compile``` to extract the dojo-library
- set up a java application Run Configuration with start class <br/>```de.ingrid.codelistHandler.JettyStarter```
- add the VM argument ```-Djetty.webapp=src/main/webapp``` to the Run Configuration
- the admin gui starts per default on port 8082, change this with VM argument ```-Djetty.port=8083```

Support
-------

If you are having issues, please let us know: info@informationgrid.eu

License
-------

The project is licensed under the EUPL license.
