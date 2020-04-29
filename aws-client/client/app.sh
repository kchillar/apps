$REPO=$HOME/.m2/repository



JAVA_HOME=$HOME/software/amazon-corretto-11.jdk/Contents/Home

CLASSPATH=":\
../../fw/bin:\
../bin:\
./conf:\
./libs/pmc-fw.jar:\
./libs/slf4j-api-1.7.25.jar:\
./libs/log4j-slf4j-impl-2.11.2.jar:\
./libs/log4j-core-2.11.2.jar:\
./libs/log4j-api-2.11.2.jar:\
./libs/jakarta.xml.bind-api-2.3.2.jar:\
./libs/istack-commons-runtime-3.0.8.jar:\
./libs/jaxb-runtime-2.3.2.jar:\
"

$JAVA_HOME/bin/java -cp $CLASSPATH com.pmc.fw.resources.ResourceInitializer ./conf

