WORKSPACE="/Users/kalyanc/workspace/telugukeyboard"

REPO=$HOME/.m2/repository
appName="test"

echo ""

CLASSPATH="\
$WORKSPACE/etot/bin:\
$WORKSPACE/mylogger/bin:\
"

OPT="-Dswing.aatext=true -Dawt.useSystemAAFontSettings=lcd -Dswing.d"


/Library/Java/JavaVirtualMachines/jdk1.8.0_102.jdk/Contents/Home/bin/java -cp $CLASSPATH com.cks.etot.app.App $*
