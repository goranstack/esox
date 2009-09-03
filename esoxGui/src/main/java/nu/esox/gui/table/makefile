MAKEHOME ?= $(shell y=.; while [ ! -e rules.mk ]; do y=$$y/..; cd ..; done; echo $$y)

-include $(MAKEHOME)/rules.mk

tags : 
	@echo "In Emacs press M-. to follow a tag, M-* to get back!"
	@echo $(MAKEHOME)
	-@rm -f $(MAKEHOME)/TAGS
	cd $(MAKEHOME); ( find . -name '*.java') | etags -
