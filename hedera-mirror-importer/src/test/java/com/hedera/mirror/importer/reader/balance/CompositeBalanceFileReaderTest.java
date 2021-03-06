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

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.function.Consumer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hedera.mirror.importer.domain.AccountBalance;
import com.hedera.mirror.importer.domain.StreamFileData;

@ExtendWith(MockitoExtension.class)
public class CompositeBalanceFileReaderTest {

    @Mock
    private BalanceFileReaderImplV1 readerImplV1;

    @Mock
    private BalanceFileReaderImplV2 readerImplV2;

    @InjectMocks
    private CompositeBalanceFileReader compositeBalanceFileReader;

    private final Consumer<AccountBalance> consumer = accountBalance -> {
    };

    @Test
    void defaultsToVersion1Reader() {
        StreamFileData streamFileData = StreamFileData.from("foo.csv", "timestamp:1");
        when(readerImplV2.supports(streamFileData)).thenReturn(false);
        compositeBalanceFileReader.read(streamFileData, consumer);
        verify(readerImplV1, times(1)).read(streamFileData, consumer);
        verify(readerImplV2, times(0)).read(streamFileData, consumer);
    }

    @Test
    void usesVersion2Reader() {
        StreamFileData streamFileData = StreamFileData.from("foo.csv", "# version:2");
        when(readerImplV2.supports(streamFileData)).thenReturn(true);
        compositeBalanceFileReader.read(streamFileData, consumer);
        verify(readerImplV2, times(1)).read(streamFileData, consumer);
        verify(readerImplV1, times(0)).read(streamFileData, consumer);
    }
}
