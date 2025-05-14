#!/bin/bash

set -o pipefail

tag_version(){
new=$1

git tag "$new"

full_name=$GITHUB_REPOSITORY
git_refs_url=$(jq .repository.git_refs_url "$GITHUB_EVENT_PATH" | tr -d '"' | sed 's/{\/sha}//g')

echo "Sending the new tag [$new] to the [$full_name] repository"

git_refs_response=$(
curl -s -X POST "$git_refs_url" \
-H "Authorization: token $GITHUB_TOKEN" \
-d @- << EOF

{
  "ref": "refs/tags/$new",
  "sha": "$GITHUB_SHA"
}
EOF
)

git_ref_posted=$( echo "${git_refs_response}" | jq .ref | tr -d '"' )

echo "GitHub response: ${git_refs_response}"

if [ "${git_ref_posted}" = "refs/tags/${new}" ]; then
  echo "New tag successfully created."
  exit 0
else
  echo "The new tag was not created"
  exit 1
fi
}

cd "${GITHUB_WORKSPACE}"/"${source}" || exit

# Gets last prod tag
last_prod_tag=$(git describe --tags --abbrev=0 --match="release/prod/*")
echo "Last prod tag: $last_prod_tag"

# Extracts last prod version from last prod tag, the result do not include sufixes added in hotfixes.
last_prod_version=$(echo "$last_prod_tag" | grep -E -o "release/prod/[0-9]+\.[0-9]+\.[0-9]+" | cut -d'/' -f 3)
echo "Last prod version: $last_prod_version"

# Gets last staging tag
last_staging_tag=$(git describe --tags --abbrev=0 --match="release/staging/*")
echo "Last staging tag: $last_staging_tag"

# Extracts last staging version from last staging tag, the result do not include sufixes added in hotfixes and RC's.
last_staging_version=$(echo "$last_staging_tag" | grep -E -o "[0-9]+\.[0-9]+\.[0-9]+" | head -n 1)
echo "Last staging version: $last_staging_version"

# Extracts the last RC number from the last staging tag
last_staging_rc=$(echo "$last_staging_tag" | grep -oE "RC\.[0-9]+" | cut -d'.' -f2)
echo "Last staging RC: $last_staging_rc"

case "$GITHUB_COMMIT_MESSAGE" in
  *#none*) exit 0 ;;
esac

# Extrai apenas o número da versão de prod (remove o prefixo release/prod/)
prod_version=$(echo "$last_prod_version" | grep -oE '[0-9]+\.[0-9]+\.[0-9]+')
echo "Current prod version: $prod_version"

shopt -s nocasematch
case "$GITHUB_COMMIT_MESSAGE" in
  *#minor* )
    echo "Starting a new minor RC"
    current_tag="release/staging/$(semver -i minor "$prod_version")-RC.1"
  ;;
  *#rc* )
    if [ -z "$last_staging_version" ]; then
      echo "No staging version found. Creating new RC based on prod version"
      current_tag="release/staging/$(semver -i patch "$prod_version")-RC.1"
    elif [ "$last_staging_version" = "$prod_version" ]; then
      echo "Staging version equals prod version. Creating new RC with patch increment"
      current_tag="release/staging/$(semver -i patch "$prod_version")-RC.1"
    else
      echo "Bumping current RC"
      next_rc=$((last_staging_rc + 1))
      current_tag="release/staging/${last_staging_version}-RC.${next_rc}"
    fi

    # Logs para debug
    echo "Production version: $prod_version"
    echo "Last staging version: $last_staging_version"
    echo "Last staging RC: $last_staging_rc"
  ;;
  *) echo "Nothing to do!."; exit 0 ;;
esac
shopt -u nocasematch

echo "Tag to be created: $current_tag"

tag_version "$current_tag"

exit 0;
