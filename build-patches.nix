{ writeScriptBin
, margit-decompiled-src
}: writeScriptBin "margit-build-patches" ''
  #!/bin/sh
  mkdir -p nms-patches
  for file in ${margit-decompiled-src}/main/java/net/minecraft/server/*; do
    filename=$(basename "$file")
    file_b="server/src/main/java/net/minecraft/server/$filename"
    if [ -f "$file_b" ]; then
        if ! diff -q "$file" "$file_b" >/dev/null; then
            diff -u --label="$filename" --label="$filename" "$file" "$file_b" > "nms-patches/$filename.patch"
        fi
    fi
done
''
