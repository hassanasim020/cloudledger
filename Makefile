.PHONY: dev stop test

dev:
	docker compose up --build

stop:
	docker compose down

test:
	cd backend && ./mvnw test
	cd frontend && npm ci && npm test -- --run

