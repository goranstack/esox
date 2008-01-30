
MAKEHOME ?= $(shell y=.; while [ ! -e rules.mk ]; do y=$$y/..;cd ..; done; echo $$y)

RUN =  -Dswing.aatext=false nu.esox.accounting.Main   db.xml

-include $(MAKEHOME)/rules.mk

