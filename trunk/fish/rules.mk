CP_SEP = $(shell if ( uname | grep -iq cygwin ); then echo ";"; else echo ":"; fi)

first : default

-include $(MAKEHOME)/libs.mk

JAVAC = /usr/java/latest/bin/javac
JAVADOC = /usr/java/latest/bin/javadoc
JAVA = /usr/java/latest/bin/java
#RMIC = rmic

CLASSPATH = "$(MAKEHOME)$(CP_SEP)$(LIBS_CP)"

JAVAC_FLAGS = -g -classpath $(CLASSPATH) -d $(MAKEHOME) $(PROJECT_JAVAC_FLAGS) -Xlint -encoding iso-8859-1
# -Xlint
#JAVADOC_FLAGS = -classpath $(CLASSPATH) -d $(TARGET)/DOC $(PROJECT_JAVADOC_FLAGS)
JAVA_FLAGS = -ea -classpath $(CLASSPATH)$(CP_SEP) $(PROJECT_JAVA_FLAGS)
#RMIC_FLAGS = -classpath $(JAVA_SOURCE_HOME) -sourcepath $(JAVA_SOURCE_HOME) -d $(TARGET) $(PROJECT_RMIC_FLAGS)

JAVAFILES := $(wildcard *.java)
CLASSFILES ?= $(JAVAFILES:.java=.class)

ALLDIRS ?= $(shell dirs=""; for f in [abcdefghijklmnopqrtstuvwxuz]*; do if [ -d $$f ]; then dirs=$$dirs" "$$f; fi; done; echo $$dirs)
DIRS ?= $(shell dirs=""; for f in [abcdefghijklmnopqrtstuvwxuz]*; do if [ -d $$f -a -e $$f/makefile ]; then dirs=$$dirs" "$$f; fi; done; echo $$dirs)

%.java :

%.class : %.java
	$(JAVAC) $(JAVAC_FLAGS) $<


.PHONY : default
default : $(LIBS)
default : TARGET = default
default : dirs
default : $(CLASSFILES)

all :
	make clear
	make default

.PHONY : clear
clear : TARGET = clear
clear : dirs
	-@rm -f *.class

clearall : clear
	-@rm -f *.jar

.PHONY : clean
clean : TARGET = clean
clean : dirs
	-@rm -f *~

.PHONY : fix
fix : TARGET = fix
fix : dirs

make :
	for d in $(ALLDIRS); do if [ ! -e $$d/makefile ]; then head -3 makefile > $$d/makefile; fi; make -C $$d make; done

forcemake :
	for d in $(ALLDIRS); do head -3 makefile > $$d/makefile; make -C $$d forcemake; done

.PHONY: dirs $(DIRS)

dirs: $(DIRS)

$(DIRS): 
	@echo -------------------------- $@ -----------------------------
	@$(MAKE) -C $@ $(TARGET);


#ARNE = "MAKEHOME ?= $$(shell y=.; while [ ! -e rules.mk ]; do y=$y/..;cd ..; done; echo $y)"
#
fix :
#	cat makefile | awk '/^MAKEHOME/ {print $(ARNE);next} {print}' > tmp.mk
#	mv tmp.mk makefile

PKG ?= $(shell y=$$PWD; while [ ! -e rules.mk ]; do cd ..; done; echo $${y//$$PWD} | sed -e 's/\///' | sed -e 's/\//./g')


.PHONY : info
info : geninfo
geninfo : 
	@echo "make info          Show this text"
	@echo "make               Compile java files"
	@echo "make clean         Remove all *~"
	@echo "make clear         Remove all class files"
	@echo "make clearall      Remove all build products"
	@echo "make all           Recompile all java files"
	@echo "make tags          Regenerate TAGS file for use with Emacs"
	@echo "make make          Create makefiles where missing"
	@echo "make forcemake     Recreate makefiles everywhere"

-include $(MAKEHOME)/local.mk

run : default
	$(JAVA) $(JAVA_FLAGS) $(MAIN_CLASS)
