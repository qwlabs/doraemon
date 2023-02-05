[![Maven Central](https://img.shields.io/maven-central/v/com.qwlabs.doraemon/lang.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.qwlabs.doraemon%22)
[![Commit](https://github.com/qwlabs/doraemon/actions/workflows/publication.yml/badge.svg?branch=master)](https://github.com/qwlabs/doraemon/actions/workflows/publication.yml)
[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)

Useful java tools

# Installation

## Maven

```xml

<dependency>
    <groupId>com.qwlabs.doraemon</groupId>
    <artifactId>lang</artifactId>
    <version>0.2.*</version>
    <type>pom</type>
</dependency>
```

## Gradle

```gradle
api 'com.qwlabs.doraemon:lang:0.2.*'
```

# Document
- [auditing](auditing/README.md) Base auditing entity/listener for jpa  
- [basic-tree](basic-tree/README.md) Basic Tree definition and operation
- [cdi](cdi/README.md) CDI Utils and Dispatchable definition for your business code
- [exceptions](exceptions/README.md) Generic exception definition
- [feature-flags](feature-flags/README.md) Help service easy use feature in CDI
- [graphql](graphql/README.md) Definition Relay for Graphql
- [graphql-builder](graphql-builder/README.md) Easy build Graphql for testing with java flow code style
- [jackson](jackson/README.md) Easy to use jackson
- [jpa](jpa/README.md) Generic jpa naming strategy as refer to spring
- [lang](lang/README.md) Generic utils for java
- [mptt-tree](mptt-tree/README.md) The tree use MPTT structure
- [panache](panache/README.md) Extends panache in paging and native query
- [q-api](q-api/README.md) Query string condition api definition
- [q-parser-antlr4](q-parser-antlr4/README.md) Query string condition parser with antlr4 
- [quarkus-cache](quarkus-cache/README.md) Help for quarkus cache
- [security](security/README.md) Define the caller concept in the application and provide the ability to load properties api
- [storage](storage/README.md) Define storage api
- [storage-minio](storage-minio/README.md) Storage api implements use minio
- [storage-s3](storage-s3/README.md) Storage api implements use s3
- [task-queue](task-queue/README.md) Define task queue for loop job
- [test-builder](test-builder/README.md) Define some builder for testing
- [validator](validator/README.md) Generic validator

