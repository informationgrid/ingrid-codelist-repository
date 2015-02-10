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

Obtain further information at https://dev.informationgrid.eu/


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

TBD


Support
-------

If you are having issues, please let us know: info@informationgrid.eu

License
-------

The project is licensed under the EUPL license.
