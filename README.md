# TGraph Benchmark

## Intro
This project is used for [TGraph](https://github.com/CrusherAlpha/tgraph) benchmark.
TGraph is a temporal property graph OLTP database which optimized for property evolution.
In this project, we compare TGraph with [Neo4j](https://github.com/neo4j/neo4j) (Property Graph Model) and [Postgres](https://github.com/postgres/postgres)
(Relation Model).


## Basic Idea
We process a request in a transaction, thus request and transaction is one-to-one map.
we evaluate system performance through latency, throughput and disk space occupation.
latency: the max value of 90%, 95%, 99% transactions finish.
throughput: the number of transactions finish per second.

Transactions include write only and read only transactions.
### write only transaction
1. import static data(graph structure and static entity property)
2. import temporal data(temporal entity property)
3. data update(include graph structure and property, note that one of our assumptions is that graph structure is not change frequently)
### read only transaction
1. entity time point temporal property query
2. entity multiple degrees temporal property time point query
3. entity time range temporal property aggregation query
4. vertex's all neighbor edges temporal property time point aggregation query

Benchmark include micro benchmark and macro benchmark. For micro benchmark, we
compare specific write and read performance

## Usage 
You can configure the system through yaml file lie in src/resource. Once you finished
the configuration, the system will automatically run you configuration and write the
result into the directory you configured.

## Dataset
[BJ-traffic](https://paperswithcode.com/dataset/taxibj), [EU-electricity](https://www.nature.com/articles/sdata2017175), [Generation](https://github.com/CrusherAlpha/temporal_graph_generator)