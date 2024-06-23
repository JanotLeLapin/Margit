{ jdk8
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
  programs = [
    jdk8 gradle
  ];

  shellHook = ''
    echo "Applying mappings"

    mkdir -p jars
    ${jdk8}/bin/java -jar ${builddata}/bin/SpecialSource-2.jar map -i ${original} -m ${builddata}/mappings/bukkit-1.8.8-cl.csrg -o ./jars/server-cl.jar
    ${jdk8}/bin/java -jar ${builddata}/bin/SpecialSource-2.jar map -i ./jars/server-cl.jar -m ${builddata}/mappings/bukkit-1.8.8-members.csrg -o ./jars/server-m.jar
    ${jdk8}/bin/java -jar ${builddata}/bin/SpecialSource.jar --kill-lvt -i ./jars/server-m.jar --access-transformer ${builddata}/mappings/bukkit-1.8.8.at -m ${builddata}/mappings/package.srg -o ./jars/server-mapped.jar
  '';
}
