tomcatName="crawler"
webappName="crawler"
/data/$tomcatName/apache-tomcat-7.0.70/bin/shutdown.sh
webappPath="/data/$tomcatName/apache-tomcat-7.0.70/webapps/$webappName/"
echo "webappPath is $webappPath"
cd $webappPath
rm -rf *
rm -f /data/$tomcatName/apache-tomcat-7.0.70/webapps/$webappName.war

deployWar="/data/$tomcatName/backup/$webappName.war"
echo "deployWar is $deployWar"
cp $deployWar $webappPath
jar -xvf $webappName.war
rm -f $webappName.war

/data/$tomcatName/apache-tomcat-7.0.70/bin/startup.sh
