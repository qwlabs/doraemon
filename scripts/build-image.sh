#!/usr/bin/env bash

set -e

DIR="$(cd "$( dirname "${BASH_SOURCE[0]}" )" && cd "../" && pwd)"

echo start build image...

DOCKER_REGISTRY="registry.cn-hangzhou.aliyuncs.com"
DOCKER_IMAGE_NAME="qwlabs/crp-server"
APP_BASE_VERSION="$(cat ${DIR}/.version)"
SERVICE_NAME=$1
SERVICE_DIR=${DIR}/services/${SERVICE_NAME}

if [ -z "${PIPELINE_ID}" ];then
	APP_VERSION=${APP_BASE_VERSION}.dev-${SERVICE_NAME}
else
  APP_VERSION=${APP_BASE_VERSION}.${PIPELINE_ID}-${SERVICE_NAME}
fi

echo start build image version:"${APP_VERSION}"...

DOCKER_BUILDKIT=1  docker build "${DIR}" -f ${SERVICE_DIR}/src/main/docker/Dockerfile \
  -t ${DOCKER_REGISTRY}/${DOCKER_IMAGE_NAME}:"${APP_VERSION}" \
  --build-arg APP_VERSION=${APP_VERSION}  \
  --build-arg SERVICE_NAME=${SERVICE_NAME}

if [ "${PIPELINE_ID}" ];then
    echo "${DOCKER_LOGIN_PASSWORD}" | docker login --username="${DOCKER_LOGIN_USER}" --password-stdin ${DOCKER_REGISTRY}
    docker push ${DOCKER_REGISTRY}/${DOCKER_IMAGE_NAME}:${APP_VERSION}
fi


