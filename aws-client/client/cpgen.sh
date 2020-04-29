#!/bin/bash

cd ..
mvn dependency:build-classpath -Dmdep.includeScope=runtime -Dmdep.outputFile=./client/cp.txt
cd -



matchVar="\/Users\/kalyanc\/.m2\/repository"

repVar='$M2_REPO'

echo 'M2_REPO=$HOME/.m2/repository' > run.classpath
echo  'CLASSPATH="\' >> run.classpath

cat cp.txt | sed  -e "s/$matchVar/$repVar/g" | sed  -e "s/:/:\\\#/g" | tr '#' '\n' >>run.classpath
echo '"' >> run.classpath

rm ./cp.txt
 
echo "********* Generated run.classpath ************"