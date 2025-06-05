#!/bin/bash

set -e  # Script falha se qualquer comando falhar

# Cores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

# Função para extrair números da versão
get_version_numbers() {
    local version=$1
    local major=$(echo $version | cut -d. -f1)
    local minor=$(echo $version | cut -d. -f2)
    local patch=$(echo $version | cut -d. -f3)
    echo "$major $minor $patch"
}

# Verificar se está na branch main
current_branch=$(git branch --show-current)
if [[ "$current_branch" != "main" ]]; then
    echo -e "${RED}Erro: Você deve estar na branch main${NC}"
    exit 1
fi

# Verificar mudanças pendentes
if [[ -n $(git status -s) ]]; then
    echo -e "${RED}Erro: Existem mudanças não commitadas${NC}"
    git status
    exit 1
fi

# Deletar todas as tags locais
echo -e "${YELLOW}Deletando todas as tags locais...${NC}"
git tag | xargs git tag -d

# Atualizar o projeto
echo -e "${YELLOW}Atualizando o projeto...${NC}"
git fetch

# Buscar última tag de produção
git fetch --tags
last_tag=$(git tag --sort=-v:refname | grep "release/prod/" | head -n 1)
if [ -z "$last_tag" ]; then
    echo -e "${RED}Erro: Nenhuma tag de produção encontrada${NC}"
    exit 1
fi

# Extrair versão atual
current_version=$(echo $last_tag | cut -d'/' -f3)
echo -e "${GREEN}Versão atual: $current_version${NC}"

# Separar números da versão
read major minor patch <<< $(get_version_numbers $current_version)

# Perguntar se deseja incrementar minor version
read -p "Deseja incrementar a minor version? (y/n) " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]; then
    new_minor=$((minor + 1))
    new_patch=0
    new_version="$major.$new_minor.$new_patch"
else
    new_patch=$((patch + 1))
    new_version="$major.$minor.$new_patch"
fi

new_tag="release/prod/$new_version"

echo -e "${YELLOW}Nova versão será: $new_version${NC}"

# Confirmar ação
read -p "Deseja continuar? (y/n) " -n 1 -r
echo
if [[ ! $REPLY =~ ^[Yy]$ ]]; then
    echo "Operação cancelada"
    exit 1
fi

# Criar e enviar nova tag
echo "Criando nova tag..."
git tag $new_tag
git push main $new_tag

# Buildar versão de produção
echo "Buildando versão de produção..."
if ! ./gradlew assembleProdRelease; then
    echo -e "${RED}Erro no build!${NC}"
    exit 1
fi

# Verificar se o APK foi gerado
APK_PATH="app/build/outputs/apk/prod/release/app-prod-release.apk"
if [ ! -f "$APK_PATH" ]; then
    echo -e "${RED}Erro: APK não foi gerado no caminho esperado${NC}"
    exit 1
fi

# Enviar para o Firebase
echo "Enviando para o Firebase..."
if ! firebase appdistribution:distribute \
    "$APK_PATH" \
    --app "1:868095324397:android:3da51a1d6b980aa0d6dce4" \
    --groups "testers" \
    --release-notes "Release versão $new_version"; then
    echo -e "${RED}Erro ao enviar para o Firebase!${NC}"
    exit 1
fi

echo -e "${GREEN}Processo finalizado com sucesso!${NC}"
echo "Nova versão $new_version está disponível no Firebase"
