.PHONY: up down run test build

up:
	docker-compose up -d

down:
	docker-compose down

run:
	./mvnw spring-boot:run

test:
	./mvnw test

build:
	./mvnw clean package -DskipTests