/*
 * Copyright (C) 2005-2008 Alfresco Software Limited.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.

 * As a special exception to the terms and conditions of version 2.0 of
 * the GPL, you may redistribute this Program in connection with Free/Libre
 * and Open Source Software ("FLOSS") applications as described in Alfresco's
 * FLOSS exception.  You should have recieved a copy of the text describing
 * the FLOSS exception, and it is also available here:
 * http://www.alfresco.com/legal/licensing"
 */
package org.alfresco.repo.transaction;

import org.alfresco.repo.transaction.RetryingTransactionHelper.RetryingTransactionCallback;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * A this advice wrapper around the {@link RetryingTransactionHelper}.
 *
 * @author Derek Hulley
 */
public class RetryingTransactionAdvice implements MethodInterceptor
{
    private RetryingTransactionHelper txnHelper;
    private boolean readOnly;
    private boolean requiresNew;

    public RetryingTransactionAdvice()
    {
        readOnly = false;
        requiresNew = false;
    }

    public void setTxnHelper(RetryingTransactionHelper txnHelper)
    {
        this.txnHelper = txnHelper;
    }

    public void setReadOnly(boolean readOnly)
    {
        this.readOnly = readOnly;
    }

    public void setRequiresNew(boolean requiresNew)
    {
        this.requiresNew = requiresNew;
    }

    public Object invoke(final MethodInvocation methodInvocation) throws Throwable
    {
        // Just call the helper
        RetryingTransactionCallback<Object> txnCallback = new RetryingTransactionCallback<Object>()
        {
            public Object execute() throws Throwable
            {
                return methodInvocation.proceed();
            }
        };
        return txnHelper.doInTransaction(txnCallback, readOnly, requiresNew);
    }
}
