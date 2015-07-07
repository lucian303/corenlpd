(ns corenlpd.test.parser
  (:use clojure.test
        ring.mock.request
        corenlpd.parser))

(deftest test-parser
  (testing "parse"
    (let [response (parse "This is a test." :parse-full)]
      (is (= "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<?xml-stylesheet href=\"CoreNLP-to-HTML.xsl\" type=\"text/xsl\"?>\r\n<root>\r\n  <document>\r\n    <sentences>\r\n      <sentence id=\"1\">\r\n        <tokens>\r\n          <token id=\"1\">\r\n            <word>This</word>\r\n            <lemma>this</lemma>\r\n            <CharacterOffsetBegin>0</CharacterOffsetBegin>\r\n            <CharacterOffsetEnd>4</CharacterOffsetEnd>\r\n            <POS>DT</POS>\r\n          </token>\r\n          <token id=\"2\">\r\n            <word>is</word>\r\n            <lemma>be</lemma>\r\n            <CharacterOffsetBegin>5</CharacterOffsetBegin>\r\n            <CharacterOffsetEnd>7</CharacterOffsetEnd>\r\n            <POS>VBZ</POS>\r\n          </token>\r\n          <token id=\"3\">\r\n            <word>a</word>\r\n            <lemma>a</lemma>\r\n            <CharacterOffsetBegin>8</CharacterOffsetBegin>\r\n            <CharacterOffsetEnd>9</CharacterOffsetEnd>\r\n            <POS>DT</POS>\r\n          </token>\r\n          <token id=\"4\">\r\n            <word>test</word>\r\n            <lemma>test</lemma>\r\n            <CharacterOffsetBegin>10</CharacterOffsetBegin>\r\n            <CharacterOffsetEnd>14</CharacterOffsetEnd>\r\n            <POS>NN</POS>\r\n          </token>\r\n          <token id=\"5\">\r\n            <word>.</word>\r\n            <lemma>.</lemma>\r\n            <CharacterOffsetBegin>14</CharacterOffsetBegin>\r\n            <CharacterOffsetEnd>15</CharacterOffsetEnd>\r\n            <POS>.</POS>\r\n          </token>\r\n        </tokens>\r\n        <parse>(ROOT (S (NP (DT This)) (VP (VBZ is) (NP (DT a) (NN test))) (. .))) </parse>\r\n        <dependencies type=\"basic-dependencies\">\r\n          <dep type=\"root\">\r\n            <governor idx=\"0\">ROOT</governor>\r\n            <dependent idx=\"4\">test</dependent>\r\n          </dep>\r\n          <dep type=\"nsubj\">\r\n            <governor idx=\"4\">test</governor>\r\n            <dependent idx=\"1\">This</dependent>\r\n          </dep>\r\n          <dep type=\"cop\">\r\n            <governor idx=\"4\">test</governor>\r\n            <dependent idx=\"2\">is</dependent>\r\n          </dep>\r\n          <dep type=\"det\">\r\n            <governor idx=\"4\">test</governor>\r\n            <dependent idx=\"3\">a</dependent>\r\n          </dep>\r\n        </dependencies>\r\n        <dependencies type=\"collapsed-dependencies\">\r\n          <dep type=\"root\">\r\n            <governor idx=\"0\">ROOT</governor>\r\n            <dependent idx=\"4\">test</dependent>\r\n          </dep>\r\n          <dep type=\"nsubj\">\r\n            <governor idx=\"4\">test</governor>\r\n            <dependent idx=\"1\">This</dependent>\r\n          </dep>\r\n          <dep type=\"cop\">\r\n            <governor idx=\"4\">test</governor>\r\n            <dependent idx=\"2\">is</dependent>\r\n          </dep>\r\n          <dep type=\"det\">\r\n            <governor idx=\"4\">test</governor>\r\n            <dependent idx=\"3\">a</dependent>\r\n          </dep>\r\n        </dependencies>\r\n        <dependencies type=\"collapsed-ccprocessed-dependencies\">\r\n          <dep type=\"root\">\r\n            <governor idx=\"0\">ROOT</governor>\r\n            <dependent idx=\"4\">test</dependent>\r\n          </dep>\r\n          <dep type=\"nsubj\">\r\n            <governor idx=\"4\">test</governor>\r\n            <dependent idx=\"1\">This</dependent>\r\n          </dep>\r\n          <dep type=\"cop\">\r\n            <governor idx=\"4\">test</governor>\r\n            <dependent idx=\"2\">is</dependent>\r\n          </dep>\r\n          <dep type=\"det\">\r\n            <governor idx=\"4\">test</governor>\r\n            <dependent idx=\"3\">a</dependent>\r\n          </dep>\r\n        </dependencies>\r\n      </sentence>\r\n    </sentences>\r\n  </document>\r\n</root>\r\n"
             response))))

  (testing "parse-with-pos"
    (let [response (parse-with-pos "Document/VB stakeholder/NN requirements/NNS regarding/VBG Information/NNP Security/NNP during/IN the/DT business/NN planning/NN process/NN ./. " :englishPCFG)]
      (is (= '{:wordsAndTags (["Document" "VB"] ["stakeholder" "NN"] ["requirements" "NNS"] ["regarding" "VBG"] ["Information" "NNP"] ["Security" "NNP"] ["during" "IN"] ["the" "DT"] ["business" "NN"] ["planning" "NN"] ["process" "NN"] ["." "."]), :typedDependencies ({:type "root", 0 {:feature "ROOT", :index 0}, 1 {:feature "Document", :index 1}} {:type "compound", 0 {:feature "requirements", :index 3}, 1 {:feature "stakeholder", :index 2}} {:type "dobj", 0 {:feature "Document", :index 1}, 1 {:feature "requirements", :index 3}} {:type "nmod", 0 {:feature "requirements", :index 3}, 1 {:feature "regarding", :index 4}} {:type "compound", 0 {:feature "Security", :index 6}, 1 {:feature "Information", :index 5}} {:type "dep", 0 {:feature "regarding", :index 4}, 1 {:feature "Security", :index 6}} {:type "nmod", 0 {:feature "Security", :index 6}, 1 {:feature "during", :index 7}})}
             response))))

  (testing "parse-with-pos"
    (let [response (parse-with-pos "Apply/VB asset/NN protection/NN mechanisms/NNS for/IN all/DT assets/NNS according/VBN to/TO their/PRP$ assigned/VBN Asset/NNP Classification/NN Policy/NN ./. " :englishPCFG)]
      (is (= '{:wordsAndTags (["Apply" "VB"] ["asset" "NN"] ["protection" "NN"] ["mechanisms" "NNS"] ["for" "IN"] ["all" "DT"] ["assets" "NNS"] ["according" "VBN"] ["to" "TO"] ["their" "PRP$"] ["assigned" "VBN"] ["Asset" "NNP"] ["Classification" "NN"] ["Policy" "NN"] ["." "."]), :typedDependencies ({:type "root", 0 {:feature "ROOT", :index 0}, 1 {:feature "Apply", :index 1}} {:type "compound", 0 {:feature "mechanisms", :index 4}, 1 {:feature "asset", :index 2}} {:type "compound", 0 {:feature "mechanisms", :index 4}, 1 {:feature "protection", :index 3}} {:type "dobj", 0 {:feature "Apply", :index 1}, 1 {:feature "mechanisms", :index 4}} {:type "nmod", 0 {:feature "mechanisms", :index 4}, 1 {:feature "for", :index 5}} {:type "det", 0 {:feature "assets", :index 7}, 1 {:feature "all", :index 6}} {:type "dep", 0 {:feature "for", :index 5}, 1 {:feature "assets", :index 7}} {:type "acl", 0 {:feature "assets", :index 7}, 1 {:feature "according", :index 8}} {:type "nmod", 0 {:feature "according", :index 8}, 1 {:feature "to", :index 9}})}
             response)))))
