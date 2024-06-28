{ jdk21
, lombok
, jdt-language-server
, writeScriptBin
}: let
  workspace = "$HOME/workspace/$(pwd)";
  jdtls = "${jdt-language-server}/share/java/jdtls";
in writeScriptBin "jdtls" ''
  #!/bin/sh
  mkdir -p .jdtls ${workspace}
  cp ${jdtls}/config*/config.ini .jdtls
  ${jdk21}/bin/java \
    -Declipse.application=org.eclipse.jdt.ls.core.id1 \
    -Dosgi.bundles.defaultStartLevel=4 \
    -Declipse.product=org.eclipse.jdt.ls.core.product \
    -Dlog.protocol=true \
    -Dlog.level=ALL \
    -Xmx4g \
    -javaagent:${lombok}/share/java/lombok.jar \
    --add-modules=ALL-SYSTEM \
    --add-opens java.base/java.util=ALL-UNNAMED \
    --add-opens java.base/java.lang=ALL-UNNAMED \
    -jar ${jdtls}/plugins/org.eclipse.equinox.launcher_*.jar \
    -configuration .jdtls \
    -data ${workspace}
''
