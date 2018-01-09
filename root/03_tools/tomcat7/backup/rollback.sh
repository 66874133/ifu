#!/bin/sh
echo $1
tomcatName="crawler"
webappName="crawler"

webappPath="/data/$tomcatName/apache-tomcat-7.0.70/webapps/$webappName/"

rm -rf $webappPath*
cp -f $1 $webappPath
cd  $webappPath
unzip $1
rm -f $1
