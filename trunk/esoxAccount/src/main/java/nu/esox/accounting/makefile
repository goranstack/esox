#---------------------------------------------------------------------------
#   $Revision: 1.8 $
#       $Date: 2006/03/08 10:54:57 $
#     $Author: dennis $
#       $Name:  $
#      
# Description: makefile used when compiling the VDL 4000-10-4.x software
#              in the Communication module. For information on usage run the
#              command make info on the top-level.
#
#---------------------------------------------------------------------------

MAKEHOME ?= $(shell y=.; while [ ! -e rules.mk ]; do y=$$y/..;cd ..; done; echo $$y)

RUN =  -Dswing.aatext=false nu.esox.accounting.Main   db.xml

-include $(MAKEHOME)/rules.mk

#MAIN ?= nu.esox.accounting.Import
#MAIN ?= nu.esox.accounting.Main
#TitlePager 2006
#Main
# xxx/*.txt

#run : default
#	java -ea -classpath $(CLASSPATH) $(MAIN)
