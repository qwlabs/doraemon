# gen
```bash
curl -O https://www.antlr.org/download/antlr-4.7.1-complete.jar

antlr4 -o ~/workspace/doraemon/q-parser-antlr4/src/main/java/com/qwlabs/q/parsers/antlr4/g4 -package com.qwlabs.q.parsers.antlr4.g4 -Dlanguage=Java -listener -encoding UTF-8 -visitor -lib ~/workspace/doraemon/q-parser-antlr4/src/main/java/com/qwlabs/q/parsers/antlr4/g4 ~/workspace/doraemon/q-parser-antlr4/src/main/java/com/qwlabs/q/parsers/antlr4/g4/QueryExpression.g4

```
