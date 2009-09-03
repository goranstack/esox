ESOX_CORE = $(MAKEHOME)/../esoxCore/esoxCore.jar
ESOX_GUI = $(MAKEHOME)/../esoxGui/esoxGui.jar

LIBS = $(ESOX_CORE) $(ESOX_GUI)
LIBS_CP = $(ESOX_CORE)$(CP_SEP)$(ESOX_GUI)

$(ESOX_CORE):
	cd $(MAKEHOME)/../esoxCore; make jar

$(ESOX_GUI):
	cd $(MAKEHOME)/../esoxGui; make jar
