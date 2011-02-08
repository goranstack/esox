ESOX_CORE = $(MAKEHOME)/../../../../esoxCore/src/main/java/esoxCore.jar
ESOX_GUI = $(MAKEHOME)/../../../../esoxGui/src/main/java/esoxGui.jar
ESOX_XML = $(MAKEHOME)/../../../../esoxXml/src/main/java/esoxXml.jar

LIBS = $(ESOX_CORE) $(ESOX_GUI) $(ESOX_XML) 
LIBS_CP = $(ESOX_CORE)$(CP_SEP)$(ESOX_GUI)$(CP_SEP)$(ESOX_XML)

$(ESOX_CORE):
	cd $(MAKEHOME)/../../../../esoxCore/src/main/java; make jar

$(ESOX_GUI):
	cd $(MAKEHOME)/../../../../esoxGui/src/main/java; make jar

$(ESOX_XML):
	cd $(MAKEHOME)/../../../../esoxXml/src/main/java; make jar
