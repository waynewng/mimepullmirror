/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2012 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * http://glassfish.java.net/public/CDDL+GPL_1_1.html
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

/* FROM mail.jar */
package org.jvnet.mimepull;

import java.util.*;

/**
 * Utilities to make it easier to get property values.
 * Properties can be strings or type-specific value objects.
 *
 * @author Bill Shannon
 */
final class PropUtil {

    // No one should instantiate this class.
    private PropUtil() {
    }

    /**
     * Get a boolean valued System property.
     */
    public static boolean getBooleanSystemProperty(String name, boolean def) {
	try {
	    return getBoolean(getProp(System.getProperties(), name), def);
	} catch (SecurityException sex) {
	    // fall through...
	}

	/*
	 * If we can't get the entire System Properties object because
	 * of a SecurityException, just ask for the specific property.
	 */
	try {
	    String value = System.getProperty(name);
	    if (value == null) {
                return def;
            }
	    if (def) {
                return !value.equalsIgnoreCase("false");
            } else {
                return value.equalsIgnoreCase("true");
            }
	} catch (SecurityException sex) {
	    return def;
	}
    }

    /**
     * Get the value of the specified property.
     * If the "get" method returns null, use the getProperty method,
     * which might cascade to a default Properties object.
     */
    private static Object getProp(Properties props, String name) {
	Object val = props.get(name);
	if (val != null) {
            return val;
        } else {
            return props.getProperty(name);
        }
    }

    /**
     * Interpret the value object as a boolean,
     * returning def if unable.
     */
    private static boolean getBoolean(Object value, boolean def) {
	if (value == null) {
            return def;
        }
	if (value instanceof String) {
	    /*
	     * If the default is true, only "false" turns it off.
	     * If the default is false, only "true" turns it on.
	     */
	    if (def) {
                return !((String)value).equalsIgnoreCase("false");
            } else {
                return ((String)value).equalsIgnoreCase("true");
            }
	}
	if (value instanceof Boolean) {
            return ((Boolean)value).booleanValue();
        }
	return def;
    }
}
