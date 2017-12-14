################################################################################
#
# The general file for making this project
#
# @author Jack
# @version 1.0
# @date Thu Dec 14 22:02:09 2017
#
################################################################################


#===============================================================================
#
# Debug
#
#===============================================================================

ifdef DEV
	SILENT := 
else
	SILENT := @
endif

#===============================================================================
#
# Variables
#
#===============================================================================


NINJA := ninja
CAT := cat
CD := cd
ECHO := echo
COMMIT = $(shell $(GIT) rev-parse --short HEAD)
CP := cp
CTAGS := ctags -R
DVIPDF := dvipdf
ECHO := echo
GIT := git
JAR := jar
JAVA := java
JDB := jldb
JSON_PP := json_pp
LATEX := latex
M4 := m4 -I /opt/local/include/ -I m4
MAVEN := mvn
MKDIR := mkdir -p
PHP := php
RM := rm -rf
RSYNC := rsync -avz -e "ssh -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null" --progress 
SASS := sass
SCP := scp
MESON := meson
SSH := ssh
SUDO := sudo
UGLIFYJS := uglifyjs
UNZIP := unzip
RELOAD_CHROME := $(SILENT) sh chrome.sh reload
RELOAD_SAFARI := $(SILENT) sh safari.sh reload
XELATEX := xelatex
ECLIPSE_FILES := .project .classpath
VERSION := $(shell jjs tool.js)
NAME := $(shell jjs tool.js -- name)
SRC := $(shell find src)
TARGET := target/$(NAME)-$(VERSION).jar


#===============================================================================
#
# Core Functions
#
#===============================================================================

rwildcard=$(foreach d,$(wildcard $1*),$(call rwildcard,$d/,$2) $(filter $(subst *,%,$2),$d))
uniq = $(if $(1),$(strip $(word 1,$(1)) $(call uniq,$(filter-out $(word 1,$(1)),$(1)))))
ssh_exec = $(shell $(SSH) root@$(1) $(2))

#===============================================================================
#
# Tasks
#
#===============================================================================

run: $(TARGET)
	$(SILENT) java -jar $(TARGET)
package: $(TARGET)
.PHONY: package

clean:
	$(SILENT) mvn clean
.PHONY: clean

dev: $(ECLIPSE_FILES)
.PHONY: dev

$(TARGET): $(SRC) pom.xml
	$(SILENT) mvn package

$(ECLIPSE_FILES): pom.xml
	$(SILENT) mvn eclipse:eclipse

