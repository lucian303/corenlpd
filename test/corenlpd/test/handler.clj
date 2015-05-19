(ns corenlpd.test.handler
  (:use clojure.test
        ring.mock.request
        corenlpd.handler))

(deftest test-app
  (testing "main nlp route"
    (let [response (app (request :get "/parse?text=This+is+a+test."))]
      (is (= 200 (:status response)))))

  (testing "not-found route"
    (let [response (app (request :get "/invalid"))]
      (is (= 404 (:status response))))))
