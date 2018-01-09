#svn checkout --username jiangbo --password jerry229 svn://127.0.0.1/root/ /data/BUILD/source/
export LANG=en_US.UTF-8

svn update /data/BUILD/source/

rm -rf /data/BUILD/release/*
svn export /data/BUILD/source/02_application /data/BUILD/release/02_application;

cd /data/BUILD/release/02_application/crawler/
ant deploy

