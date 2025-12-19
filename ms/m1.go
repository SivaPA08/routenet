package main

import (
	"fmt"
	"net/http"
	"strings"
)

func health(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodGet {
		http.Error(w, "method not allowed", http.StatusMethodNotAllowed)
		return
	}
	fmt.Fprint(w, "OK")
}

func hello(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodGet {
		http.Error(w, "method not allowed", http.StatusMethodNotAllowed)
		return
	}

	// url: /hello/{name}
	path := strings.Trim(r.URL.Path, "/")
	parts := strings.Split(path, "/")

	if len(parts) != 2 || parts[0] != "hello" {
		http.NotFound(w, r)
		return
	}

	name := parts[1]
	fmt.Fprintf(w, "Hello %s", name)
}

func main() {
	http.HandleFunc("/health", health)

	// trailing slash enables prefix match
	http.HandleFunc("/hello/", hello)

	fmt.Println("listening on :5000")
	if err := http.ListenAndServe(":5000", nil); err != nil {
		panic(err)
	}
}
