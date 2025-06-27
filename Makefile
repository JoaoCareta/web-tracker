# Variáveis
GRADLEW = ./gradlew

# Permissões
permissions:
	chmod +x $(GRADLEW)

# Limpar projeto
clean:
	$(GRADLEW) clean

# Análise estática com Detekt
detekt:
	$(GRADLEW) detekt

# Build do projeto
build-staging:
	$(GRADLEW) assembleStagingRelease

# Testes unitários
test:
	$(GRADLEW) testStagingDebugUnitTest

# Relatórios de cobertura
coverage:
	$(GRADLEW) jacocoTestReportStaging
	$(GRADLEW) collectJacocoReports

# Deploy Firebase
deploy-firebase:
	firebase appdistribution:distribute \
		app/build/outputs/apk/staging/release/app-staging-release.apk \
		--app ${FIREBASE_APP_ID} \
		--groups "testers" \
		--token "${FIREBASE_TOKEN}" \
		--release-notes "Build from main branch - $$(git describe --tags --abbrev=0)"
