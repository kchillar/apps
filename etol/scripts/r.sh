source ./run.classpath

echo "CLASSPATH: $CLASSPATH"


OPT="-Dswing.aatext=true -Dawt.useSystemAAFontSettings=lcd -Dswing.d"


/Users/kalyanc/software/amazon-corretto-11.jdk/Contents/Home/bin/java -cp $CLASSPATH com.ajoy.etol.app.CLIApp $*
