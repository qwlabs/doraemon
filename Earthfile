VERSION 0.8
ARG --global BASE_IMAGE=earthly/dind:alpine
ARG --global BUILD_IMAGE=gradle:8.10.0-jdk17-alpine

FROM ${BASE_IMAGE}
WORKDIR /app
COPY .version .
ARG --global PIPELINE_ID
ARG --global APP_BASE_VERSION=$(cat .version | head -1)
ARG --global APP_VERSION=${APP_BASE_VERSION}.${PIPELINE_ID}
ARG --global MAVEN_SONATYPE_USERNAME
ARG --global MAVEN_SONATYPE_PASSWORD
ARG --global GPG_PRIVATE_KEY
ARG --global GPG_PASSPHRASE

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
  RUN export VERSION_HEADER=$'Version: GnuPG v2\n\n'
  RUN export ORG_GRADLE_PROJECT_signingKey=${GPG_PRIVATE_KEY#"$VERSION_HEADER"}
  RUN export ORG_GRADLE_PROJECT_signingPassword="$GPG_PASSPHRASE"
  RUN gradle publishAllPublicationsToCentralPortal --no-parallel --no-daemon

ci-check:
  BUILD +check

ci-publish:
  BUILD +publish
