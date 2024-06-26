{ git
, gnutar
, margit-decompiled-src
, stdenv
}: stdenv.mkDerivation {
  name = "margit-patched-src";
  src = margit-decompiled-src;
  buildInputs = [ git gnutar ];
  buildPhase = ''
    git init
    git add .

    git config user.email "auto@margit.com"
    git config user.name "Margit"
    git commit -m "initial commit"
    for patch in ${./patches}/*.patch; do
      git am --3way --ignore-space-change < $patch
    done
    tar -czvf patched.tar.gz main .git
  '';
  installPhase = ''
    mkdir $out
    mv main $out
    mv patched.tar.gz $out
  '';
}
