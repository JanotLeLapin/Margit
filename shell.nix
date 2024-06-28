{ jdk8
, jdk21
, rust-bin
, rust-analyzer
, unzip
, gnutar
, git
, fetchurl
, fetchgit
, stdenv
, callPackage
, mkShell
, writeScriptBin
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

  lsp = callPackage ./lsp.nix {};

  margit-mapped-jar = callPackage ./remap.nix { inherit margit-original-jar margit-build-data; };
  margit-decompiled-src = callPackage ./decompile.nix { inherit margit-mapped-jar margit-build-data; };
  margit-patched-src = callPackage ./apply-patches.nix { inherit margit-decompiled-src; };

  margit-build-patches = callPackage ./build-patches.nix {};
in mkShell {
  buildInputs = [
    jdk21 (rust-bin.fromRustupToolchainFile ./proxy/rust-toolchain.toml)
    lsp rust-analyzer
    git gnutar
    margit-build-patches
  ];

  shellHook = ''
    if [ ! -d "src" ]; then
      mkdir -p src
      tar -xzf ${margit-patched-src}/patched.tar.gz -C src
      echo "Initialized src"
    else
      echo "Skipping src initialization"
    fi
  '';
}
