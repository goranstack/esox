ESOX_CORE = $(MAKEHOME)/../esoxCore/esoxCore.jar
ESOX_GUI = $(MAKEHOME)/../esoxGui/esoxGui.jar
ESOX_XML = $(MAKEHOME)/../esoxXml/esoxXml.jar

LIBS = $(ESOX_CORE) $(ESOX_GUI) $(ESOX_XML) 
LIBS_CP = $(ESOX_CORE)$(CP_SEP)$(ESOX_GUI)$(CP_SEP)$(ESOX_XML)

$(ESOX_CORE):
	cd $(MAKEHOME)/../esoxCore; make jar

$(ESOX_GUI):
	cd $(MAKEHOME)/../esoxGui; make jar

$(ESOX_XML):
	cd $(MAKEHOME)/../esoxXml; make jar
