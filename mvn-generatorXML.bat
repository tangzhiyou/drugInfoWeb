set MAVEN_OPTS=-Xms256m -Xmx512m -XX:MaxPermSize=512m -Dfile.encoding=UTF-8
call mvn mybatis-generator:generate
call mvn clean compile
call mvn exec:java -Dexec.mainClass="com.skysoft.util.GenerateRULE"
call mvn compile jetty:run
