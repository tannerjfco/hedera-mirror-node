package com.hedera.mirror.importer.parser.record.transactionhandler;

/*
 * ‌
 * Hedera Mirror Node
 * ​
 * Copyright (C) 2019 - 2021 Hedera Hashgraph, LLC
 * ​
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ‍
 */

import static org.assertj.core.api.Assertions.assertThat;

import com.google.protobuf.ByteString;
import com.hederahashgraph.api.proto.java.Duration;
import com.hederahashgraph.api.proto.java.Key;
import com.hederahashgraph.api.proto.java.SignedTransaction;
import com.hederahashgraph.api.proto.java.Timestamp;
import com.hederahashgraph.api.proto.java.Transaction;
import com.hederahashgraph.api.proto.java.TransactionRecord;
import java.time.Instant;
import org.junit.jupiter.api.Test;

import com.hedera.mirror.importer.domain.Entities;
import com.hedera.mirror.importer.parser.domain.RecordItem;
import com.hedera.mirror.importer.util.Utility;

abstract class AbstractUpdatesEntityTransactionHandlerTest extends AbstractTransactionHandlerTest {

    protected static final Duration DEFAULT_AUTO_RENEW_PERIOD = Duration.newBuilder().setSeconds(1).build();

    protected static final Timestamp DEFAULT_EXPIRATION_TIME = Utility.instantToTimestamp(Instant.now());

    protected static final Key DEFAULT_KEY = getKey("4a5ad514f0957fa170a676210c9bdbddf3bc9519702cf915fa6767a40463b96f");

    protected static final String DEFAULT_MEMO = "updatesEntityTransactionHandlerMemo";

    protected static final Key DEFAULT_SUBMIT_KEY = getKey(
            "5a5ad514f0957fa170a676210c9bdbddf3bc9519702cf915fa6767a40463b96G");

    @Test
    void updateEntity() {
        //given
        Entities entity = new Entities();
        RecordItem recordItem = getUpdateEntityRecordItem();
        Entities expectedEntity = getUpdateEntityExpectedEntity();

        //when
        getTransactionHandler().updateEntity(entity, recordItem);

        //then
        assertThat(entity).isEqualTo(expectedEntity);
    }

    abstract ByteString getUpdateEntityTransactionBody();

    abstract void buildUpdateEntityExpectedEntity(Entities entity);

    private RecordItem getUpdateEntityRecordItem() {
        Transaction transaction = Transaction.newBuilder()
                .setSignedTransactionBytes(
                        SignedTransaction.newBuilder()
                                .setBodyBytes(getUpdateEntityTransactionBody())
                                .build().toByteString())
                .build();

        return new RecordItem(transaction, TransactionRecord.newBuilder().build());
    }

    private Entities getUpdateEntityExpectedEntity() {
        Entities entity = new Entities();
        buildUpdateEntityExpectedEntity(entity);
        return entity;
    }

    protected static Key getKey(String keyString) {
        return Key.newBuilder().setEd25519(ByteString.copyFromUtf8(keyString)).build();
    }
}
