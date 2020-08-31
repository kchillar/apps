source ./run.classpath
JAVA_HOME=$HOME/software/amazon-corretto-11.jdk/Contents/Home

echo "Using CLASSPATH=$CLASSPATH"

$JAVA_HOME/bin/java -cp ../../fw/bin:$CLASSPATH com.pmc.fw.view.ViewInitializer ./conf/views

