/*
 * Copyright 2019 Web3 Labs LTD.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.web3j.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.web3j.crypto.Hash;
import org.web3j.rlp.RlpEncoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;

public class PrivacyGroupUtils {
    public static String generateLegacyGroup(
            final String privateFrom, final List<String> privateFor) {
        final List<byte[]> stringList = new ArrayList<>();
        stringList.add(Base64.getDecoder().decode(privateFrom));
        privateFor.forEach(item -> stringList.add(Base64.getDecoder().decode(item)));

        final List<RlpType> rlpList =
                stringList.stream()
                        .distinct()
                        .sorted(Comparator.comparing(Arrays::hashCode))
                        .map(RlpString::create)
                        .collect(Collectors.toList());

        return Numeric.byteArrayToBase64(Hash.sha3(RlpEncoder.encode(new RlpList(rlpList))));
    }
}
