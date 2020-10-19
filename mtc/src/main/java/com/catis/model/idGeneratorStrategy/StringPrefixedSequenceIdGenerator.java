package com.catis.model.idGeneratorStrategy;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class StringPrefixedSequenceIdGenerator implements  IdentifierGenerator {

	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
		
		String prefix = "Test_";
        Connection connection = session.connection();

        try {
            Statement statement=connection.createStatement();

            ResultSet rs=statement.executeQuery("select count(id_partenaire) as Id from mydb.t_partenaire");

            if(rs.next())
            {
                int id=rs.getInt(1)+ 1;
                String generatedId = prefix + new Integer(id).toString();
                System.out.println("Generated Id: " + generatedId);
                return generatedId;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        return null;
    }
	}

	/*
	 * public static final String VALUE_PREFIX_PARAMETER = "valuePrefix"; public
	 * static final String VALUE_PREFIX_DEFAULT = ""; private String valuePrefix;
	 * 
	 * public static final String NUMBER_FORMAT_PARAMETER = "numberFormat"; public
	 * static final String NUMBER_FORMAT_DEFAULT = "%d"; private String
	 * numberFormat;
	 * 
	 * @Override public void configure(Type type, Properties params, ServiceRegistry
	 * serviceRegistry) throws MappingException { // TODO Auto-generated method stub
	 * super.configure(type, params, serviceRegistry); valuePrefix =
	 * ConfigurationHelper.getString(VALUE_PREFIX_PARAMETER, params,
	 * VALUE_PREFIX_DEFAULT); numberFormat =
	 * ConfigurationHelper.getString(NUMBER_FORMAT_PARAMETER, params,
	 * NUMBER_FORMAT_DEFAULT); }
	 * 
	 * @Override public Serializable generate(SharedSessionContractImplementor
	 * session, Object object) throws HibernateException {
	 * 
	 * return valuePrefix + String.format(numberFormat, super.generate(session,
	 * object)); }
	 */
   

