{ git
, busybox
, gnupatch
, margit-decompiled-src
, stdenv
}: stdenv.mkDerivation {
  name = "margit-patched-src";
  src = ./.;
  buildInputs = [ git busybox gnupatch ];
  buildPhase = ''
    cp -r --no-preserve=all ${margit-decompiled-src}/main/java/net/minecraft/server nms

    for file in ${./nms-patches}/*; do
      filename=$(basename "$file" .patch)
      ${gnupatch}/bin/patch "nms/$filename" < "$file"
    done
  '';
  installPhase = ''
    mv nms $out
  '';
}
