ESOX_CORE = $(MAKEHOME)/../esoxCore/esoxCore.jar

LIBS = $(ESOX_CORE)
LIBS_CP = $(ESOX_CORE)

$(ESOX_CORE):
	cd $(MAKEHOME)/../esoxCore; make jar
