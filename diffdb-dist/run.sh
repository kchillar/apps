JAVA_HOME=$HOME/software/amazon-corretto-11.jdk/Contents/Home

source ./run.classpath

echo "Using CLASSPATH=$CLASSPATH"

$JAVA_HOME/bin/java -Xms64M -Xmx512M -cp $CLASSPATH com.pmc.fw.resources.ResourceInitializer ./conf

