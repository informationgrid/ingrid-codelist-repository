/*-
 * **************************************************-
 * InGrid CodeList Repository
 * ==================================================
 * Copyright (C) 2014 - 2025 wemove digital solutions GmbH
 * ==================================================
 * Licensed under the EUPL, Version 1.2 or – as soon they will be
 * approved by the European Commission - subsequent versions of the
 * EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 * https://joinup.ec.europa.eu/software/page/eupl
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 * **************************************************#
 */
package de.ingrid.codelistHandler.importer.bawOrderInfo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public record OrderInfoCodelistModel(List<OrderInfoItem> items) {

    /**
     * Delegate JSON array directly into the items list.
     */
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public OrderInfoCodelistModel(List<OrderInfoItem> items) {
        // When using DELEGATING mode with a single parameter of type List, the root array
        // is mapped into this parameter even without a property name. Keep property for clarity.
        this.items = items != null ? items : new ArrayList<>();
    }
}
