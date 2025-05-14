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

shopt -s nocasematch
if [[ "$GITHUB_COMMIT_MESSAGE" =~ [^a-zA-Z](#minor)[^a-zA-Z] ]]; then
    echo "Starting a new minor RC"
    current_tag="release/staging/$(semver -i minor "$last_prod_version")-RC.1"
elif [[ "$GITHUB_COMMIT_MESSAGE" =~ [^a-zA-Z](#[Rr][Cc])[^a-zA-Z] ]]; then
    if [ "$last_prod_version" = "$last_staging_version" ]; then
        echo "Starting a new patch RC"
        current_tag="release/staging/$(semver -i patch "$last_staging_version")-RC.1"
    else
        echo "Bumping current RC"
        suffix="RC"
        current_tag="release/staging/$(semver -i prerelease "$last_staging_version-$last_staging_rc" --preid $suffix)"
    fi
else
    echo "Nothing to do!";
    exit 0
fi
shopt -u nocasematch

tag_version "$current_tag"

exit 0;
