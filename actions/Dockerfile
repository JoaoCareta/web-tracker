FROM alpine:3.10
COPY entrypoint.sh /entrypoint.sh
RUN apk update && apk add bash git curl jq && apk add --update nodejs npm && npm install -g semver
ENTRYPOINT ["/entrypoint.sh"]
