#!/bin/bash

source util.sh
source cluster_utils.sh
source topics_utils.sh

start_demo() {
    create_cluster
    wait_for_brokers
    create_all_topics
}

wait_for_brokers() {
    log_info "Waiting for brokers to start."
    while ! is_kafka_starting; do
    :
    done

    sleep 15 # Give the broker a moment to start taking connections
    log_ok "Broker has started."
}

create_all_topics() {
    log_info "Creating all topics."
    create_topic orders
    create_topic payments
    create_topic customers

    list_topics
}

create_cluster() {
    log_info "Starting kafka cluster."
    docker-compose up -d

    if [ $? -eq 0 ]; then
        log_ok "Kafka cluster started"
    else
        log_error "Kafka cluster could not be started"
        exit 1
    fi
}

start_demo