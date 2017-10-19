#!/usr/bin/env bash
scp -i /Users/denglt/mysh/privatekey/denglt.pem  /Users/denglt/Dropbox/myprograme/javaStudy/oracleDba/target/oracleDba/oracleDba-0.0.1-SNAPSHOT.jar centos@fortify.iask.in:/home/centos/oracleDba
ssh -i /Users/denglt/mysh/privatekey/denglt.pem  centos@fortify.iask.in sudo /home/centos/oracleDba/bin/run.sh restart
