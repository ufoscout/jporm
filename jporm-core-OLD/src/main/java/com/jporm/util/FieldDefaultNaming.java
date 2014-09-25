/*******************************************************************************
 * Copyright 2013 Francesco Cina'
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.jporm.util;

import com.jporm.factory.ObjectBuilder;


/**
 * 
 * @author Francesco Cina
 *
 * 21/mag/2011
 */
public abstract class FieldDefaultNaming {

    /**
     * Remove the "is", "get" and "set" prefix from a method name and return the default column
     * name associated to the method.
     * Es:
     * methodName = getEmployeeAge -> return EMPLOYEE_AGE
     * methodName = isMale -> return MALE
     * methodNAme =  employee -> return ""
     * @param methodName
     * @return
     */
    public static String getDefaultMappingNameForMethod(final String javaName) {
        String result = ObjectBuilder.EMPTY_STRING;

        String[] prefixs = new String[]{Constants.GET_METHOD_PREFIX,Constants.SET_METHOD_PREFIX,Constants.IS_METHOD_PREFIX};
        boolean found = false;
        for (String prefix : prefixs) {
            result = removePrefix(prefix, javaName);
            found = !result.equalsIgnoreCase(javaName);
            if (found) {
                break;
            }
        }
        if (!found)
        {
            return ""; //$NON-NLS-1$
        }
        return getJavanameToDBnameDefaultMapping(result);
    }

    /**
     * Return the default database object name associated to the javaName
     * Es:
     * methodName = getEmployeeAge -> return GET_EMPLOYEE_AGE
     * methodName = isMale -> return IS_MALE
     * methodNAme =  employee -> return EMPLOYEE
     * @param methodName
     * @return
     */
    public static String getJavanameToDBnameDefaultMapping(final String javaName) {
        StringBuilder result = new StringBuilder();
        if ( javaName.length()>0 ) {
            result.append(javaName.charAt(0));
            for (int i=1; i<javaName.length(); i++) {
                if ( Character.isUpperCase( javaName.charAt(i) ) ) {
                    result.append("_"); //$NON-NLS-1$
                }
                result.append(javaName.charAt(i));
            }
        }
        return result.toString().toUpperCase();
    }

    /**
     * Return the default java filed name name associated to the DB object name
     * Es:
     * dbName = GET_EMPLOYEE_AGE -> return employeeAge
     * dbName = IS_MALE -> return isMale
     * dbName = EMPLOYEE -> return employee
     * @param dbName
     * @return
     */
    public static String getDBnameToJavanameDefaultMapping(final String dbName, final boolean startWithUpperCase) {
        StringBuilder result = new StringBuilder();
        boolean upperCase = startWithUpperCase;
        for (int i=0; i<dbName.length(); i++) {
            Character character = Character.valueOf(dbName.charAt(i));
            if (character == '_') {
                upperCase = true;
                continue;
            }
            if (Character.isLetterOrDigit(character)) {
                if (upperCase) {
                    result.append(Character.toUpperCase(character));
                    upperCase = false;
                } else {
                    result.append(Character.toLowerCase(character));
                }
            }
        }
        return result.toString();
    }

    public static String removePrefix(final String prefix, final String methodName) {
        if (methodName.startsWith(prefix)) {
            return methodName.substring(prefix.length(), methodName.length());
        }
        return methodName;
    }

    /**
     * Return the default name of a getter for a property.
     * Es:
     * javaPropertyName = hello -> return getHello
     * @param javaPropertyName
     * @return
     */
    public static String getDefaultGetterName(final String javaPropertyName) {
        return Constants.GET_METHOD_PREFIX + javaPropertyName.substring(0, 1).toUpperCase() + javaPropertyName.substring(1);
    }

    /**
     * Return the default name of a getter for a property.
     * Es:
     * javaPropertyName = hello -> return setHello
     * @param javaPropertyName
     * @return
     */
    public static String getDefaultSetterName(final String javaPropertyName) {
        return Constants.SET_METHOD_PREFIX + javaPropertyName.substring(0, 1).toUpperCase() + javaPropertyName.substring(1);
    }


    /**
     * Return the default name of a getter for a property of type boolean.
     * Es:
     * javaPropertyName = hello -> return isHello
     * @param javaPropertyName
     * @return
     */
    public static String getDefaultBooleanGetterName(final String javaPropertyName) {
        return Constants.IS_METHOD_PREFIX + javaPropertyName.substring(0, 1).toUpperCase() + javaPropertyName.substring(1);
    }
}
