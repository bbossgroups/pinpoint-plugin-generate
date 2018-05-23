#!/bin/sh
cd `dirname \$0`
java -jar ${project}-bootstrap-${bboss_version}.jar stop --shutdownLevel=9
