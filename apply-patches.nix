{ git
, busybox
, gnupatch
, margit-decompiled-src
, margit-bukkit
, margit-craft-bukkit
, stdenv
}: stdenv.mkDerivation {
  name = "margit-patched-src";
  src = ./.;
  buildInputs = [ git busybox gnupatch ];
  buildPhase = ''
    cp -r --no-preserve=all ${margit-bukkit} api
    cd api
    git init
    git config user.email "auto@margit.com"
    git config user.name "Margit"
    git add .
    git commit -m "initial commit"
    git am --3way --ignore-whitespace ${./bukkit-patches}/*.patch
    git am --3way --ignore-whitespace ${./spigot-api-patches}/*.patch
    cd ..

    mkdir nms
    cp -r --no-preserve=all ${margit-decompiled-src}/main nms

    for file in ${margit-craft-bukkit}/nms-patches/* ${./nms-patches}/*; do
      patchFile="$file"
      file="$(echo "$file" | rev | cut -d/ -f1 | rev | cut -d. -f1).java"

      echo "Patching $file < $patchFile"
      sed -i 's/\r//' "nms/main/java/net/minecraft/server/$file" > /dev/null

      ${gnupatch}/bin/patch -s -d nms/main/java/ "net/minecraft/server/$file" < "$patchFile"
    done

    cp -r --no-preserve=all ${margit-craft-bukkit} server
    mv nms/main/java/net server/src/main/java
    cd server
    git init
    git config user.email "auto@margit.com"
    git config user.name "Margit"
    git add .
    git commit -m "add nms"
    git am --3way --ignore-whitespace ${./craftbukkit-patches}/*.patch
    git am --3way --ignore-whitespace ${./spigot-server-patches}/*.patch
    cd ..
  '';
  installPhase = ''
    mkdir $out

    mv api $out
    mv server $out
  '';
}
