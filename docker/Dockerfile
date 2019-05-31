FROM alpine:3.7

WORKDIR /usr/src/app

COPY ./src/main/resources/extract_features.ln extract_features.ln

RUN mkdir processed-images && mkdir images-to-process

# When no process is running, docker kills container
RUN tail -f /dev/null &
