package test.all.bean;

import com.jporm.annotation.*;

// @Table can be used to specify the table and schema name. It is not mandatory.
// If not specified, JPO will determine the table name based on the class name
@Table(tableName = "USERS")
public class User {

    // @Id qualifies the current field as unique identifier for this bean
    @Id
    // @Generator is used to indicate an auto generated field. In this case the value is managed by JPO
    // and auto generated using the Database sequence USERS_SEQUENCE
    @Generator(generatorType = GeneratorType.SEQUENCE, name = "USERS_SEQUENCE")
    public Long id;

    // JPO determines the name of the database columns based on the field name, otherwise
    // the name can be manually specified using the @Column annotation
    @Column(name = "FIRST_NAME")
    public String firstName;

    // This field is automatically associated with the database column called LAST_NAME
    public String lastName;

    // This field is ignored by JPO
    @Ignore
    public String ignoredField;

}
