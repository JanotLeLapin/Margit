{ jdk8
, unzip
, gnutar
, stdenv
, margit-mapped-jar
, margit-build-data
}: stdenv.mkDerivation {
  name = "margit-decompiled-jar";
  src = margit-mapped-jar;
  phases = [ "buildPhase" "installPhase" ];
  buildInputs = [ jdk8 unzip gnutar ];
  buildPhase = ''
    mkdir -p classes src/main/java
    unzip ${margit-mapped-jar} "net/minecraft/server/*" -d classes
    java -jar ${margit-build-data.fernFlower} -dgs=1 -hdc=0 -rbr=0 -asc=1 -udv=0 classes src/main/java
    tar -czvf decompiled.tar.gz src/main/java
  '';
  installPhase = ''
    mv decompiled.tar.gz $out
  '';
}
