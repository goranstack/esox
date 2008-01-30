MAIN_CLASS = 

info : localinfo
localinfo : 
	@echo "make jar           Build esoxCore.jar"

JAR ?= $${PWD/*\//}".jar"

jar:
	make
	jar cf $${PWD/*\//}".jar" `find nu -path "*/.svn" -prune -or -not -name "*.java" -and -not -name "makefile" -and -type f -print`

%.jar : jar
	-rm -rf tmp_dir
	mkdir tmp_dir
	export JAR=$(JAR); make -C tmp_dir -f ../makefile addjar
	mv tmp_dir/tmp.jar $*.jar
	rm -rf tmp_dir

addjar :
	cp ../$(JAR) tmp.jar
	for f in `echo $(LIBS_CP) | sed 's/$(CP_SEP)/ /g'`; do jar xf $$f; done
	jar uf tmp.jar nu

exec : lsdk.jar
	echo "Main-Class: nu.esox.accounting.Main" > lsdk.mf
	jar ufm lsdk.jar lsdk.mf
	rm lsdk.mf
