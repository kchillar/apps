#!/bin/bash
rm ./srvcs.zip
rm -rf ./srvcs
mkdir ./srvcs
cp -r ./target/classes/* ./srvcs
cp -r ./target/lib ./srvcs
cd ./srvcs
zip -r ../srvcs.zip *
cd -
ls -ltr
