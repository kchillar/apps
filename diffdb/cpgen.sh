#!/bin/bash

timestamp=`date +%F-%H:%M:%S`

#Go to where pom.xml is located to run the maven command to generate the classpath file
cd ..
mvn dependency:build-classpath -Dmdep.includeScope=runtime -Dmdep.outputFile=./run.classpath
# come back to this directory
cd -

# The string that needs to be replaced in cp.txt
matchVar="\/Users\/kalyanc\/.m2\/repository"

# The replacement string
repVar='$M2_REPO'

echo '#######################################################################' > run.classpath
echo "# AUTO GENERATED FILE USING ./cpgen.sh Timestamp: $timestamp #" >> run.classpath
echo '#######################################################################' >> run.classpath
echo '' >> run.classpath

# Add M2_REPO variable definition to the file
echo 'M2_REPO=$HOME/.m2/repository' >> run.classpath

# Add CLASSPATH variable
echo 'CLASSPATH="../bin:./conf:$M2_REPO/org/apache/logging/log4j/log4j-slf4j-impl/2.11.2/log4j-slf4j-impl-2.11.2.jar:$M2_REPO/org/apache/logging/log4j/log4j-core/2.11.2/log4j-core-2.11.2.jar:$M2_REPO/org/apache/logging/log4j/log4j-api/2.11.2/log4j-api-2.11.2.jar:\'>> run.classpath


cat cp.txt | sed  -e "s/$matchVar/$repVar/g" | sed  -e "s/:/:\\\#/g" | tr '#' '\n' >>run.classpath

# close quote opened for classpath variale value definition 
echo '"' >> run.classpath

# Remove the mavne generated file
rm ./cp.txt
 
# Now run.classpath file will have the needed classpath
# In app.sh source the run.classpath for setting the CLASSPATH
 
echo "############## Generated run.classpath #################"



#./libs/log4j-slf4j-impl-2.11.2.jar:\
#./libs/log4j-core-2.11.2.jar:\
# ./libs/log4j-api-2.11.2.jar:\
