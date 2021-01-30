Demo project using Micronaut elasticsearch
https://micronaut-projects.github.io/micronaut-elasticsearch/latest/guide/index.html

## Micronaut 2.3.0 Documentation

- [User Guide](https://docs.micronaut.io/2.3.0/guide/index.html)
- [API Reference](https://docs.micronaut.io/2.3.0/api/index.html)
- [Configuration Reference](https://docs.micronaut.io/2.3.0/guide/configurationreference.html)
- [Micronaut Guides](https://guides.micronaut.io/index.html)
---

## Feature http-client documentation

- [Micronaut HTTP Client documentation](https://docs.micronaut.io/latest/guide/index.html#httpClient)

## Dependencies:
- Elasticsearch (tested with Elastic 7.10.2)

To start, run Application.main() method

It will create two endpoints:

- GET http://localhost:8080/twitter <-- create index if necessary, put mappings and index 1 doc
  - CURL command:  curl "http://localhost:8080/twitter"
- DELETE http://localhost:8080/twitter <-- delete the index 
  - CURL command:  curl -XDELETE "http://localhost:8080/twitter"




