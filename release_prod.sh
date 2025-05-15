#!/bin/bash

# Função para extrair números da versão
get_version_numbers() {
    local version=$1
    local major=$(echo $version | cut -d. -f1)
    local minor=$(echo $version | cut -d. -f2)
    local patch=$(echo $version | cut -d. -f3)
    echo "$major $minor $patch"
}

# Buscar última tag de produção
last_tag=$(git tag --sort=-v:refname | grep "release/prod/" | head -n 1)
if [ -z "$last_tag" ]; then
    echo "Nenhuma tag de produção encontrada"
    exit 1
fi

# Extrair versão atual
current_version=$(echo $last_tag | cut -d'/' -f3)
echo "Versão atual: $current_version"

# Separar números da versão
read major minor patch <<< $(get_version_numbers $current_version)

# Incrementar patch
new_patch=$((patch + 1))
new_version="$major.$minor.$new_patch"
new_tag="release/prod/$new_version"

echo "Nova versão será: $new_version"

# Criar e enviar nova tag
git tag $new_tag
git push main $new_tag

# Buildar versão de produção
./gradlew assembleProdRelease

# Enviar para o Firebase
firebase appdistribution:distribute \
    app/build/outputs/apk/prod/release/app-prod-release.apk \
    --app 1:868095324397:android:3da51a1d6b980aa0d6dce4 \
    --groups "testers" \
    --release-notes "Release versão $new_version"

echo "Processo finalizado com sucesso!"