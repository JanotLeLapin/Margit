{ jdk8
, jdt-language-server
, unzip
, gnutar
, git
, fetchurl
, fetchgit
, stdenv
, callPackage
, mkShell
}: let
  margit-original-jar = fetchurl {
    url = "https://launcher.mojang.com/v1/objects/5fafba3f58c40dc51b5c3ca72a98f62dfdae1db7/server.jar";
    hash = "sha256-Oa73INxTCUdvVvLpalFvPdMEG7v0Qsv9R9Y6y9Bq8x4=";
  };

  margit-build-data = let
    repo = fetchgit {
      url = "https://hub.spigotmc.org/stash/scm/spigot/builddata.git";
      rev = "838b40587fa7a68a130b75252959bc8a3481d94f";
      hash = "sha256-ZHghwZUgx6N6FP2a4MKyQhI6ZvdkmHTPog5EgeVs+Xg=";
    };
  in {
    specialSource = "${repo}/bin/SpecialSource.jar";
    specialSource2 = "${repo}/bin/SpecialSource-2.jar";
    fernFlower = "${repo}/bin/fernflower.jar";
    mapPath = key: "${repo}/mappings/${builtins.getAttr key (builtins.fromJSON (builtins.readFile "${repo}/info.json"))}";
  };

  margit-mapped-jar = callPackage ./remap.nix { inherit margit-original-jar margit-build-data; };
  margit-decompiled-jar = callPackage ./decompile.nix { inherit margit-mapped-jar margit-build-data; };
in mkShell {
  buildInputs = [
    jdk8 git gnutar jdt-language-server
  ];

  shellHook = ''
    rm -rf src
    mkdir -p src
    git -C src init

    tar -xzvf ${margit-decompiled-jar}
    git -C src add .
    git -C src commit -m "initial commit"
  '';
}
