VERSION 0.8
ARG --global BASE_IMAGE=earthly/dind:alpine
ARG --global BUILD_IMAGE=gradle:8.9.0-jdk17-alpine

FROM ${BASE_IMAGE}
WORKDIR /app
COPY .version .
ARG --global PIPELINE_ID
ARG --global APP_BASE_VERSION=$(cat .version | head -1)
ARG --global APP_VERSION=${APP_BASE_VERSION}.${PIPELINE_ID}
ARG --global GPG_PASSPHRASE
ARG --global OSSRH_TOKEN_USERNAME
ARG --global OSSRH_TOKEN_PASSWORD

build-base:
  FROM ${BUILD_IMAGE}
  ENV GRADLE_USER_HOME=/app
  WORKDIR /app
  CACHE --sharing shared /home/gradle/.gradle
  CACHE --sharing shared /root/.m2
  SAVE IMAGE --cache-hint

check:
  FROM +build-base
  COPY . .
  RUN gradle prepush --no-parallel --no-daemon

publish:
  FROM +build-base
  COPY . .
  RUN echo $(cat ./gpg_private_key)
  RUN echo ${GPG_PASSPHRASE}
  RUN echo ${OSSRH_TOKEN_USERNAME}
  RUN echo ${OSSRH_TOKEN_PASSWORD}
#  RUN export GPG_PRIVATE_KEY=$(cat ./gpg_private_key)
#  RUN export VERSION_HEADER=$'Version: GnuPG v2\n\n'
#  RUN export ORG_GRADLE_PROJECT_signingKey=${GPG_PRIVATE_KEY#"$VERSION_HEADER"}
#  RUN --secret GPG_PASSPHRASE export ORG_GRADLE_PROJECT_signingPassword="$GPG_PASSPHRASE"
#  RUN --secret OSSRH_TOKEN_USERNAME export OSSRH_TOKEN_USERNAME="$OSSRH_TOKEN_USERNAME"
#  RUN --secret OSSRH_TOKEN_PASSWORD export OSSRH_TOKEN_PASSWORD="$OSSRH_TOKEN_PASSWORD"
#  RUN gradle publish --no-parallel --no-daemon

ci-check:
  BUILD +check

ci-publish:
  BUILD +publish
