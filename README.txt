$Id: README.txt 325 2015-02-27 16:57:05Z recena $

A) You must to use something similar to this in Eclipse IDE:

JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.7.0_25.jdk/Contents/Home
MAVEN_OPTS=-javaagent:/Users/recena/tools/jrebel-5.1.2/jrebel.jar -Dfile.encoding=UTF-8

mvn package vertx:runMod -Denv=recena-dev -Dmaven.test.skip

B) You must use something similar to this in CLI:

mvn clean install -Denv=recena-dev
vertx runzip target/api-1.0-SNAPSHOT-mod.zip -conf target/classes/config.json