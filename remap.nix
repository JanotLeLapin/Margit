{ jdk8
, stdenv
, margit-original-jar
, margit-build-data
}: stdenv.mkDerivation {
  name = "margit-mapped-jar";
  src = margit-original-jar;
  phases = [ "buildPhase" "installPhase" ];
  buildInputs = [ jdk8 ];
  buildPhase = ''
    java -jar ${margit-build-data.specialSource2} map -i ${margit-original-jar} -m ${margit-build-data.mapPath "classMappings"} -o cl.jar
    java -jar ${margit-build-data.specialSource2} map -i cl.jar -m ${margit-build-data.mapPath "memberMappings"} -o m.jar
    java -jar ${margit-build-data.specialSource} --kill-lvt -i m.jar --access-transformer ${margit-build-data.mapPath "accessTransforms"} -m ${margit-build-data.mapPath "packageMappings"} -o mapped.jar
  '';
  installPhase = ''
    mv mapped.jar $out
  '';
}
