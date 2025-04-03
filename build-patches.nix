{ writeScriptBin }: writeScriptBin "margit-build-patches" ''
  #!/bin/sh

  rm -r api-patches
  mkdir api-patches
  git -C api format-patch --no-stat -N -o "$(pwd)/api-patches" $(git -C api rev-list --max-parents=0 HEAD)..HEAD
  for patch in "$(pwd)/api-patches"/*.patch; do
    sed -i "1d" $patch
  done

  rm -r server-patches
  mkdir server-patches
  git -C server format-patch --no-stat -N -o "$(pwd)/server-patches" $(git -C server rev-list --max-parents=0 HEAD)..HEAD
  for patch in "$(pwd)/server-patches"/*.patch; do
    sed -i "1d" $patch
  done
''
