#!/bin/sh
day=`date +%Y%m%d`
fileName="/data/crawler/backup/crawler_${day}.zip"
echo "backup file name is $fileName"

tomcatName="crawler"
webappName="crawler"

dir="/data/$tomcatName/apache-tomcat-7.0.70/webapps/$webappName/*"
echo "backup path is $dir"
zip -r $fileName $dir
