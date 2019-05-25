if [ $# -ne 2 ]
  then
    echo "Wrong parameters size. Should be #game and #move(rock, paper, scissors)"
    exit 1
  fi

curl -i -H "Content-Type: application/json" -X PUT -d "$2" http://localhost:8080/games/$1
