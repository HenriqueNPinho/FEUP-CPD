.PHONY: default build clean

default: build

build:	
	sudo sh -c 'echo -1 >/proc/sys/kernel/perf_event_paranoid'
	g++ -O2 matrixproduct.cpp -o matrix -lpapi
	javac MatrixProd.java
	
clean:
	rm -rf matrix
	rm -rf MatrixProd.class