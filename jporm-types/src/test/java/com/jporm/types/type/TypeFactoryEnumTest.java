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
/* ----------------------------------------------------------------------------
 *     PROJECT : JPOrm
 *
 *  CREATED BY : Francesco Cina'
 *          ON : Feb 23, 2013
 * ----------------------------------------------------------------------------
 */
package com.jporm.core.persistor.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;

import org.junit.Test;

import com.jporm.core.BaseTestApi;
import com.jporm.core.persistor.type.TypeFactory;
import com.jporm.core.persistor.type.ext.EnumWrapper;
import com.jporm.wrapper.TypeWrapper;

/**
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : Feb 23, 2013
 *
 * @author Francesco Cina'
 * @version $Revision
 */
public class TypeFactoryEnumTest extends BaseTestApi {

    private TypeFactory typeFactory = new TypeFactory();

    @Test
    public void testEnumWrapperNotNull() {
        assertNotNull(typeFactory.getTypeWrapper(Number.class));
        assertNotNull(typeFactory.getTypeWrapper(Color.class));
    }
    
    @Test
    public void testEnumWrapperOverriding() {
        assertEquals( EnumWrapper.class, typeFactory.getTypeWrapper(Number.class).getTypeWrapper().getClass());
        assertEquals( EnumWrapper.class, typeFactory.getTypeWrapper(Color.class).getTypeWrapper().getClass());
        
        typeFactory.addTypeWrapper(new NumberTypeWrapper());
        
        assertEquals( NumberTypeWrapper.class, typeFactory.getTypeWrapper(Number.class).getTypeWrapper().getClass());
        assertEquals( EnumWrapper.class, typeFactory.getTypeWrapper(Color.class).getTypeWrapper().getClass());
    }
    
    @Test
    public void testEnumWrapperOverridingAndUse() {
        
        TypeWrapper<Color, Object> colorWrapper = typeFactory.getTypeWrapper(Color.class).getTypeWrapper();
        assertEquals( "WHITE",  colorWrapper.unWrap( Color.WHITE ) );
        assertEquals( Color.BLUE,  colorWrapper.wrap( "BLUE" ) );
        
        TypeWrapper<Number, Object> numberWrapper = typeFactory.getTypeWrapper(Number.class).getTypeWrapper();
        assertEquals( "ONE",  numberWrapper.unWrap( Number.ONE ) );
        assertEquals( Number.TWO,  numberWrapper.wrap( "TWO" ) );
        
        typeFactory.addTypeWrapper(new NumberTypeWrapper());
        
        TypeWrapper<Number, Object> overriddenNumberWrapper = typeFactory.getTypeWrapper(Number.class).getTypeWrapper();
        assertEquals( BigDecimal.valueOf(1),  overriddenNumberWrapper.unWrap( Number.ONE ) );
        assertEquals( Number.TWO,  overriddenNumberWrapper.wrap( BigDecimal.valueOf(2) ) );
        
        
    }
    
    enum Number {
        ZERO(BigDecimal.valueOf(0)),
        ONE(BigDecimal.valueOf(1)),
        TWO(BigDecimal.valueOf(2));
        
        private final BigDecimal value;

        Number (BigDecimal value) {
            this.value = value;
        }

        public BigDecimal getValue() {
            return value;
        }

        static Number fromValue(BigDecimal fromValue) {
            if (BigDecimal.valueOf(0).equals(fromValue)) return ZERO;
            if (BigDecimal.valueOf(1).equals(fromValue)) return ONE;
            if (BigDecimal.valueOf(2).equals(fromValue)) return TWO;
            return null;
        }

    }
    
    enum Color {
        WHITE,
        BLUE
    }
    
    class NumberTypeWrapper implements TypeWrapper<Number, BigDecimal> {

        @Override
        public Class<BigDecimal> jdbcType() {
            return BigDecimal.class;
        }

        @Override
        public Class<Number> propertyType() {
            return Number.class;
        }

        @Override
        public Number wrap(BigDecimal value) {
            return Number.fromValue(value);
        }

        @Override
        public BigDecimal unWrap(Number value) {
            return value.getValue();
        }

        @Override
        public Number clone(Number source) {
            return source;
        }
        
    }

}
