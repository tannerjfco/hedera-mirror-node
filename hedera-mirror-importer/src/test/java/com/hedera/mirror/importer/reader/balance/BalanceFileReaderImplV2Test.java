package com.hedera.mirror.importer.reader.balance;

/*-
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

import java.io.File;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestConstructor;

import com.hedera.mirror.importer.domain.StreamFileData;
import com.hedera.mirror.importer.reader.balance.line.AccountBalanceLineParserV2;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class BalanceFileReaderImplV2Test extends CsvBalanceFileReaderTest {

    static final String FILENAME = "classpath:data/accountBalances/v2/2020-09-22T04_25_00.083212003Z_Balances.csv";

    BalanceFileReaderImplV2Test(BalanceFileReaderImplV2 balanceFileReader, AccountBalanceLineParserV2 parser,
                                @Value(FILENAME) File balanceFile) {
        super(balanceFileReader, parser, balanceFile, 106L);
    }

    @Test
    void supportsInvalidWhenWrongVersion() {
        StreamFileData streamFileData = StreamFileData
                .from(balanceFile.getName(), BalanceFileReaderImplV1.TIMESTAMP_HEADER_PREFIX);
        assertThat(balanceFileReader.supports(streamFileData)).isFalse();
    }
}
