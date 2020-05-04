source ./run.classpath
JAVA_HOME=$HOME/software/amazon-corretto-11.jdk/Contents/Home

echo "Using CLASSPATH=$CLASSPATH"

$JAVA_HOME/bin/java -cp $CLASSPATH com.pmc.fw.resources.ResourceInitializer ./conf

