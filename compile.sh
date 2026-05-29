ant generate
ant compile

for test in tests/test*.c; do
    java -cp "bin/cls:tools/commons-lang3-3.12.0.jar:tools/commons-text-1.9.jar:tools/antlr-4.13.1-complete.jar:$CLASSPATH" fr.n7.stl.minic.Driver "$test"
done
