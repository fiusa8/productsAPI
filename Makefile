##DOCKER_FULL_IMAGE_NAME=springboot
##DOCKER_FULL_REG_TAG_NAME=tools.adidas-group.com:5000/fdp/rating-producer:1.0.0
##DOCKER_FULL_REG_LAST_NAME=springboot
ARGS="--spring.cloud.config.uri=http://config_server:8888"
##.PHONY: build dockerize run bash clean help
##.SILENT: tools

default: help

build: clean ## Build the application and prepare the docker image
	@mvn -f ./pom.xml install #dependency:copy -Dcopy.artifactId=productsAPI -DoutputDirectory=docker/

dockerize: build  ## Create docker image
	@docker build --pull --no-cache -t springboot .
	@rm -rf target/*.jar

config_server: ## Spring cloud config server
	@docker stop config_server
	@docker rm config_server
	@docker run -dit --network mynetwork -p 8888:8888 -v /Users/fustedav/Desktop/config:/tmp/configurations \
	-e SPRING_CLOUD_CONFIG_SERVER_GIT_URI=file:/tmp/configurations --name config_server configserver

mongo: ## Run docker mongo container
	@docker run --rm --network mynetwork --name mongodb -p 27017:27017 -d mongo:4.0.3

run: ## Run docker spring container
	@docker run --rm --network mynetwork --link mongodb --link config_server --name springboot -p 8080:8080 -p 8443:8443 springboot ${ARGS}
	
#bash: ## Run docker container
#	@docker run -it --rm --network mynetwork --name springboot --entrypoint bash ${DOCKER_FULL_REG_LAST_NAME}

clean: ## Clean generated files from target folder
	@rm -rf target/*.jar

help: ## Show this help.
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'