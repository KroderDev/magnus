.PHONY: build test lint clean shell up down help

build:
	docker compose run --rm dev ./gradlew build

test:
	docker compose run --rm dev ./gradlew test

lint:
	docker compose run --rm dev ./gradlew detekt

clean:
	docker compose run --rm dev ./gradlew clean

shell:
	docker compose run --rm dev

up:
	docker compose up -d postgres redis

down:
	docker compose down

help:
	@echo "Usage: make <target>"
	@echo ""
	@echo "Targets:"
	@echo "  build    Build the project (compile + package)"
	@echo "  test     Run all tests (unit + integration)"
	@echo "  lint     Run Detekt static analysis"
	@echo "  clean    Clean build artifacts"
	@echo "  shell    Open a shell in the dev container"
	@echo "  up       Start PostgreSQL and Redis services"
	@echo "  down     Stop all services"
