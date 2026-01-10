for i in {1..100}; do
  curl -s -o /dev/null -w "%{http_code}\n" http://localhost:8080/hello/siva &
done
wait
