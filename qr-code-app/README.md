## Build a native image

```bash
cd kotlin-app

gradle -Pagent run
gradle metadataCopy --task run --dir src/main/resources/META-INF/native-image
gradle nativeCompile
```

## Run the native image

```bash
./qr-code-app -Djava.home=%JAVA_HOME% -Dsun.awt.fontconfig=./fontconfig.properties
```

This is not a good solution, since it requires the user to specify the fontconfig.properties file and a jdk must be in place,
which should not the case for a native image.

Also see
- https://github.com/oracle/graal/pull/5870
- https://github.com/oracle/graal/issues/6244
- https://github.com/openjdk/jdk/pull/11559
- https://github.com/oracle/graal/issues/2835#issuecomment-1333300103
