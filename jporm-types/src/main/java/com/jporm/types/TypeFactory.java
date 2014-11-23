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
package com.jporm.core.persistor.type;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.jporm.core.persistor.type.ext.BooleanToBigDecimalWrapper;
import com.jporm.core.persistor.type.ext.ByteToBigDecimalWrapper;
import com.jporm.core.persistor.type.ext.CharacterToStringWrapper;
import com.jporm.core.persistor.type.ext.DoubleToBigDecimalWrapper;
import com.jporm.core.persistor.type.ext.FloatToBigDecimalWrapper;
import com.jporm.core.persistor.type.ext.InstantToTimestampWrapper;
import com.jporm.core.persistor.type.ext.IntegerToBigDecimalWrapper;
import com.jporm.core.persistor.type.ext.LocalDateTimeToSqlTimestampWrapper;
import com.jporm.core.persistor.type.ext.LocalDateToSqlTimestampWrapper;
import com.jporm.core.persistor.type.ext.LongToBigDecimalWrapper;
import com.jporm.core.persistor.type.ext.ShortToBigDecimalWrapper;
import com.jporm.core.persistor.type.ext.UtilDateToSqlTimestampWrapper;
import com.jporm.core.persistor.type.ext.ZoneDateTimeToSqlTimestampWrapper;
import com.jporm.core.persistor.type.jdbc.ArrayJdbcIO;
import com.jporm.core.persistor.type.jdbc.ArrayNullWrapper;
import com.jporm.core.persistor.type.jdbc.BigDecimalJdbcIO;
import com.jporm.core.persistor.type.jdbc.BigDecimalNullWrapper;
import com.jporm.core.persistor.type.jdbc.BlobJdbcIO;
import com.jporm.core.persistor.type.jdbc.BlobNullWrapper;
import com.jporm.core.persistor.type.jdbc.BooleanPrimitiveJdbcIO;
import com.jporm.core.persistor.type.jdbc.BooleanPrimitiveNullWrapper;
import com.jporm.core.persistor.type.jdbc.BytePrimitiveJdbcIO;
import com.jporm.core.persistor.type.jdbc.BytePrimitiveNullWrapper;
import com.jporm.core.persistor.type.jdbc.BytesJdbcIO;
import com.jporm.core.persistor.type.jdbc.BytesNullWrapper;
import com.jporm.core.persistor.type.jdbc.ClobJdbcIO;
import com.jporm.core.persistor.type.jdbc.ClobNullWrapper;
import com.jporm.core.persistor.type.jdbc.DateJdbcIO;
import com.jporm.core.persistor.type.jdbc.DateNullWrapper;
import com.jporm.core.persistor.type.jdbc.DoublePrimitiveJdbcIO;
import com.jporm.core.persistor.type.jdbc.DoublePrimitiveNullWrapper;
import com.jporm.core.persistor.type.jdbc.FloatPrimitiveJdbcIO;
import com.jporm.core.persistor.type.jdbc.FloatPrimitiveNullWrapper;
import com.jporm.core.persistor.type.jdbc.InputStreamJdbcIO;
import com.jporm.core.persistor.type.jdbc.InputStreamNullWrapper;
import com.jporm.core.persistor.type.jdbc.IntegerPrimitiveJdbcIO;
import com.jporm.core.persistor.type.jdbc.IntegerPrimitiveNullWrapper;
import com.jporm.core.persistor.type.jdbc.LongPrimitiveJdbcIO;
import com.jporm.core.persistor.type.jdbc.LongPrimitiveNullWrapper;
import com.jporm.core.persistor.type.jdbc.NClobJdbcIO;
import com.jporm.core.persistor.type.jdbc.NClobNullWrapper;
import com.jporm.core.persistor.type.jdbc.ObjectJdbcIO;
import com.jporm.core.persistor.type.jdbc.ObjectNullWrapper;
import com.jporm.core.persistor.type.jdbc.ReaderJdbcIO;
import com.jporm.core.persistor.type.jdbc.ReaderNullWrapper;
import com.jporm.core.persistor.type.jdbc.RefJdbcIO;
import com.jporm.core.persistor.type.jdbc.RefNullWrapper;
import com.jporm.core.persistor.type.jdbc.RowIdJdbcIO;
import com.jporm.core.persistor.type.jdbc.RowIdNullWrapper;
import com.jporm.core.persistor.type.jdbc.SQLXMLJdbcIO;
import com.jporm.core.persistor.type.jdbc.SQLXMLNullWrapper;
import com.jporm.core.persistor.type.jdbc.ShortPrimitiveJdbcIO;
import com.jporm.core.persistor.type.jdbc.ShortPrimitiveNullWrapper;
import com.jporm.core.persistor.type.jdbc.StringJdbcIO;
import com.jporm.core.persistor.type.jdbc.StringNullWrapper;
import com.jporm.core.persistor.type.jdbc.TimeJdbcIO;
import com.jporm.core.persistor.type.jdbc.TimeNullWrapper;
import com.jporm.core.persistor.type.jdbc.TimestampJdbcIO;
import com.jporm.core.persistor.type.jdbc.TimestampNullWrapper;
import com.jporm.core.persistor.type.jdbc.URLJdbcIO;
import com.jporm.core.persistor.type.jdbc.URLNullWrapper;
import com.jporm.core.util.MapUtil;
import com.jporm.exception.OrmConfigurationException;
import com.jporm.wrapper.TypeWrapper;
import com.jporm.wrapper.TypeWrapperBuilder;

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
		throw new OrmConfigurationException("Cannot manipulate properties of type [" + clazz + "]. Allowed types [" + MapUtil.keysToString(typeWrapperBuilders) + "]. Use another type or register a custom " + TypeWrapper.class.getName()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	private void registerJdbcType() {
		this.addType(new ArrayJdbcIO(), new ArrayNullWrapper());
		this.addType(new BigDecimalJdbcIO(), new BigDecimalNullWrapper());
		this.addType(new BlobJdbcIO(), new BlobNullWrapper());
		this.addType(new BooleanPrimitiveJdbcIO(), new BooleanPrimitiveNullWrapper());
		this.addType(new BytesJdbcIO(), new BytesNullWrapper());
		this.addType(new BytePrimitiveJdbcIO(), new BytePrimitiveNullWrapper());
		this.addType(new ClobJdbcIO(), new ClobNullWrapper());
		this.addType(new DateJdbcIO(), new DateNullWrapper());
		this.addType(new DoublePrimitiveJdbcIO(), new DoublePrimitiveNullWrapper());
		this.addType(new FloatPrimitiveJdbcIO(), new FloatPrimitiveNullWrapper());
		this.addType(new InputStreamJdbcIO(), new InputStreamNullWrapper());
		this.addType(new IntegerPrimitiveJdbcIO(), new IntegerPrimitiveNullWrapper());
		this.addType(new LongPrimitiveJdbcIO(), new LongPrimitiveNullWrapper());
		this.addType(new NClobJdbcIO(), new NClobNullWrapper());
		this.addType(new ObjectJdbcIO(), new ObjectNullWrapper());
		this.addType(new ReaderJdbcIO(), new ReaderNullWrapper());
		this.addType(new RefJdbcIO(), new RefNullWrapper());
		this.addType(new RowIdJdbcIO(), new RowIdNullWrapper());
		this.addType(new ShortPrimitiveJdbcIO(), new ShortPrimitiveNullWrapper());
		this.addType(new SQLXMLJdbcIO(), new SQLXMLNullWrapper());
		this.addType(new StringJdbcIO(), new StringNullWrapper());
		this.addType(new TimeJdbcIO(), new TimeNullWrapper());
		this.addType(new TimestampJdbcIO(), new TimestampNullWrapper());
		this.addType(new URLJdbcIO(), new URLNullWrapper());
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
			throw new OrmConfigurationException("Cannot register TypeWrapper " + typeWrapperbuilder.getClass() + ". The specified jdbc type " + typeWrapperbuilder.jdbcType() + " is not a valid type for the ResultSet and PreparedStatement getters/setters"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
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
