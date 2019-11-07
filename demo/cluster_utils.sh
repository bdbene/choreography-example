#!/bin/bash

is_kafka_starting() {
    BROKER_LIST=$(${DOCKER_CMD} 'zookeeper' ${ZK_SHELL_CMD} ls /brokers/ids | tail -n 1)
    echo $BROKER_LIST
    
    # List of brokers will look like "[]" when the broker is still starting up.
    # Otherwise it will list the broker IDs, only one in this case.
    if [ ${#BROKER_LIST} = '[1]' ] 
    then
        false
    else
        true
    fi
}