# elasticsearch-edgengram2-token-filter
Yet another a edge n gram token filter.

Fow example, it would map "EVA EGG京" to an array [E, EV, EVA, E, EG, EGG, 京].

The difference between edge_ngram and edge_ngram2 is that edge_ngram2 can offer the offsets to highlight partial word.

## How to use
1. git clone git@github.com:ralgond/elasticsearch-edgengram2-token-filter.git
2. cd elasticsearch-edgengram2-token-filter
3. mvn clean package
4. copy target/releases/elasticsearch-edgengram2-token-filter-7.2.1.zip to the plugins directory of elasticsearch.
5. upzip elasticsearch-edgengram2-token-filter-7.2.1.zip to directory elasticsearch-edgengram2-token-filter-7.2.1
6. restart elasticsearch
7. write some code in kibana as follow:
```
DELETE idx-15
PUT idx-15
{
  "settings": {
    "analysis": {
      "analyzer": {
        "my_analyzer": {
          "tokenizer": "standard",
          "filter": [
            "lowercase",
            "my_stop",
            "edge_ngram2"
          ]
        }
      },
      "filter": {
        "my_stop": {
          "type": "stop",
          "stopwords": "_english_"
        }
      }
    }
  },
  "mappings": {
    "properties": {
      "title": {
        "type": "text",
        "analyzer": "my_analyzer",
        "search_analyzer": "standard"
      }
    }
  }
}

GET idx-15/_search
{
  "query": {
    "match": {
      "title": {
        "query":  "EV"
      }
    }
  },
  "highlight": {
    "fields": {
      "title": {}
    }
  }
}
```
