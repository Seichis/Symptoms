package com.masterthesis.personaldata.symptoms.DAO.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;

@DatabaseTable(tableName = "patients")
public class Patient extends BaseDAO {
    // for QueryBuilder to be able to find the fields
    public static final String NAME_FIELD_NAME = "name";
    public static final String GENDER_FIELD_NAME = "gender";
    public static final String IMAGE_FIELD_NAME = "picture";

    @DatabaseField(columnName = IMAGE_FIELD_NAME, dataType = DataType.BYTE_ARRAY)
    byte[] profilePic;
    @DatabaseField(columnName = NAME_FIELD_NAME, canBeNull = false)
    String name;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @DatabaseField(columnName = GENDER_FIELD_NAME)
    String gender;

    public Patient() {
    }
    public Patient(Timestamp createdAt) {
        super(createdAt);
    }

    public String getPatientName() {
        return name;
    }

    public void setPatientName(String name) {
        this.name = name;
    }

    public Bitmap getProfilePic() {
        return BitmapFactory.decodeByteArray(profilePic, 0, profilePic.length);
    }

    public void setProfilePic(Bitmap bitmap) {
        ByteArrayOutputStream blob = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, blob);
        this.profilePic = blob.toByteArray();
    }


}

