#!/bin/sh
### BEGIN INIT INFO
# Provides:          getlicense-api
# Required-Start:    $local_fs $remote_fs $network $syslog $named mysql
# Required-Stop:     $local_fs $remote_fs $network $syslog $named mysql
# Default-Start:     2 3 4 5
# Default-Stop:      0 1 6
# Short-Description: Start/Stop getlicense API with commons-daemon (jsvc)
### END INIT INFO

. /lib/lsb/init-functions

export API_HOME=/usr/local/getlicense/api
export DAEMON_HOME=/usr/local/jsvc
export VERTX_HOME=/usr/local/vertx
export JAVA_HOME=/usr/local/java

PID_FILE=$API_HOME/temp/getlicense-api.pid
CLASSPATH=$VERTX_HOME/conf:$VERTX_HOME/lib/*:$DAEMON_HOME/commons-daemon-1.0.15.jar:$DAEMON_HOME/jsvc.daemonstarter-0.0.1-SNAPSHOT.jar
MOD=$API_HOME/modules/api-1.0-SNAPSHOT-mod.zip

case "$1" in

  start)
    log_daemon_msg "Starting getlicense API with commons-daemon (jsvc)" "jsvc"
    log_end_msg 0
    $DAEMON_HOME/jsvc \
    -jvm server \
    -home $JAVA_HOME \
    -Xms128m -Xmx256m \
    -Duser.language=en \
    -Djava.io.tmpdir=$API_HOME/temp \
    -pidfile $PID_FILE \
    -outfile $API_HOME/logs/jsvc.log \
    -errfile $API_HOME/logs/jsvc-extra.log \
    -cp $CLASSPATH \
    -showversion \
    -procname getlicense \
    net.pyxzl.jsvc.daemonstarter.DaemonStarter org.vertx.java.platform.impl.cli.Starter runzip $MOD -conf $API_HOME/conf/config.json
    exit 0
    ;;

  debug)
    log_daemon_msg "Starting getlicense API with commons-daemon (jsvc)" "jsvc"
    log_end_msg 0
    $DAEMON_HOME/jsvc \
    -jvm server \
    -home $JAVA_HOME \
    -Xms128m -Xmx256m \
    -Djava.io.tmpdir=$API_HOME/temp \
    -pidfile $PID_FILE \
    -outfile $API_HOME/logs/jsvc.log \
    -errfile $API_HOME/logs/jsvc-extra.log \
    -cp $CLASSPATH \
    -showversion \
    -procname getlicense \
    -Xdebug \
    -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=8000 \
    net.pyxzl.jsvc.daemonstarter.DaemonStarter org.vertx.java.platform.impl.cli.Starter runzip $MOD -conf $API_HOME/conf/config.json
    exit 0
    ;;

  stop)
    log_daemon_msg "Stopping getlicense API" "jsvc"
    log_end_msg 0
    $DAEMON_HOME/jsvc \
    -stop \
    -pidfile $PID_FILE \
    es.klicap.clinker.watchdog.Main
    exit 0
    ;;

  status)
    if [ -f $PID_FILE ]; then
      PID=`cat $PID_FILE`
      if [ -z "`ps axf | grep ${PID} | grep -v grep`" ]; then
        printf "%s\n" "getlicense API process (jsvc) dead but pidfile exists"
        exit 1
      else
        echo "getlicense API is running"
        exit 0
      fi
    else
      printf "%s\n" "getlicese API is not running"
      exit 3
    fi
    ;;

  *)
    echo "Usage service getlicense-api {start|stop|status}"
    exit 1
    ;;
esac
