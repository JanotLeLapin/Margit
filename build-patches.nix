{ writeScriptBin }: writeScriptBin "margit-build-patches" ''
  #!/bin/sh

  rm -r patches
  mkdir patches
  git -C src format-patch --no-stat -N -o "$(pwd)/patches" $(git -C src rev-list --max-parents=0 HEAD)..HEAD
  for patch in "$(pwd)/patches"/*.patch; do
    sed -i "1d" $patch
  done
''
