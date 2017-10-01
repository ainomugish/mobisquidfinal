package com.mobisquid.mobicash.dbstuff;


import com.orm.SugarRecord;

public class TempStore extends SugarRecord {

    String mobile;
    String firstName;
    String lastName;
    String idNo ;
    String gender ;
    String martalStatus ;
    String language ;
    String occupation;
    String dob ;
    String email ;
    String address ;
    String nokName ;
    String nokPhone ;
    String nokRelation ;
    String imei;
    String location;
    String idtype ;
    String tempresnr ;
    String idexpiration;
    String  faceImage ;
    String idImage ;
    String residencephoto;

    public String getCountrycode() {
        return countrycode;
    }

    public void setCountrycode(String countrycode) {
        this.countrycode = countrycode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    String countrycode;
    String city;
    String province;
    String password;
    String pin;

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    String other;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    String username;


    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMartalStatus() {
        return martalStatus;
    }

    public void setMartalStatus(String martalStatus) {
        this.martalStatus = martalStatus;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNokName() {
        return nokName;
    }

    public void setNokName(String nokName) {
        this.nokName = nokName;
    }

    public String getNokPhone() {
        return nokPhone;
    }

    public void setNokPhone(String nokPhone) {
        this.nokPhone = nokPhone;
    }

    public String getNokRelation() {
        return nokRelation;
    }

    public void setNokRelation(String nokRelation) {
        this.nokRelation = nokRelation;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getIdtype() {
        return idtype;
    }

    public void setIdtype(String idtype) {
        this.idtype = idtype;
    }

    public String getTempresnr() {
        return tempresnr;
    }

    public void setTempresnr(String tempresnr) {
        this.tempresnr = tempresnr;
    }

    public String getIdexpiration() {
        return idexpiration;
    }

    public void setIdexpiration(String idexpiration) {
        this.idexpiration = idexpiration;
    }

    public String getFaceImage() {
        return faceImage;
    }

    public void setFaceImage(String faceImage) {
        this.faceImage = faceImage;
    }

    public String getIdImage() {
        return idImage;
    }

    public void setIdImage(String idImage) {
        this.idImage = idImage;
    }

    public String getResidencephoto() {
        return residencephoto;
    }

    public void setResidencephoto(String residencephoto) {
        this.residencephoto = residencephoto;
    }

    public TempStore(){

    }

    public String getSecondname() {
        return secondname;
    }

    public void setSecondname(String secondname) {
        this.secondname = secondname;
    }

    String secondname;
    public TempStore(String mobile, String firstName,String secondname,String username,String idNo,String idexpiration,String idtype
    ,String language,String gender, String martalStatus,String dob,String email,String occupation,String nokName,
                     String nokRelation,String nokPhone,String address,String countrycode,String city,String province,
                     String faceImage,String residencephoto,String idImage,String password,String pin,String other){
        this.firstName = firstName;
        this.mobile=mobile;
        this.secondname = secondname;
        this.username = username;
        this.idNo = idNo;
        this.idexpiration = idexpiration;
        this.idtype = idtype;
        this.language = language;
        this.gender = gender;
        this.martalStatus = martalStatus;
        this.countrycode=countrycode;
        this.city = city;
        this.province=province;
        this.dob = dob;
        this.email = email;
        this.occupation = occupation;
        this.nokName = nokName;
        this.nokRelation = nokRelation;
        this.nokPhone = nokPhone;
        this.address = address;
        this.faceImage = faceImage;
        this.residencephoto = residencephoto;
        this.idImage = idImage;
        this.password =password;
        this.pin=pin;
        this.other=other;

    }



}
