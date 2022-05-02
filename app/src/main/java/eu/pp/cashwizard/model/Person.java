package eu.pp.cashwizard.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

import eu.pp.cashwizard.configuration.Conf;
import eu.pp.cashwizard.dict.Sex;
import eu.pp.cashwizard.util.JUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@Getter
//@Setter
//@EqualsAndHashCode
//@NoArgsConstructor
//@AllArgsConstructor
@Entity(tableName = "Person")
public class Person {

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name="Id")
    @NonNull
    private Long id;

    @ColumnInfo(name="FirstName")
    private String firstName;

    @ColumnInfo(name="LastName")
    private String lastName;

    @ColumnInfo(name="NickName")
    private String nickName;

    @ColumnInfo(name="DateBorn")
    private Date dateBorn;

    @ColumnInfo(name="Sex")
//    @Embedded
    private Sex sex;

    @ColumnInfo(name="PhotoFileName")
    private String photoFileName;

    private void setId() {
        id = JUtil.now().getTime();
        try {
            Thread.sleep( 1 );
        } catch (InterruptedException e) {
        }
    }

    public static Person newPerson() {
        Person person = new Person();
        return person;
    }

    public Person() {
        setId();
    }

    @Ignore
    public Person(String firstName, String lastName, String nickName, Date dateBorn, Sex sex) {
        setId();
        this.firstName = firstName;
        this.lastName = lastName;
        this.nickName = nickName;
        this.dateBorn = dateBorn;
        this.sex = sex;
        photoFileName = null;
    }

    @Ignore
    public Person(String firstName, String lastName, String nickName, Date dateBorn, Sex sex, String photoFileName ) {
        setId();
        this.firstName = firstName;
        this.lastName = lastName;
        this.nickName = nickName;
        this.dateBorn = dateBorn;
        this.sex = sex;
        this.photoFileName = photoFileName;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Person))
            return false;
        if (obj == this)
            return true;
        return this.id.equals(((Person) obj).id);
    }

    @Override
    public int hashCode(){
        return id.intValue();//for simplicity reason
    }


    public void updatePerson( Person p ) {
        this.setId( p.getId() );
        this.setFirstName( p.getFirstName() );
        this.setLastName( p.getLastName() );
        this.setNickName( p.getNickName() );
        this.setDateBorn( p.getDateBorn() );
        this.setSex( p.getSex() );
        this.setPhotoFileName( p.getPhotoFileName() );
    }

    public int compareToById(Person person) {
        if( getId() == null ) return 0;
        if( person.getId() == null ) return 0;
        return getId().compareTo( person.getId() );
    }

    public int compareToByNickName(Person person) {
        if( getNickName() == null ) return 0;
        if( person.getNickName() == null ) return 0;
        return getNickName().compareTo( person.getNickName() );
    }

    public boolean hasPhoto() {
        return (photoFileName!=null?true:false);
    }

    public boolean isMale() {
        return (sex!=null&&sex.isMale()?true:false);
    }

    public boolean isFemale() {
        return (sex!=null&&sex.isFemale()?true:false);
    }

    public String getPhotoFullPath() {
        if( photoFileName == null ) return null;
        return Conf.DIRECTORY_SMALL_PICTURES + "/" + photoFileName;
    }


    @NonNull
    public Long getId() {
        return id;
    }

    public void setId(@NonNull Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Date getDateBorn() {
        return dateBorn;
    }

    public void setDateBorn(Date dateBorn) {
        this.dateBorn = dateBorn;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public String getPhotoFileName() {
        return photoFileName;
    }

    public void setPhotoFileName(String photoFileName) {
        this.photoFileName = photoFileName;
    }
}
