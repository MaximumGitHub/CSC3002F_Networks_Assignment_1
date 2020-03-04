# Makefile for Server.java
# CSC3002F Networks Practical
# Authors
# Chelsea Van Coller	VNCCHE001
# Michael Brough		BRGMIC022
# Max Mcgown-Withers	MCGMAX001
# 02/03/2020

JAVA = java
JAVAC = javac
JFLAGS = -g
SRC = src/
DOC = doc/
BIN = bin/
JAVADOC = javadoc

.SUFFIXES: .java .class

.java.class:
	$(JAVAC) -cp .:./bin -d $(BIN) $(JFLAGS) $*.java
	
CLASSES = \
	$(SRC)Server.java\
	
default: classes

classes: $(CLASSES:.java=.class)

docs:
	$(JAVADOC) -d $(DOC) $(SRC)*.java
	
clean:
	$(RM) $(BIN)*.class
	
server:
	$(JAVAC) -d bin $(SRC)server.java
	$(JAVA) -cp bin server

client:
	$(JAVAC) -d bin $(SRC)client.java
	$(JAVA) -cp bin client
