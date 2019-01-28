#!/usr/bin/env bash

cd /Users/denglt/onGithub/javaStudy/oracleDba/target
sshKey=/Users/denglt/Dropbox/privatekey/denglt.pem
server=root@47.106.93.69
jar=oracleDba.jar

echo tar jar
tar  -cf  $jar   oracleDba
echo copy jar to server
scp -i $sshKey  $jar $server:/app

echo unzip jar on sever
ssh -i $sshKey  $server  rm -rf /app/oracleDba
ssh -i $sshKey  $server  tar -xf /app/oracleDba.jar -C /app

echo run jar on sever
ssh -i $sshKey  $server sh /app/oracleDba/bin/run.sh restart
