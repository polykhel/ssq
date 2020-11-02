package com.polykhel.ssq.util;

import org.hibernate.dialect.PostgreSQL10Dialect;
import org.hibernate.type.descriptor.sql.BinaryTypeDescriptor;
import org.hibernate.type.descriptor.sql.SqlTypeDescriptor;

import java.sql.Types;

/**
 * <p>FixedPostgreSQL10Dialect class.</p>
 */
@SuppressWarnings("squid:S110")
public class FixedPostgreSQL10Dialect extends PostgreSQL10Dialect {

    /**
     * <p>Constructor for FixedPostgreSQL95Dialect.</p>
     */
    public FixedPostgreSQL10Dialect() {
        super();
        registerColumnType(Types.BLOB, "bytea");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SqlTypeDescriptor remapSqlTypeDescriptor(SqlTypeDescriptor sqlTypeDescriptor) {
        if (sqlTypeDescriptor.getSqlType() == Types.BLOB) {
            return BinaryTypeDescriptor.INSTANCE;
        }
        return super.remapSqlTypeDescriptor(sqlTypeDescriptor);
    }
}
