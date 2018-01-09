rm -rf /data/BUILD/source
mkdir -p /data/BUILD/source/02_application/funnel
svn checkout --username jiangbo --password jerry229 svn://127.0.0.1/root/02_application/funnel /data/BUILD/source/02_application/funnel
export LANG=en_US.UTF-8


rm -rf /data/BUILD/release/*
mkdir /data/BUILD/release/02_application
svn export /data/BUILD/source/02_application/funnel /data/BUILD/release/02_application/funnel

cd /data/BUILD/release/02_application/funnel/
ant deploy
/data/crawler/deploy.sh
