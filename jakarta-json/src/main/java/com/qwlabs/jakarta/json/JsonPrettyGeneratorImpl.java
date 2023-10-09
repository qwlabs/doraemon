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
import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.Charset;

/**
 * @author Jitendra Kotamraju
 */
public class JsonPrettyGeneratorImpl extends JsonGeneratorImpl {

    private int indentLevel;
    private static final String INDENT = "    ";

    public JsonPrettyGeneratorImpl(Writer writer, JsonContext jsonContext) {
        super(writer, jsonContext);
    }

    public JsonPrettyGeneratorImpl(OutputStream out, JsonContext jsonContext) {
        super(out, jsonContext);
    }

    public JsonPrettyGeneratorImpl(OutputStream out, Charset encoding, JsonContext jsonContext) {
        super(out, encoding, jsonContext);
    }

    @Override
    public JsonGenerator writeStartObject() {
        super.writeStartObject();
        indentLevel++;
        return this;
    }

    @Override
    public JsonGenerator writeStartObject(String name) {
        super.writeStartObject(name);
        indentLevel++;
        return this;
    }

    @Override
    public JsonGenerator writeStartArray() {
        super.writeStartArray();
        indentLevel++;
        return this;
    }

    @Override
    public JsonGenerator writeStartArray(String name) {
        super.writeStartArray(name);
        indentLevel++;
        return this;
    }

    @Override
    public JsonGenerator writeEnd() {
        writeNewLine();
        indentLevel--;
        writeIndent();
        super.writeEnd();
        return this;
    }

    private void writeIndent() {
        for(int i=0; i < indentLevel; i++) {
            writeString(INDENT);
        }
    }

    @Override
    protected void writeComma() {
        super.writeComma();
        if (isCommaAllowed() && !inNone()) {
            writeChar('\n');
            writeIndent();
        }
    }

    @Override
    protected void writeColon() {
        super.writeColon();
        writeChar(' ');
    }

    private void writeNewLine() {
        writeChar('\n');
    }
}
