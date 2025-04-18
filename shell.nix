{ jdk8
, jdk23
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
  margit-build-data = let
    repo = fetchgit {
      url = "https://hub.spigotmc.org/stash/scm/spigot/builddata.git";
      rev = "838b40587fa7a68a130b75252959bc8a3481d94f";
      hash = "sha256-ZHghwZUgx6N6FP2a4MKyQhI6ZvdkmHTPog5EgeVs+Xg=";
    };

    info = (builtins.fromJSON (builtins.readFile "${repo}/info.json"));
  in {
    specialSource = "${repo}/bin/SpecialSource.jar";
    specialSource2 = "${repo}/bin/SpecialSource-2.jar";
    fernFlower = "${repo}/bin/fernflower.jar";
    mapPath = key: "${repo}/mappings/${builtins.getAttr key info}";
    minecraftHash = (builtins.getAttr "minecraftHash" info);
  };

  margit-original-jar = fetchurl {
    url = "https://launcher.mojang.com/v1/objects/${margit-build-data.minecraftHash}/server.jar";
    hash = "sha256-Oa73INxTCUdvVvLpalFvPdMEG7v0Qsv9R9Y6y9Bq8x4=";
  };

  margit-craft-bukkit = fetchgit {
    url = "https://hub.spigotmc.org/stash/scm/spigot/craftbukkit.git";
    rev = "e1ebe524a78e27f6a2829ed4574fded3779094e1";
    hash = "sha256-Ek9CTBHwEcTs6ju+4V84zPNQyCxyl7fN3XCSIRYWiDY=";
  };

  lsp = callPackage ./lsp.nix {};

  margit-mapped-jar = callPackage ./remap.nix { inherit margit-original-jar margit-build-data; };
  margit-decompiled-src = callPackage ./decompile.nix { inherit margit-mapped-jar margit-build-data; };
  margit-patched-src = callPackage ./apply-patches.nix { inherit margit-decompiled-src margit-craft-bukkit; };

  margit-build-patches = callPackage ./build-patches.nix {};
in mkShell {
  buildInputs = [
    jdk23 git gnutar
    lsp margit-build-patches
  ];

  shellHook = ''
    if [ ! -d "server" ]; then
      cp -r --no-preserve=all ${margit-patched-src}/server .
      echo "Initialized server"
    else
      echo "Skipping server initialization"
    fi
  '';
}
