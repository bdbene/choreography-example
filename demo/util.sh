#!/bin/bash

export RED='\033[0;31m'
export NC='\033[0m'
export YELLOW='\033[1;33m'
export GREEN='\033[0;32m'
export PURPLE='\033[0;35m'

log_info() {
    echo -e "${PURPLE}INFO:${NC}" $1
}

log_ok() {
    echo -e "${GREEN}OK:${NC}" $1
}

log_warn() {
    echo -e "${YELLOW}WARN:${NC}" $1
}

log_error() {
    echo -e "${RED}ERROR:${NC}" $1
}