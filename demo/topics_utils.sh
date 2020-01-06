#!/bin/bash

source util.sh

CONTAINER_NAME='broker'
BOOTSTRAP_SERVER='localhost:9092'
KAFKA_CREATE_CMD="kafka-topics --create --bootstrap-server ${BOOTSTRAP_SERVER} --replication-factor 1 --partitions 1 --config retention.bytes=10485760 --topic"
KAFKA_LIST_TOPIC_CMD="kafka-topics --list --bootstrap-server ${BOOTSTRAP_SERVER}"
KAFKA_DESCRIBE_TOPIC_CMD="kafka-topics --describe --bootstrap-server ${BOOTSTRAP_SERVER} --topic"
DOCKER_CMD='docker exec'
ZK_SHELL_CMD='zookeeper-shell localhost:2181'

create_topic() {
    TOPIC_NAME=${1}
    ${DOCKER_CMD} ${CONTAINER_NAME} ${KAFKA_CREATE_CMD} ${TOPIC_NAME}

    if [ $? -eq 0 ]; then
        log_ok "Topic created: ${TOPIC_NAME}"
    else
        log_warn "Topic could not be created: ${TOPIC_NAME}"
    fi
}

list_topics() {
    log_info "The following topics exist:"
    ${DOCKER_CMD} ${CONTAINER_NAME} ${KAFKA_LIST_TOPIC_CMD}
}

describe_topic() {
    TOPIC_NAME=${1}
    log_info "Description of topic $TOPIC_NAME:"
    ${DOCKER_CMD} ${CONTAINER_NAME} ${KAFKA_DESCRIBE_TOPIC_CMD} ${TOPIC_NAME}
}

alter_topic() {
    TOPIC_NAME=${1}
    CONFIG=${2}


}
