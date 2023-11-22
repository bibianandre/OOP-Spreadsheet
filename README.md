# XXL application

How to compile:
In the diretory of the repository, set the classpath:
```
export CLASSPATH=$(pwd)/po-uilib/po-uilib.jar:$(pwd)/xxl-core/xxl-core.jar:$(pwd)/xxl-app/xxl-app.jar
```
Run manually:
```
make
```
Run tests:
```
./run_tests.sh
```
In case of
> bash: ./run_tests.sh: Permission denied
> 
run:
```
chmod +x run_tests.sh
```

