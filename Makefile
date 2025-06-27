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

# Executar SonarQube Scanner
sonar:
	$(GRADLEW) sonar -Dsonar.login="$(SONAR_TOKEN)"

# Instalar Firebase CLI
setup-firebase:
	curl -sL https://firebase.tools | bash

# Configurar Keystore
setup-keystore:
	# Mostra o diretório de trabalho
	echo "Working directory: $PWD"
	# Cria um diretório temporário para a keystore
	mkdir -p ${PWD}/app/keystore
	# Define o caminho completo da keystore
	KEYSTORE_PATH="${PWD}/app/keystore/release.keystore"
	echo "Keystore path: $KEYSTORE_PATH"
	# Verifica se a variável ENCODED_KEYSTORE existe
	if [ -z "$ENCODED_KEYSTORE" ]; then \
	  echo "ERROR: ENCODED_KEYSTORE is empty or not set"; \
	  exit 1; \
	fi
	# Decodifica a keystore
	echo "$ENCODED_KEYSTORE" | base64 -d > "$KEYSTORE_PATH"
	# Verifica se o arquivo foi criado e seu tamanho
	if [ -f "$KEYSTORE_PATH" ]; then \
	  echo "Keystore created successfully"; \
	  ls -la "$KEYSTORE_PATH"; \
	  echo "Keystore size: $(wc -c < "$KEYSTORE_PATH") bytes"; \
	else \
	  echo "ERROR: Failed to create keystore file"; \
	  exit 1; \
	fi
	# Ajusta as permissões
	chmod 600 "$KEYSTORE_PATH"

# Configurar Mapbox Token
setup-mapbox:
	if [ -z "$MAPBOX_ACCESS_TOKEN" ]; then \
	  echo "ERROR: MAPBOX_ACCESS_TOKEN is not set"; \
	  exit 1; \
	fi
	# Cria local.properties se não existir
	touch local.properties
	# Remove linha antiga do MAPBOX_ACCESS_TOKEN se existir
	sed -i '/MAPBOX_ACCESS_TOKEN/d' local.properties
	# Adiciona novo token
	echo "MAPBOX_ACCESS_TOKEN=$MAPBOX_ACCESS_TOKEN" >> local.properties
	# Mostra o conteúdo do arquivo (sem mostrar o token completo)
	echo "Content of local.properties (masked):"
	cat local.properties | sed 's/\(MAPBOX_ACCESS_TOKEN=\).*/\1****/'

# Ambiente de Debug
debug-environment:
	echo "Workspace: $PWD"
	echo "List of environment variables:"
	env | grep -v ENCODED_KEYSTORE
	echo "Directory structure:"
	ls -R

# Deploy Firebase
deploy-firebase:
	firebase appdistribution:distribute \
	   app/build/outputs/apk/staging/release/app-staging-release.apk \
	   --app ${FIREBASE_APP_ID} \
	   --groups "testers" \
	   --token "${FIREBASE_TOKEN}" \
	   --release-notes "Build from main branch - $$(git describe --tags --abbrev=0)"

