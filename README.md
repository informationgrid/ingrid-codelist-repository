InGrid Codelist Repository
==========================

The codelist repository maintains codelists that are used by several InGrid components. The Codelists are used especially in conjunction with the InGrid Catalog (meta data).


Requirements
-------------

- a running InGrid Software System

Installation
------------

Download from https://distributions.informationgrid.eu/ingrid-codelist-repository/
 
or

build from source with `mvn clean package`.

Execute

```
java -jar ingrid-codelist-repository-x.x.x-installer.jar
```

and follow the install instructions.

Obtain further information at http://www.ingrid-oss.eu/

Configuration
-------------

The following environment variables are supported:

* CREDENTIALS_ADMIN => credentials for user who can access and manage the codelist repository
  * e.g. "admin1=>password1,admin2=>MD5:2112323212as3...,admin3=>CRYPT:ad1k..."
* CREDENTIALS_USER => credentials for user who can access the codelist repository API to fetch codelists
  * e.g. "username1=>password1,username2=>password2"
* CODELISTS_IGNORE => list of Codelist-IDs that should not be updated
  * e.g. "100,200"

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


Patch naming convention
-------
The patches are named according to their planned release version.
Once the first patch is created, subsequent patches incorporate the version name and a letter from the alphabet.
An underscore is used to separate this, followed by the descriptive operation name (e.g., add, update, delete, etc.).
Another underscore is added, which is then followed by the codelist number. In the case of multiple codelists, they are separated by underscores as well.


Commit convention - Changes file deprecated
-------
The change file is no longer in use.
Instead, commits should include the ticket number of the implemented patch.

