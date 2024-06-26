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
    mkdir -p classes src/main/java src/main/resources
    unzip ${margit-mapped-jar} "net/minecraft/server/*" -d classes
    unzip ${margit-mapped-jar} "assets/*" log4j2.xml yggdrasil_session_pubkey.der -d src/main/resources
    java -jar ${margit-build-data.fernFlower} -dgs=1 -hdc=0 -rbr=0 -asc=1 -udv=0 classes src/main/java
    tar -czvf decompiled.tar.gz src
  '';
  installPhase = ''
    mv decompiled.tar.gz $out
  '';
}
