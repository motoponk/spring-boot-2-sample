#!/usr/bin/env bash


function get_all_users() {
    echo -e "\n========== get_all_users ============\n"
    curl -v http://localhost:18080/api/users
}

function get_user_by_id() {
    echo -e "\n========== get_user_by_id ============\n"
    curl -v http://localhost:18080/api/users/$RANDOM
}

function create_user() {
    echo -e "\n========== create_user ============\n"

    data="{\"name\": \"name-$RANDOM\", \"email\": \"name-$RANDOM@gmail.com\", \"githubUsername\": \"ghuser-$RANDOM\"}"
    curl -v --header "Content-Type: application/json" --request POST --data "$data" http://localhost:18080/api/users
}

function delete_user_by_id() {
    echo -e "\n========== delete_user_by_id ============\n"
    curl -v -X DELETE http://localhost:18080/api/users/$RANDOM
}

while true
do
    n=$(( ( RANDOM % 3 )  + 1 ))
    # echo "n=$n"

    if [ "$n" -eq "1" ]; then
        create_user
    elif [ "$n" -eq "2" ]; then
        delete_user_by_id
    elif [ "$n" -eq "3" ]; then
        get_user_by_id
    else
        get_all_users
    fi

    sleep 1s
done
