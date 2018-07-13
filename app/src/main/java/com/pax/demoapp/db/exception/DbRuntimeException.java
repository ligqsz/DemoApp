/*
 * ============================================================================
 * COPYRIGHT
 *              Pax CORPORATION PROPRIETARY INFORMATION
 *   This software is supplied under the terms of a license agreement or
 *   nondisclosure agreement with Pax Corporation and may not be copied
 *   or disclosed except in accordance with the terms in that agreement.
 *      Copyright (C) 2017 - ? Pax Corporation. All rights reserved.
 * Module Date: 2018-5-7
 * Module Author: ligq
 * Description:
 *
 * ============================================================================
 */
package com.pax.demoapp.db.exception;

/**
 * @author ligq
 * @date 2018/5/7
 */

public class DbRuntimeException extends RuntimeException {
    public DbRuntimeException(Exception e) {
        super(e);
    }
}
