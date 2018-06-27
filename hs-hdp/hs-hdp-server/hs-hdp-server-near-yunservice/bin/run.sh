#!/usr/bin/env bash
#获取当前目录的上一级目录
SERVERDIR=$(cd `dirname $0`;cd ..;pwd)
cd $SERVERDIR
#DEBUG_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005"
GC_OPTS=" -Xloggc:hdpServer_gc.log -verbose:gc -XX:+PrintGCDetails -XX:+PrintGCTimeStamps  -XX:+UseConcMarkSweepGC  -XX:+UseParNewGC -XX:+HeapDumpOnOutOfMemoryError  -XX:HeapDumpPath=hdpserver_oom.hprof"
JAVA_OPTS="-cp $SERVERDIR/conf:$SERVERDIR/*:$SERVERDIR/lib/* -server -Xms512m -Xmx1024m -Xss256k -Djavax.net.debug=ssl,handshake $GC_OPTS $DEBUG_OPTS "

JAVACMD="$JAVA_HOME/bin/java"
MAINCLASS=com.yuntai.hdp.server.StartHdpServer
SERVERAPP=HdpServer
JAVACMD="$JAVACMD $JAVA_OPTS  $MAINCLASS"

start(){
    echo -n "Starting $SERVERAPP :"
    PID=`pgrep -f  "hdpserver_near_yunservice"`
    if [ -n "$PID" ]; then
        printf "$MAINCLASS is already running with pid $PID. \n"
        return 0
    else
        $JAVACMD >/dev/null 2>&1 &
        sleep 2
        PID=`pgrep -f  "$MAINCLASS"`
        if [ -n "$PID" ]; then
            printf "$MAINCLASS is  running with pid $PID.\n "
        else
            echo "failed."
        fi
    fi
}

new(){
    echo -n "Starting $SERVERAPP :"
    $JAVACMD >/dev/null 2>&1 &
    sleep 2
    PID=`pgrep -f  "$MAINCLASS"`
    if [ -n "$PID" ]; then
        printf "$MAINCLASS is  running with pid $PID.\n "
    else
        echo "failed."
    fi
}

stop(){
    echo -n "Stoping  $SERVERAPP :"
    PID=`pgrep -f  "$MAINCLASS"`
    if [ -n "$PID" ]; then
        pkill -f  "$MAINCLASS"
    else
        echo " no running!"
        return 0
    fi
    i=1
    while [ "`pgrep -f \"$MAINCLASS\"`" ];
    do
        if [ $i -eq 60 ]; then
            echo "giving up after 60 secods"
            return 0
        fi
        i=`expr $i + 1`
        sleep 1
    done
    PID=`pgrep -f "$MAINCLASS"`
    if [ -n "$PID" ]; then
        echo " failed"
        return 0
    else
        echo " ok"
        return 1
    fi
}

status(){
    PID=`pgrep -f  "$MAINCLASS"`
    if [ -n "$PID" ]; then
        echo "$SERVERAPP is running (pid $PID)."
    else
        echo "$SERVERAPP is NOT running."
    fi
}

case "$1" in  

    start)
        start
        RETVAL=$?
        ;;

    stop)
        stop
        RETVAL=$?
        ;;

    status)
        status
        RETVAL=$?
        ;;
  
    restart)
        stop
        sleep 2
        start
        RETVAL=$?
        ;;

    restartpid)
        kill -9 $2
        start
        RETVAL=$?
        ;;

    new)
        new
        RETVAL=$?
        ;;

    *)
        echo "Usage: $0 {start|stop|status|restart}"
        exit 1
  
esac 
exit $?
