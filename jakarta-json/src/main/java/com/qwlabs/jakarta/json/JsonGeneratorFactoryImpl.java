/*
 * Copyright (c) 2012, 2023 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package com.qwlabs.jakarta.json;

import jakarta.json.stream.JsonGenerator;
import jakarta.json.stream.JsonGeneratorFactory;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * @author Jitendra Kotamraju
 */
class JsonGeneratorFactoryImpl implements JsonGeneratorFactory {

    private final JsonContext jsonContext;

    JsonGeneratorFactoryImpl(JsonContext jsonContext) {
        this.jsonContext = jsonContext;
    }

    @Override
    public JsonGenerator createGenerator(Writer writer) {
        return jsonContext.prettyPrinting()
                ? new JsonPrettyGeneratorImpl(writer, jsonContext)
                : new JsonGeneratorImpl(writer, jsonContext);
    }

    @Override
    public JsonGenerator createGenerator(OutputStream out) {
        return jsonContext.prettyPrinting()
                ? new JsonPrettyGeneratorImpl(out, jsonContext)
                : new JsonGeneratorImpl(out, jsonContext);
    }

    @Override
    public JsonGenerator createGenerator(OutputStream out, Charset charset) {
        return jsonContext.prettyPrinting()
                ? new JsonPrettyGeneratorImpl(out, charset, jsonContext)
                : new JsonGeneratorImpl(out, charset, jsonContext);
    }

    @Override
    public Map<String, ?> getConfigInUse() {
        return jsonContext.config();
    }

}
