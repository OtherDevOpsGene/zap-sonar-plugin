#!make

# Makefile for Demo Auth Serve
SHELL := /bin/sh

export APP_URL_UNDER_TEST ?= 'https://oneprofile.io/auth'
export PLUGIN_VERSION ?= 2.2.0

$(info URL of the application under test = $(APP_URL_UNDER_TEST))

#build:
#	@docker-compose build sonarqube-build

sonarqube:
	@wget https://github.com/Coveros/zap-sonar-plugin/releases/download/sonar-zap-plugin-${PLUGIN_VERSION}/sonar-zap-plugin-${PLUGIN_VERSION}.jar -O ./plugin/sonar-zap-plugin-${PLUGIN_VERSION}.jar
	@docker-compose up -d sonarqube

zap:
	@docker-compose up owasp-zap

sonar-scan:
	@docker-compose up sonar-scanner

zap-scan: zap sonar-scan

run:
	@wget https://github.com/Coveros/zap-sonar-plugin/releases/download/sonar-zap-plugin-${PLUGIN_VERSION}/sonar-zap-plugin-${PLUGIN_VERSION}.jar -O ./plugin/sonar-zap-plugin-${PLUGIN_VERSION}.jar
	@docker-compose up -d sonarqube
	@sleep 75
	@docker-compose up owasp-zap
	@docker-compose up sonar-scanner

down:
	@docker-compose down
