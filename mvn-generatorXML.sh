#!/bin/sh
set MAVEN_OPTS=-Xms256m -Xmx512m -XX:MaxPermSize=512m -Dfile.encoding=UTF-8
mvn mybatis-generator:generate
mvn clean compile
mvn exec:java -Dexec.mainClass="com.skysoft.util.GenerateRULE"
