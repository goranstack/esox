MAKEHOME ?= $(shell y=.; while [ ! -e rules.mk ]; do y=$$y/..;cd ..; done; echo $$y)

-include $(MAKEHOME)/rules.mk
