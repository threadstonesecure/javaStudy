echo "yun 日志"
more /Users/denglt/onGithub/javaStudy/hs-hdp/hs-hdp-server/hs-hdp-server-near-yunservice/log/hdp-server.log | grep $1

echo "hosp 日志"
more /Users/denglt/onGithub/javaStudy/hs-hdp/hs-hdp-server/hs-hdp-server-near-hosp/log/hdp-server.log | grep $1

#more /Users/denglt/onGithub/javaStudy/hs-hdp/hs-hdp-gateway/target/hs-hdp-gateway/log/hdp-gateway.log | grep $1
