MAIN_CLASS = 

info : localinfo
localinfo : 
	@echo "make jar           Build esoxCore.jar"

# doc : doc.zip

# doc.zip :
# 	-@rm -rf esox2doc
# 	$(JAVADOC) -encoding iso-8859-1 -classpath $(CLASSPATH) -d esox2doc nu.esox.util nu.esox.xml nu.esox.fos nu.esox.fos.client nu.esox.fos.server nu.esox.fos.shared nu.esox.gui nu.esox.gui.aspect nu.esox.gui.layout nu.esox.gui.list nu.esox.gui.model nu.esox.gui.table
# 	zip -r doc.zip esox2doc
# 	-@rm -rf esox2doc

jar : 
	make
	jar cf esoxXml.jar `find nu -path "*/.svn" -prune -or -not -name "*.java" -and -not -name "makefile" -and -type f -print`
