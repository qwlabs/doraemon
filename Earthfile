VERSION 0.8
ARG --global BASE_IMAGE=earthly/dind:alpine
ARG --global GRADLE_IMAGE=bitnami/gradle:8.9.0

FROM ${BASE_IMAGE}
WORKDIR /app
COPY .version .
ARG --global PIPELINE_ID
ARG --global APP_BASE_VERSION=$(cat .version)
ARG --global APP_VERSION=${APP_BASE_VERSION}.${PIPELINE_ID}

build-base:
  FROM ${GRADLE_IMAGE}
  WORKDIR /app
  CACHE --sharing shared /home/gradle/.gradle
  CACHE --sharing shared /root/.m2

check:
  FROM +build-base
  COPY . .
  RUN ./gradlew clean test

publish:
  FROM +build-base
  COPY . .
  RUN export VERSION_HEADER=$'Version: GnuPG v2\n\n'
  RUN --secret GPG_PRIVATE_KEY export ORG_GRADLE_PROJECT_signingKey=${GPG_PRIVATE_KEY#"$VERSION_HEADER"}
  RUN --secret GPG_PASSPHRASE export ORG_GRADLE_PROJECT_signingPassword="$GPG_PASSPHRASE"
  RUN --secret OSSRH_TOKEN_USERNAME export OSSRH_TOKEN_USERNAME="$OSSRH_TOKEN_USERNAME"
  RUN --secret OSSRH_TOKEN_PASSWORD export OSSRH_TOKEN_PASSWORD="$OSSRH_TOKEN_PASSWORD"
  RUN ./gradlew clean publish

ci-check:
  BUILD +check

ci-publish:
  BUILD +publish
