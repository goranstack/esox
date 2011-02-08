ESOX_CORE = $(MAKEHOME)/../../../../esoxCore/src/main/java/esoxCore.jar

LIBS = $(ESOX_CORE)
LIBS_CP = $(ESOX_CORE)

$(ESOX_CORE):
	cd $(MAKEHOME)/../../../../esoxCore/src/main/java; make jar
