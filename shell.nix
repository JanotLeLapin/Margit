{ jdk8
, jdk22
, unzip
, gradle
, fetchurl
, fetchgit
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
in mkShell {
  buildInputs = [
    jdk8 gradle
  ];

  shellHook = let
    info = builtins.fromJSON (builtins.readFile "${builddata}/info.json");
    ss = "${builddata}/bin/SpecialSource.jar";
    ss2 = "${builddata}/bin/SpecialSource-2.jar";
    fernflower = "${builddata}/bin/fernflower.jar";
    mapPath = key: "${builddata}/mappings/${builtins.getAttr key info}";

    classMap = "./jars/server-cl.jar";
    memberMap = "./jars/server-m.jar";
    finalMap = "./jars/server-mapped.jar";
  in ''
    echo "Applying mappings"

    mkdir -p jars/classes
    ${jdk8}/bin/java -jar ${ss2} map -i ${original} -m ${mapPath "classMappings"} -o ${classMap}
    ${jdk8}/bin/java -jar ${ss2} map -i ${classMap} -m ${mapPath "memberMappings"} -o ${memberMap}
    ${jdk8}/bin/java -jar ${ss} --kill-lvt -i ${memberMap} --access-transformer ${mapPath "accessTransforms"} -m ${mapPath "packageMappings"} -o ${finalMap}

    mkdir -p src/main/java
    ${unzip}/bin/unzip ${finalMap} "net/minecraft/server/*" -d jars/classes
    ${jdk8}/bin/java -jar ${fernflower} -dgs=1 -hdc=0 -rbr=0 -asc=1 -udv=0 jars/classes src/main/java
  '';
}
