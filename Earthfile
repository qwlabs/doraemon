VERSION 0.8
ARG --global BASE_IMAGE=earthly/dind:alpine
ARG --global BUILD_IMAGE=bitnami/gradle:8.9.0

FROM ${BASE_IMAGE}
WORKDIR /app
COPY .version .
ARG --global PIPELINE_ID
ARG --global APP_BASE_VERSION=$(cat .version)
ARG --global APP_VERSION=${APP_BASE_VERSION}.${PIPELINE_ID}

build-base:
  FROM ${BUILD_IMAGE}
  WORKDIR /app
  COPY . .
  RUN gradle clean
  SAVE IMAGE --cache-hint

check:
  FROM +build-base
#  RUN gradle :lang:test --stacktrace --no-parallel --no-daemon
#  RUN gradle :basic-tree:test  --tests "com.qwlabs.tree.LocationComparatorTest" --stacktrace --no-parallel --no-daemon
  RUN gradle prepush --no-parallel --no-daemon

publish:
  FROM +build-base
  RUN export VERSION_HEADER=$'Version: GnuPG v2\n\n'
  RUN --secret GPG_PRIVATE_KEY export ORG_GRADLE_PROJECT_signingKey=${GPG_PRIVATE_KEY#"$VERSION_HEADER"}
  RUN --secret GPG_PASSPHRASE export ORG_GRADLE_PROJECT_signingPassword="$GPG_PASSPHRASE"
  RUN --secret OSSRH_TOKEN_USERNAME export OSSRH_TOKEN_USERNAME="$OSSRH_TOKEN_USERNAME"
  RUN --secret OSSRH_TOKEN_PASSWORD export OSSRH_TOKEN_PASSWORD="$OSSRH_TOKEN_PASSWORD"
  RUN gradle publish --no-parallel --no-daemon

ci-check:
  BUILD +check

ci-publish:
  BUILD +publish
