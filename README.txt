Eclipse developing
------------------

- run/debug with JettyStarter and VM-parameter '-Djetty.webapp=src/main/webapp'
- change build target path of java sources to 'target/WEB-INF/classes' -> this is where jersey looks for classes!
- with "mvn compile" the dojo package is downloaded and unpacked into the target directory where it is used by the webapp
- in project properties under Java Compiler set version to 1.6! This should be done automatically when doing "mvn eclipse:eclipse"