ECLIPSE_FILES := .project .classpath
VERSION := $(shell jjs tool.js)
NAME := $(shell jjs tool.js -- name)
SRC := $(shell find src)
TARGET := target/$(NAME)-$(VERSION).jar

