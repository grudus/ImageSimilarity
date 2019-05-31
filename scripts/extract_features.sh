#!/usr/bin/env bash



DOCKER_WORKDIR=/usr/src/app

IMAGES_DIR=images-to-process
HOST_PROCESSED_IMAGES_DIR=../src/main/resources/processed-images
HOST_IMAGES_DIR=../src/main/resources/images-to-process
IMAGE_TO_PROCESS=${HOST_IMAGES_DIR}/$1
PROCESSED_IMAGE="$1.haraff.sift.png"
PROCESSED_INFO="$1.haraff.sift"

if [[ -z "$1" ]]
  then
    >&2 echo "Specify image to extract as first argument!"
    >&2 echo "For example: '$0 image.ppm' will look for file image.ppm in the $HOST_IMAGES_DIR directory"
    exit 1
fi


if [[ ! -f ${IMAGE_TO_PROCESS} ]]; then
    >&2 echo "Cannot find file $IMAGE_TO_PROCESS!"
    exit 2
fi

DOCKER_ID=$(docker ps | grep extract_features | awk '{print $1}')

if [[ -z ${DOCKER_ID} ]]; then
    >&2 echo "Docker container with name 'extract_features' is not running!"
    >&2 echo "Run 'docker-compose up -d' in the project-root directory and try again."
    exit 3

else
    echo "Extracting features in docker container with id $DOCKER_ID"
    echo
fi


echo "Copy ${IMAGE_TO_PROCESS} file to docker directory $DOCKER_ID:$DOCKER_WORKDIR/$IMAGES_DIR"
docker cp ${IMAGE_TO_PROCESS} ${DOCKER_ID}:${DOCKER_WORKDIR}/${IMAGES_DIR}

echo "Start extracting features"
echo


docker exec -i ${DOCKER_ID} /bin/sh -c "./extract_features.ln -haraff -sift -i $IMAGES_DIR/$1 -DE" \
&& echo -e "\nFile $1 extracted successfully" \
&& docker cp ${DOCKER_ID}:${DOCKER_WORKDIR}/${IMAGES_DIR}/${PROCESSED_IMAGE} ${HOST_PROCESSED_IMAGES_DIR} \
&& docker cp ${DOCKER_ID}:${DOCKER_WORKDIR}/${IMAGES_DIR}/${PROCESSED_INFO} ${HOST_PROCESSED_IMAGES_DIR} \
&& echo "Files copied to $HOST_PROCESSED_IMAGES_DIR"
