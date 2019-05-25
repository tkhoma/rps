if [ -z "$1" ]
  then
    echo "No argument supplied"
    exit 1
  fi

curl -i -X GET http://localhost:8080/games/$1
