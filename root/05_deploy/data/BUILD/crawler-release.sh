rm -rf /data/BUILD/source
mkdir -p /data/BUILD/source/02_application/crawler
svn checkout --username jiangbo --password jerry229 svn://127.0.0.1/root/02_application/crawler /data/BUILD/source/02_application/crawler
export LANG=en_US.UTF-8


rm -rf /data/BUILD/release/*
mkdir /data/BUILD/release/02_application
svn export /data/BUILD/source/02_application/crawler /data/BUILD/release/02_application/crawler

cd /data/BUILD/release/02_application/crawler/
ant deploy
/data/crawler/deploy.sh

