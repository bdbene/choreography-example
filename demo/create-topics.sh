#!/bin/bash

KAFKA_CMD='kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic'
DOCKER_CMD='docker exec broker'

function create_topic() {
    ${DOCKER_CMD} ${KAFKA_CMD} ${1}
}

create_topic orders
create_topic payments
