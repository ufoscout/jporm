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
package com.jporm.types;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.jporm.types.exception.JpoWrongTypeException;
import com.jporm.types.ext.BooleanToBigDecimalWrapper;
import com.jporm.types.ext.ByteToBigDecimalWrapper;
import com.jporm.types.ext.CharacterToStringWrapper;
import com.jporm.types.ext.DoubleToBigDecimalWrapper;
import com.jporm.types.ext.FloatToBigDecimalWrapper;
import com.jporm.types.ext.InstantToTimestampWrapper;
import com.jporm.types.ext.IntegerToBigDecimalWrapper;
import com.jporm.types.ext.LocalDateTimeToSqlTimestampWrapper;
import com.jporm.types.ext.LocalDateToSqlTimestampWrapper;
import com.jporm.types.ext.LongToBigDecimalWrapper;
import com.jporm.types.ext.ShortToBigDecimalWrapper;
import com.jporm.types.ext.UtilDateToSqlTimestampWrapper;
import com.jporm.types.ext.ZoneDateTimeToSqlTimestampWrapper;
import com.jporm.types.jdbc.ArrayJdbcIO;
import com.jporm.types.jdbc.ArrayNullWrapper;
import com.jporm.types.jdbc.BigDecimalJdbcIO;
import com.jporm.types.jdbc.BigDecimalNullWrapper;
import com.jporm.types.jdbc.BlobJdbcIO;
import com.jporm.types.jdbc.BlobNullWrapper;
import com.jporm.types.jdbc.BooleanPrimitiveJdbcIO;
import com.jporm.types.jdbc.BooleanPrimitiveNullWrapper;
import com.jporm.types.jdbc.BytePrimitiveJdbcIO;
import com.jporm.types.jdbc.BytePrimitiveNullWrapper;
import com.jporm.types.jdbc.BytesJdbcIO;
import com.jporm.types.jdbc.BytesNullWrapper;
import com.jporm.types.jdbc.ClobJdbcIO;
import com.jporm.types.jdbc.ClobNullWrapper;
import com.jporm.types.jdbc.DateJdbcIO;
import com.jporm.types.jdbc.DateNullWrapper;
import com.jporm.types.jdbc.DoublePrimitiveJdbcIO;
import com.jporm.types.jdbc.DoublePrimitiveNullWrapper;
import com.jporm.types.jdbc.FloatPrimitiveJdbcIO;
import com.jporm.types.jdbc.FloatPrimitiveNullWrapper;
import com.jporm.types.jdbc.InputStreamJdbcIO;
import com.jporm.types.jdbc.InputStreamNullWrapper;
import com.jporm.types.jdbc.IntegerPrimitiveJdbcIO;
import com.jporm.types.jdbc.IntegerPrimitiveNullWrapper;
import com.jporm.types.jdbc.LongPrimitiveJdbcIO;
import com.jporm.types.jdbc.LongPrimitiveNullWrapper;
import com.jporm.types.jdbc.NClobJdbcIO;
import com.jporm.types.jdbc.NClobNullWrapper;
import com.jporm.types.jdbc.ObjectJdbcIO;
import com.jporm.types.jdbc.ObjectNullWrapper;
import com.jporm.types.jdbc.ReaderJdbcIO;
import com.jporm.types.jdbc.ReaderNullWrapper;
import com.jporm.types.jdbc.RefJdbcIO;
import com.jporm.types.jdbc.RefNullWrapper;
import com.jporm.types.jdbc.RowIdJdbcIO;
import com.jporm.types.jdbc.RowIdNullWrapper;
import com.jporm.types.jdbc.SQLXMLJdbcIO;
import com.jporm.types.jdbc.SQLXMLNullWrapper;
import com.jporm.types.jdbc.ShortPrimitiveJdbcIO;
import com.jporm.types.jdbc.ShortPrimitiveNullWrapper;
import com.jporm.types.jdbc.StringJdbcIO;
import com.jporm.types.jdbc.StringNullWrapper;
import com.jporm.types.jdbc.TimeJdbcIO;
import com.jporm.types.jdbc.TimeNullWrapper;
import com.jporm.types.jdbc.TimestampJdbcIO;
import com.jporm.types.jdbc.TimestampNullWrapper;
import com.jporm.types.jdbc.URLJdbcIO;
import com.jporm.types.jdbc.URLNullWrapper;

/**
 *
 * @author ufo
 *
 */
public class TypeFactory {

	private final Map<Class<?>, JdbcIO<?>> jdbcIOs = new HashMap<Class<?>, JdbcIO<?>>();
	private final Map<Class<?>, TypeWrapperBuilder<?,?>> typeWrapperBuilders = new HashMap<Class<?>, TypeWrapperBuilder<?,?>>();

	public TypeFactory() {
		registerJdbcType();
		registerExtendedType();
	}

	public boolean isWrappedType(final Class<?> clazz) {
		if (typeWrapperBuilders.containsKey(clazz)) {
			return true;
		}
		checkAssignableFor(clazz);
		return typeWrapperBuilders.containsKey(clazz);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <P, DB> TypeWrapperJdbcReady<P,DB> getTypeWrapper(final Class<P> clazz) {
		if (isWrappedType(clazz)) {
			TypeWrapper<P,DB> typeWrapper = (TypeWrapper<P, DB>) typeWrapperBuilders.get(clazz).build((Class) clazz);
			JdbcIO<DB> jdbcIO = (JdbcIO<DB>) jdbcIOs.get(typeWrapper.jdbcType());
			return new TypeWrapperJdbcReady<P, DB>(typeWrapper, jdbcIO);
		}

		throw new JpoWrongTypeException("Cannot manipulate properties of type [" + clazz + "]. Allowed types [" + Arrays.toString( typeWrapperBuilders.keySet().toArray() ) + "]. Use another type or register a custom " + TypeWrapper.class.getName()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	private void registerJdbcType() {
		addType(new ArrayJdbcIO(), new ArrayNullWrapper());
		addType(new BigDecimalJdbcIO(), new BigDecimalNullWrapper());
		addType(new BlobJdbcIO(), new BlobNullWrapper());
		addType(new BooleanPrimitiveJdbcIO(), new BooleanPrimitiveNullWrapper());
		addType(new BytesJdbcIO(), new BytesNullWrapper());
		addType(new BytePrimitiveJdbcIO(), new BytePrimitiveNullWrapper());
		addType(new ClobJdbcIO(), new ClobNullWrapper());
		addType(new DateJdbcIO(), new DateNullWrapper());
		addType(new DoublePrimitiveJdbcIO(), new DoublePrimitiveNullWrapper());
		addType(new FloatPrimitiveJdbcIO(), new FloatPrimitiveNullWrapper());
		addType(new InputStreamJdbcIO(), new InputStreamNullWrapper());
		addType(new IntegerPrimitiveJdbcIO(), new IntegerPrimitiveNullWrapper());
		addType(new LongPrimitiveJdbcIO(), new LongPrimitiveNullWrapper());
		addType(new NClobJdbcIO(), new NClobNullWrapper());
		addType(new ObjectJdbcIO(), new ObjectNullWrapper());
		addType(new ReaderJdbcIO(), new ReaderNullWrapper());
		addType(new RefJdbcIO(), new RefNullWrapper());
		addType(new RowIdJdbcIO(), new RowIdNullWrapper());
		addType(new ShortPrimitiveJdbcIO(), new ShortPrimitiveNullWrapper());
		addType(new SQLXMLJdbcIO(), new SQLXMLNullWrapper());
		addType(new StringJdbcIO(), new StringNullWrapper());
		addType(new TimeJdbcIO(), new TimeNullWrapper());
		addType(new TimestampJdbcIO(), new TimestampNullWrapper());
		addType(new URLJdbcIO(), new URLNullWrapper());
	}

	private void registerExtendedType() {
		addTypeWrapper(new BooleanToBigDecimalWrapper());
		addTypeWrapper(new ByteToBigDecimalWrapper());
		addTypeWrapper(new CharacterToStringWrapper());
		addTypeWrapper(new DoubleToBigDecimalWrapper());
		addTypeWrapper(new FloatToBigDecimalWrapper());
		addTypeWrapper(new InstantToTimestampWrapper());
		addTypeWrapper(new IntegerToBigDecimalWrapper());
		addTypeWrapper(new LocalDateTimeToSqlTimestampWrapper());
		addTypeWrapper(new LocalDateToSqlTimestampWrapper());
		addTypeWrapper(new LongToBigDecimalWrapper());
		addTypeWrapper(new ShortToBigDecimalWrapper());
		addTypeWrapper(new UtilDateToSqlTimestampWrapper());
		addTypeWrapper(new TypeWrapperBuilderEnum() );
		addTypeWrapper(new ZoneDateTimeToSqlTimestampWrapper());
	}

	/**
	 * This method assures that for every {@link JdbcIO} there is a correspondent {@link TypeWrapper} that
	 * convert from and to the same type.
	 * @param jdbcIO
	 * @param typeWrapper
	 */
	private <DB> void addType( final JdbcIO<DB> jdbcIO, final TypeWrapper<DB, DB> typeWrapper) {
		jdbcIOs.put(jdbcIO.getDBClass(), jdbcIO);
		addTypeWrapper(typeWrapper.propertyType(), new TypeWrapperBuilderDefault<DB, DB>(typeWrapper) );
	}

	private <TYPE, DB> void addTypeWrapper( final Class<TYPE> clazz , final TypeWrapperBuilder<TYPE, DB> typeWrapperbuilder) {
		if (!jdbcIOs.containsKey(typeWrapperbuilder.jdbcType())) {
			throw new JpoWrongTypeException("Cannot register TypeWrapper " + typeWrapperbuilder.getClass() + ". The specified jdbc type " + typeWrapperbuilder.jdbcType() + " is not a valid type for the ResultSet and PreparedStatement getters/setters"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		typeWrapperBuilders.put(clazz, typeWrapperbuilder);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <TYPE> void addTypeWrapper( final TypeWrapper<TYPE, ?> typeWrapper) {
		addTypeWrapper(typeWrapper.propertyType(), new TypeWrapperBuilderDefault(typeWrapper));
	}

	public <TYPE> void addTypeWrapper( final TypeWrapperBuilder<TYPE, ?> typeWrapper) {
		addTypeWrapper(typeWrapper.propertyType(), typeWrapper);
	}

	synchronized private <TYPE> void checkAssignableFor(final Class<TYPE> versusClass) {
		TypeWrapperBuilder<TYPE, ?> candidate = null;
		for (Entry<Class<?>, TypeWrapperBuilder<?, ?>> twEntry : typeWrapperBuilders.entrySet()) {
			if (twEntry.getKey().isAssignableFrom(versusClass) && !twEntry.getKey().equals(Object.class) ) {
				candidate = (TypeWrapperBuilder<TYPE, ?>) twEntry.getValue();
				break;
			}
		}
		if (candidate != null) {
			addTypeWrapper(versusClass, candidate);
		}
	}
}
