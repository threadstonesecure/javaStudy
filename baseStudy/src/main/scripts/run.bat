set JAVA_HOME=%JAVA_HOME%
set JAVA_OPTS=
set javacmd=%JAVA_HOME%\bin\java
set _url=jdbc:oracle:thin:@132.121.164.207:1521:gxtest  
set _username=gpdi_dlt  
set _password=denglt

rem  cp配置时../conf放在前面，覆盖jar中的配置文件
%javacmd% %AVA_OPTS%  -cp ../conf;../lib/*  com.gpdi.collector.oracle.Launch  %_url% %_username% %_password%