#!/bin/sh

# Pack the files for a GridSim release
# Usage example: 
#    sh release.sh 5beta

VERSION=$1
FILES="./source ./examples ./jars ./build.xml ./doc *.txt"
CURRDIR=`pwd`

mkdir -p ./release/gridsim

for file in $FILES; do
    cp -r $file release/gridsim
done

cd ./release

tar cfzv gridsim-toolkit${VERSION}.tar.gz gridsim --exclude=**/.svn
zip -r gridsim-toolkit${VERSION}.zip gridsim -x *.svn*

rm -rf gridsim

cd $CURRDIR
