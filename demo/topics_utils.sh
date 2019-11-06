#!/bin/bash

source util.sh

KAFKA_CREATE_CMD='kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic'
KAFKA_LIST_TOPIC_CMD='kafka-topics --list --bootstrap-server localhost:9092'
DOCKER_CMD='docker exec'
ZK_SHELL_CMD='zookeeper-shell localhost:2181'

create_topic() {
    TOPIC_NAME=${1}
    ${DOCKER_CMD} 'broker' ${KAFKA_CREATE_CMD} ${TOPIC_NAME}

    if [ $? -eq 0 ]; then
        log_ok "Topic created: ${TOPIC_NAME}"
    else
        log_warn "Topic could not be created: ${TOPIC_NAME}"
    fi
}

list_topics() {
    log_info "The following topics exist:"
    ${DOCKER_CMD} 'broker' ${KAFKA_LIST_TOPIC_CMD}
}
