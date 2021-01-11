package com.hedera.mirror.importer.reader.signature;

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

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.inject.Named;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Primary;

import com.hedera.mirror.importer.domain.FileStreamSignature;
import com.hedera.mirror.importer.exception.SignatureFileParsingException;

@Log4j2
@Named
@Primary
@RequiredArgsConstructor
public class CompositeSignatureFileReader implements SignatureFileReader {

    private final SignatureFileReaderV2 signatureFileReaderV2;

    @Override
    public FileStreamSignature read(InputStream inputStream) {

        try (DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(inputStream))) {
            dataInputStream.mark(Byte.BYTES);
            byte version = dataInputStream.readByte();
            dataInputStream.reset();
            SignatureFileReader fileReader;
            // Version 2 of the signature file begins with a byte of value 4.
            if (version <= SignatureFileReaderV2.SIGNATURE_TYPE_FILE_HASH) {
                fileReader = signatureFileReaderV2;
            } else {
                throw new SignatureFileParsingException("Unsupported signature file version: " + version);
            }
            return fileReader.read(dataInputStream);
        } catch (IOException ex) {
            throw new SignatureFileParsingException("Error reading signature file", ex);
        }
    }
}