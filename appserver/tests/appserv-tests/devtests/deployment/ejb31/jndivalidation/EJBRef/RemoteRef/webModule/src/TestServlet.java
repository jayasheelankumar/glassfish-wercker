/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2017 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://oss.oracle.com/licenses/CDDL+GPL-1.1
 * or LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at LICENSE.txt.
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

package test.web;

import java.io.IOException;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import test.ejb.Sless;

@WebServlet(name = "TestServlet", urlPatterns = {"/TestServlet"})
public class TestServlet extends HttpServlet {
    // Portable JNDI names
    // Testing mapped name using java:global and java:app JNDI names
    @EJB(mappedName="java:global/deployment-ejb31-jndivalidation-EJBRef-RemoteRef-app/deployment-ejb31-jndivalidation-EJBRef-RemoteRef-ejb/SlessEJB")
    Sless ejb1;

    @EJB(mappedName="java:app/deployment-ejb31-jndivalidation-EJBRef-RemoteRef-ejb/SlessEJB")
    Sless ejb2;

    @EJB(mappedName="java:global/deployment-ejb31-jndivalidation-EJBRef-RemoteRef-app/deployment-ejb31-jndivalidation-EJBRef-RemoteRef-ejb/SlessEJB!test.ejb.Sless")
    Sless ejb3;

    @EJB(mappedName="java:app/deployment-ejb31-jndivalidation-EJBRef-RemoteRef-ejb/SlessEJB!test.ejb.Sless")
    Sless ejb4;

    // Non-portable jndi names
    // This should work since we have only one remote business interface
    @EJB(lookup="sless_ejb")
    Sless ejb5;

    @EJB(lookup="sless_ejb#test.ejb.Sless")
    Sless ejb6;

    // Auto-linking
    @EJB
    Sless ejb7;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }
}
