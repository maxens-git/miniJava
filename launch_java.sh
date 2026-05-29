ant -f minijava-build.xml generate
ant -f minijava-build.xml compile 

select test in tests_java/test*.mjava; do
    java -cp "bin/cls:tools/commons-lang3-3.7.jar:tools/commons-text-1.2.jar:tools/antlr-4.13.1-complete.jar:$CLASSPATH" fr.n7.stl.minijava.Driver "$test"
    java -jar runtam.jar "${test%.mjava}.tam"
    break
done