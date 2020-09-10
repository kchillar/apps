source ./run.classpath

echo "CLASSPATH: $CLASSPATH"


OPT="-Dswing.aatext=true -Dawt.useSystemAAFontSettings=lcd -Dswing.d"


$JAVA_HOME/bin/java -cp $CLASSPATH com.ajoy.etol.app.UIApp $*
