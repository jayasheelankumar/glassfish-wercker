/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2010-2013 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package org.glassfish.concurrent.runtime;

import org.glassfish.enterprise.concurrent.spi.TransactionHandle;
import org.glassfish.enterprise.concurrent.spi.TransactionSetupProvider;

import javax.enterprise.concurrent.ManagedTask;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.InvalidTransactionException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

public class TransactionSetupProviderImpl implements TransactionSetupProvider {

    @Override
    public TransactionHandle beforeProxyMethod(String transactionExecutionProperty) {
        // suspend current transaction if not using transaction of execution thread
        if (! ManagedTask.USE_TRANSACTION_OF_EXECUTION_THREAD.equals(transactionExecutionProperty)) {
            try {
                Context ctx = new InitialContext();
                TransactionManager tm = (TransactionManager) ctx.lookup("java:comp/TransactionManager");
                Transaction suspendedTxn = tm.suspend();
                return new TransactionHandleImpl(suspendedTxn);
            } catch (NamingException e) {

            } catch (SystemException e) {

            }
        }
        return null;
    }

    @Override
    public void afterProxyMethod(TransactionHandle handle, String transactionExecutionProperty) {
        // resume transaction if any
        if (handle instanceof TransactionHandleImpl) {
            Transaction suspendedTxn = ((TransactionHandleImpl)handle).getTransaction();
            if (suspendedTxn != null) {
                try {
                    Context ctx = new InitialContext();
                    TransactionManager tm = (TransactionManager) ctx.lookup("java:comp/TransactionManager");
                    tm.resume(suspendedTxn);
                } catch (NamingException e) {

                } catch (InvalidTransactionException e) {

                } catch (SystemException e) {

                }
            }
        }
    }
}
