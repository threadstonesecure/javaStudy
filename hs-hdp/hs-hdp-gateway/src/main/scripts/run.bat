set JAVA_HOME=%JAVA_HOME%
set JAVA_OPTS=-cp ../conf;../*;../lib   -Xms512m -Xmx512m -Djavax.net.debug=ssl,handshake
set JAVACMD="%JAVA_HOME%/bin/java"

%JAVACMD%  %JAVA_OPTS%  com.yuntai.hdp.gateway.StartHdpGateway

rem java -jar ../hs-hdp-server-1.0.0-SNAPSHOT.jar
