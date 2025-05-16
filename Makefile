# Variáveis
GRADLEW = ./gradlew
FIREBASE_CLI = firebase

# Garantir permissão de execução do gradlew
permissions:
	chmod +x $(GRADLEW)

# Limpar projeto
clean:
	$(GRADLEW) clean

# Análise estática com Detekt
detekt:
	$(GRADLEW) detekt

# Testes unitários e cobertura
test:
	$(GRADLEW) test

# Gerar e coletar relatórios Jacoco
coverage:
	$(GRADLEW) jacocoTestReportStaging
	$(GRADLEW) collectJacocoReports

# Análise Sonar
sonar:
	$(GRADLEW) sonarqube

# Build da versão de staging
build-staging:
	$(GRADLEW) assembleStagingRelease

# Deploy para Firebase
deploy-firebase:
	$(FIREBASE_CLI) appdistribution:distribute \
		app/build/outputs/apk/staging/release/app-staging-release.apk \
		--app ${FIREBASE_APP_ID} \
		--groups "testers" \
		--token "${FIREBASE_TOKEN}" \
		--release-notes "Build from main branch - $$(git describe --tags --abbrev=0)"

# Comando para executar pipeline completa
pipeline: permissions clean detekt test coverage sonar build-staging deploy-firebase

# Comando para executar apenas análises
analyze: permissions clean detekt test coverage sonar

# Comando para build e deploy
deploy: permissions clean build-staging deploy-firebase