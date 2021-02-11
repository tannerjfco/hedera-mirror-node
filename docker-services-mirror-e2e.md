## How to run mirrornode with local services node

- clone this `docker-services-mirror-e2e` branch of [tannerjfco/hedera-mirror-node](https://github.com/tannerjfco/hedera-mirror-node) to a local directory such as ~/hashgraph (this will be the path used through this guide)
- clone this `docker-services-mirror-e2e` branch of [tannerjfco/hedera-services](https://github.com/tannerjfco/hedera-services) to the same local directory as mirror node

directory structure should be like the following

```
» ls ~/hashgraph
hedera-services                  hedera-mirror-node
```

- cd to ~/hashgraph/hedera-services directory
- run `docker-compose up -d` to start the hedera services node and its postgresql containers

```
» docker-compose up -d
Starting node_0          ... done
Starting hedera-postgres ... done
```

- run `docker-compose logs -f` to follow hedera services node logs. once the logs output the following messages, the network is online and ready to receive transactions

```
node_0      | 2021-02-11 22:03:47.304 INFO  110  ServicesMain - Now current platform status = ACTIVE in HederaNode#0.
node_0      | 2021-02-11 22:03:47.306 INFO  212  RecordStreamManager - RecordStream inFreeze is set to be false
```

- hit ctrl+C to exit `docker-compose logs -f`

- cd to ~/hashgraph/hedera-mirror-node directory

- run `docker-compose -f docker-compose.yml -f docker-compose.minio.yml up -d` to start minio server and hedera-mirror-node components


### Notes

- This setup is built to operate with only a single hedera services node. In order to run this setup with any other number of services nodes, the following must be updated
  - in hedera-services/hedera-node/configuration/compose/, `config.txt` must be updated as documented within config.txt to reflect the new nodes. e.g., to run 2 nodes, uncomment the line `#address, B, Bob, 1, node_1, 50204, node_1, 50204, 0.0.4`
  - the keys in hedera-services/hedera-node/configuration/compose/keys must be replaced with new keys generated using the [generate.sh](https://github.com/hashgraph/hedera-services/blob/master/hedera-node/data/keys/generate.sh) script, providing it the names of each node to run in the network e.g. `./generate.sh Alice Bob`
  - the initial address book file must be replaced with a new binary file generated from the new config.txt and the new public.pfx files
- This setup does not yet configure the rosetta component properly, it will generate an exit code 1 due to being unable to connect
- This setup does not yet configure a monitor component to provide sustained traffic to the network or monitor overall mirror node health
- As suggests above this setup depends on pregenerated keys for hedera services and pregenerated initial address book file for hedera mirror importer. The generator for hedera services keys is already publicly available, but the script to generate the initial address book binary file is not yet published. This will need to be published along with usage instructions in order for this setup to be provided in a manner that does not include pre-generated keys and binary artifacts
- This setup modifies existing docker-compose configurations provided in both hedera-services and hedera-mirror-node repositories. It will need to be decided how to package this for public consumption, whether by updating the existing implementations or providing an alternate configuration set for the purpose of operationg a full end-to-end stack
