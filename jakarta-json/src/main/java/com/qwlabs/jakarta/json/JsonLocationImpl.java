/*
 * Copyright (c) 2013, 2021 Oracle and/or its affiliates. All rights reserved.
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

import jakarta.json.stream.JsonLocation;

/**
 * @author Jitendra Kotamraju
 */
class JsonLocationImpl implements JsonLocation {
    static final JsonLocation UNKNOWN = new JsonLocationImpl(-1, -1, -1);

    private final long columnNo;
    private final long lineNo;
    private final long offset;

    JsonLocationImpl(long lineNo, long columnNo, long streamOffset) {
        this.lineNo = lineNo;
        this.columnNo = columnNo;
        this.offset = streamOffset;
    }

    @Override
    public long getLineNumber() {
        return lineNo;
    }

    @Override
    public long getColumnNumber() {
        return columnNo;
    }

    @Override
    public long getStreamOffset() {
        return offset;
    }

    @Override
    public String toString() {
        return "(line no="+lineNo+", column no="+columnNo+", offset="+ offset +")";
    }

}
