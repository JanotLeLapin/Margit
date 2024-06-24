{ jdk8
, jdk22
, unzip
, gradle
, fetchurl
, fetchgit
, stdenv
, mkShell
}: let
  original = fetchurl {
    url = "https://launcher.mojang.com/v1/objects/5fafba3f58c40dc51b5c3ca72a98f62dfdae1db7/server.jar";
    hash = "sha256-Oa73INxTCUdvVvLpalFvPdMEG7v0Qsv9R9Y6y9Bq8x4=";
  };

  builddata = fetchgit {
    url = "https://hub.spigotmc.org/stash/scm/spigot/builddata.git";
    rev = "838b40587fa7a68a130b75252959bc8a3481d94f";
    hash = "sha256-ZHghwZUgx6N6FP2a4MKyQhI6ZvdkmHTPog5EgeVs+Xg=";
  };

  mapped = let
    info = builtins.fromJSON (builtins.readFile "${builddata}/info.json");
    ss = "${builddata}/bin/SpecialSource.jar";
    ss2 = "${builddata}/bin/SpecialSource-2.jar";
    mapPath = key: "${builddata}/mappings/${builtins.getAttr key info}";
  in stdenv.mkDerivation {
    name = "mapped-jar";
    src = original;
    phases = [ "buildPhase" "installPhase" ];
    buildInputs = [ jdk8 ];
    buildPhase = ''
      java -jar ${ss2} map -i ${original} -m ${mapPath "classMappings"} -o server-cl.jar
      java -jar ${ss2} map -i server-cl.jar -m ${mapPath "memberMappings"} -o server-m.jar
      java -jar ${ss} --kill-lvt -i server-m.jar --access-transformer ${mapPath "accessTransforms"} -m ${mapPath "packageMappings"} -o server-mapped.jar
    '';
    installPhase = ''
      mv server-mapped.jar $out
    '';
  };
in mkShell {
  buildInputs = [
    jdk22 gradle
  ];

  shellHook = let
    fernflower = "${builddata}/bin/fernflower.jar";
  in ''
    echo "decompiling"
    mkdir -p src/main/java
    mkdir -p classes
    ${unzip}/bin/unzip ${mapped} "net/minecraft/server/*" -d classes
    ${jdk8}/bin/java -jar ${fernflower} -dgs=1 -hdc=0 -rbr=0 -asc=1 -udv=0 classes src/main/java
  '';
}
