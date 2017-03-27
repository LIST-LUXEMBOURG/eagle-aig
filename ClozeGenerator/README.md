##Documentation

Cloze - Noun: Parses the text. Picks all nouns. Generates distractors using DISCO. Computes the distractor quality by using similarity approaches. Discards distractors with a similarity less than 50%. If more then 3 distractors are left, a key-distractor pair is generated.


Cloze - Annotation: Parses the text. Uses the words picked by KODA. Generates distractors using DISCO. Computes the distractor quality by using similarity approaches. Discards distractors with a similarity less than 50%. If more then 3 distractors are left, a key-distractor pair is generated.


Cloze - Tense: Parses the text. Picks all verbs. Generates distractors using the verbGenerator webservices which is based on the SimpleNLG-DE and SimpleNLG-EnFr libaries. Computes the distractor quality by using similarity approaches. Discards distractors with a similarity less than 50%. If more then 3 distractors are left, a key-distractor pair is generated.


Cloze - C-Test: Cuts each second word by half its length.


Open Questions: Parses the text. Searches for connectives and generates a questions with this sentence.


MCQ - Definition: Parses the html wiki page. Picks all words with a "bold" fond. Distractors are other bold words in the article. Computes the distractor quality by using similarity approaches. Discards distractors with a similarity less than 50%. If more then 3 distractors are left, a key-distractor pair is generated.



##How to run:

Use the jar compiled with dependencies.

Following environment variable have to be set:

- DISCO\_DE\_DIR (example: "../resources/DISCO/de-general-20150421-lm-word2vec-sim")

- DISCO\_EN\_DIR (example: "../resources/DISCO/enwiki-20130403-word2vec-lm-mwl-lc-sim")

- DISCO\_FR\_DIR (example: "../resources/DISCO/fr-general-20151126-lm-word2vec-sim")

At least 4GB of allocated memory is needed (please use JVM parameter: "-Xmx4G")


