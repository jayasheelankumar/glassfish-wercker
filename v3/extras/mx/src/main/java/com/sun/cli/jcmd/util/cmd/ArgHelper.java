/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2003-2010 Sun Microsystems, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 *
 * Contributor(s):
 *
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

/*
 * $Header: /m/jws/jmxcmd/src/com/sun/cli/jcmd/util/cmd/ArgHelper.java,v 1.5 2005/11/08 22:39:18 llc Exp $
 * $Revision: 1.5 $
 * $Date: 2005/11/08 22:39:18 $
 */
 
package com.sun.cli.jcmd.util.cmd;


/**
	Interface which implements parsing of a command-line.
 */
public interface ArgHelper
{
	/**
		The special token that means the end of options has been reached
	 */
	public static final String		OPTION_PREFIX		=  OptionInfo.OPTION_PREFIX;
	public static final String		SHORT_OPTION_PREFIX	=  OptionInfo.SHORT_OPTION_PREFIX;
	public static final String		LONG_OPTION_PREFIX	=  OptionInfo.LONG_OPTION_PREFIX;
	
	/**
		The special token that means the end of options has been reached
	 */
	public static final String		END_OPTIONS	=  LONG_OPTION_PREFIX;
	
	/**
		The delimiter when multiple values are specified for long options
		eg "--stuff=1,2,3" (3 values)
	 */
	public static final char	 	MULTI_VALUE_DELIM	= ',';
	
	/**
		The escape character when a literal MULTI_VALUE_DELIM is needed within
		a value
	 */
	public static final char	 	ESCAPE_CHAR	= '\\';
	
	
	/**
		Return a list of all required options that are missing.
		
		@return		array of missing options (empty if none missing)
	 */
	public OptionInfo []		getMissingOptions( );
	
	
	/**
		Check all meta-requirements such as missing options, conflicting options, etc
		
		Throw an IllegalOptionException if there is a problem.
	 */
	public void			checkRequirements( ) throws IllegalOptionException;
	
	
	/**
		Return the value string associated with the &lt;i>last instance of</i> the option (if any).
		
		@param 		name	name of the option
		@return			the String associated with the option, or null if not found
		@throws		IllegalOptionException if the option doesn't have exactly one value
	 */
	public String		getOptionValue( String name )
							throws IllegalOptionException;
							
	/**
		Return the value string(s) associated with the &lt;i>last instance of</i> the option (if any).
		
		Note that multiple values generally should be avoided for ease-of-use.
		
		Either the long or short name may be specified.  A long name starts with "--" and
		a short name starts with a single "-".  The query will always first look
		for the specified name; if not found it will look for the short/long equivalent.
		In the case of duplicates, the last specified option is always returned.
		
		@param 		name	name of the option
		@return			array of Strings associated with the option, or null if not found
	 */
	public String[]	getOptionValues( String name  )
							throws IllegalOptionException;
	
	/**
		Count the number of options parsed.
		
		@return	number of options parsed
	 */
	public int			countOptions();
	
	/**
		Count the number of instances of a given option.  All names are considered instances.
		<p>
		Example:
		<code>
		--count 1 -c 2
		</code>
		would return a count of 2, assuming "count" and "c" are the same option.
		
		@param name		name of the option
		@return	number of instances of the given option
	 */
	public int			countOptionInstances( String name ) throws IllegalOptionException;
		 
	
	/**
		Get all instances of the option.   All names are considered instances.
		<p>
		Example (assuming "count", "c", and "cnt" all are the same option):
		<code>
		--count 1 -c 2 --cnt=3
		</code>
		would return a ParsedOption[] of length 3:
		<code>
			{ "count", "1" }
			{ "c", "2" }
			{ "cnt", "3" }
		</code>
		
		@param name		name of the option
		@return	number of instances of the given option
		@throws IllegalOptionException if the option name is not legal
	 */
	public ParsedOption[]	getOptionInstances( String name ) throws IllegalOptionException;
	
	
	/**
		Get the &lt;i>last instance of</i> an option.
		<p>
		The name may be any legal name for the option. However, the last instance of the
		option that is found may or may not have the same name as 'name'.  If all instances
		are needed, call getOptionInstances( name ).
		
		@param name			name of the option (with or without leading "-" or "--")
		@return			value associated with the option
	 */
	public ParsedOption		getOption( String name )
								throws IllegalOptionException;
							
	/**
		Get the &lt;i>last instance of</i> a String option.
		If the option cannot be found, the default value is returned.
		<p>
		Same name handling as getArgValues().
		
		@param name			name of the option (with or without leading "-" or "--")
		@param defaultValue	a default value (may be null), returned if option not found
		@return			value associated with the option (possibly the default value)
	 */
	public String		getStringValue( String name, String defaultValue)
							throws IllegalOptionException;
	
	/**
		Get the &lt;i>last instance of</i> an Integer option.
		If the option cannot be found, the default value is returned.
		The default value may be null.
		<p>
		Calls getString( name, null ), and if a value exists, attempts to coerce it to an Integer.
		<p>
		Same name handling as getArgValues().
		
		@param name				name of the option (with or without leading "-" or "--")
		@param defaultValue		a default value (may be null), returned if option not found
		@throws		IllegalOptionException
	 */
	public Integer		getIntegerValue( String name, Integer defaultValue )
							throws IllegalOptionException;
	
	/**
		Get the &lt;i>last instance of</i> a Boolean option.
		
		If the option cannot be found, the default value is returned.
		The default value may be null.
		<p>
		Calls getString( name, null ), and if a value exists, attempts to coerce it to an Integer.
		<p>
		Same name handling as getArgValues().
		
		@param name			name of the option (with or without leading "-" or "--")
		@param defaultValue	a default value (may be null), returned if option not found
		@return			value associated with the option (possibly the default value)
		@throws		IllegalOptionException
	 */
	public Boolean		getBooleanValue( String name, Boolean defaultValue)
							throws IllegalOptionException;
	
	/**
		Get all operands (possibly none).  If there are no operands, an empty array is
		returned.  Operands follow implicitly, or may be specified explicitly via the
		special value "--".
		
		@return			value associated with the option (possibly the default value)
	 */
	public String []	getOperands( );
};



